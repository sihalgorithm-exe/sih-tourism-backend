package com.sih.tourism.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sih.tourism.dto.request.LocationUpdateRequest;
import com.sih.tourism.entity.GroupMember;
import com.sih.tourism.entity.LocationUpdate;
import com.sih.tourism.entity.SafetyAlert;
import com.sih.tourism.entity.TravelGroup;
import com.sih.tourism.exception.ResourceNotFoundException;
import com.sih.tourism.exception.UnauthorizedActionException;
import com.sih.tourism.repository.GroupMemberRepository;
import com.sih.tourism.repository.LocationUpdateRepository;
import com.sih.tourism.repository.SafetyAlertRepository;
import com.sih.tourism.util.HaversineCalculator;

@Service
public class GroupGuardService {

    private final GroupService groupService;
    private final GroupMemberRepository groupMemberRepository;
    private final LocationUpdateRepository locationUpdateRepository;
        private final SafetyAlertRepository safetyAlertRepository;

    @org.springframework.beans.factory.annotation.Value("${groupguard.response-timeout-minutes:5}")
    private int responseTimeoutMinutes;

    @Autowired
    public GroupGuardService(GroupService groupService,                          GroupMemberRepository groupMemberRepository,
                              LocationUpdateRepository locationUpdateRepository,
                              SafetyAlertRepository safetyAlertRepository) {
        this.groupService = groupService;
        this.groupMemberRepository = groupMemberRepository;
        this.locationUpdateRepository = locationUpdateRepository;
        this.safetyAlertRepository = safetyAlertRepository;
    }

    /**
     * Records a location ping from a group member (leader or regular member) and,
     * for non-leader members, runs the exit-event state machine against the
     * leader's most recent known location.
     *
     * State machine (per member):
     *   outside radius AND not already flagged  -> create ONE alert, flag = true
     *   outside radius AND already flagged       -> do nothing (no duplicate alert)
     *   inside radius  AND flagged                -> reset flag = false (no alert)
     *   inside radius  AND not flagged            -> do nothing
     */
    public LocationUpdate submitLocation(Long groupId, Long userId, LocationUpdateRequest request) {
        TravelGroup group = groupService.getGroupOrThrow(groupId);
        GroupMember member = groupService.getMembershipOrThrow(userId, groupId);

        LocationUpdate update = new LocationUpdate(member, request.getLatitude(), request.getLongitude());
        LocationUpdate savedUpdate = locationUpdateRepository.save(update);

        boolean isLeader = groupService.isLeader(group, userId);

        if (!isLeader) {
            evaluateExitEvent(group, member, savedUpdate);
        }

        return savedUpdate;
    }

    private void evaluateExitEvent(TravelGroup group, GroupMember member, LocationUpdate memberLocation) {
        GroupMember leaderMembership = groupMemberRepository
                .findByUserIdAndGroupId(group.getLeader().getId(), group.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Leader membership record not found"));

        Optional<LocationUpdate> leaderLatestLocation =
                locationUpdateRepository.findFirstByGroupMemberIdOrderByRecordedAtDesc(leaderMembership.getId());

        // If the leader hasn't sent a location yet, we can't evaluate distance - known limitation, skip safely.
        if (leaderLatestLocation.isEmpty()) {
            return;
        }

        LocationUpdate leaderLocation = leaderLatestLocation.get();

        double distance = HaversineCalculator.distanceInMeters(
                leaderLocation.getLatitude(), leaderLocation.getLongitude(),
                memberLocation.getLatitude(), memberLocation.getLongitude()
        );

        boolean isOutside = distance > group.getRadiusMeters();

                if (isOutside && !member.isOutOfBounds()) {
            // Exit event: member just crossed outside the radius. Create exactly one alert.
            // (Unchanged) - this is still the ONLY place that fires the Team Lead alert
            // for the out-of-range condition itself.
            member.setOutOfBounds(true);
            member.setGuardStatus("PENDING_RESPONSE");
            member.setStatusUpdatedAt(java.time.LocalDateTime.now());
            groupMemberRepository.save(member);

            SafetyAlert alert = new SafetyAlert(member, group, distance);
            safetyAlertRepository.save(alert);

        } else if (!isOutside && member.isOutOfBounds()) {
            // Re-entry event: reset state, no alert created.
            member.setOutOfBounds(false);
            // Member came back into range before answering (or after answering) -
            // clear any pending/late guard state per requirement: "if the member
            // returns within range before responding, handle this appropriately".
            if ("PENDING_RESPONSE".equals(member.getGuardStatus()) || "NO_RESPONSE".equals(member.getGuardStatus())) {
                member.setGuardStatus("OK");
                member.setStatusUpdatedAt(null);
            }
            groupMemberRepository.save(member);
        }
        // Remaining two cases (outside+already flagged, inside+not flagged) require no action.
    }

    public List<SafetyAlert> getAlertsForGroup(Long groupId, Long requesterUserId) {
        TravelGroup group = groupService.getGroupOrThrow(groupId);

        // Only the leader can view the group's alert feed.
        if (!groupService.isLeader(group, requesterUserId)) {
            throw new UnauthorizedActionException("Only the group leader can view safety alerts");
        }

        return safetyAlertRepository.findByGroupIdOrderByTriggeredAtDesc(groupId);
    }

        /**
     * Returns the calling member's own guard status plus enough location info
     * to (a) drive the "Are you lost?" prompt and (b) build a directions link
     * to the leader if they choose "I'm Lost". Reuses the same membership and
     * location lookups already used by submitLocation/evaluateExitEvent.
     */
    public GroupMember getMyMembership(Long groupId, Long userId) {
        groupService.getGroupOrThrow(groupId);
        return groupService.getMembershipOrThrow(userId, groupId);
    }

    public Optional<LocationUpdate> getLatestLocation(Long groupMemberId) {
        return locationUpdateRepository.findFirstByGroupMemberIdOrderByRecordedAtDesc(groupMemberId);
    }

    /**
     * Records the member's answer to the "Are you lost?" prompt.
     * Reuses the existing guardStatus field (no second status system) and the
     * existing SafetyAlert table/list for notifying the Team Lead.
     */
    public GroupMember respondToGuardPrompt(Long groupId, Long userId, String response) {
        TravelGroup group = groupService.getGroupOrThrow(groupId);
        GroupMember member = groupService.getMembershipOrThrow(userId, groupId);

        if (!"PENDING_RESPONSE".equals(member.getGuardStatus()) && !"NO_RESPONSE".equals(member.getGuardStatus())) {
            throw new UnauthorizedActionException("There is no active out-of-range prompt to respond to");
        }

        Double lastDistance = locationUpdateRepository
                .findFirstByGroupMemberIdOrderByRecordedAtDesc(member.getId())
                .map(loc -> {
                    Optional<LocationUpdate> leaderLoc = locationUpdateRepository
                            .findFirstByGroupMemberIdOrderByRecordedAtDesc(
                                    groupMemberRepository.findByUserIdAndGroupId(group.getLeader().getId(), group.getId())
                                            .map(GroupMember::getId).orElse(-1L));
                    return leaderLoc.map(leader -> HaversineCalculator.distanceInMeters(
                            leader.getLatitude(), leader.getLongitude(), loc.getLatitude(), loc.getLongitude()
                    )).orElse(0.0);
                })
                .orElse(0.0);

        switch (response) {
            case "SAFE":
                // Requirement: resolve the warning, no emergency alert, no Team Lead ping.
                member.setGuardStatus("SAFE");
                member.setStatusUpdatedAt(null);
                break;
            case "LOST":
                member.setGuardStatus("LOST");
                member.setStatusUpdatedAt(null);
                safetyAlertRepository.save(new SafetyAlert(member, group, lastDistance, "LOST"));
                break;
            case "NEEDS_HELP":
                member.setGuardStatus("NEEDS_HELP");
                member.setStatusUpdatedAt(null);
                safetyAlertRepository.save(new SafetyAlert(member, group, lastDistance, "NEEDS_HELP"));
                break;
            default:
                throw new IllegalArgumentException("Unknown guard response: " + response);
        }

        return groupMemberRepository.save(member);
    }

    /**
     * Scheduled sweep for the 5-minute no-response escalation. Runs every 30s
     * and flips anyone still PENDING_RESPONSE past the configured timeout.
     * A sweep is used instead of one timer per member so it survives page
     * reloads, closed browsers, and server restarts without duplicating state.
     */
    @org.springframework.scheduling.annotation.Scheduled(fixedDelay = 30000)
    public void escalateUnansweredPrompts() {
        LocalDateTime cutoff = LocalDateTime.now().minusMinutes(responseTimeoutMinutes);
        List<GroupMember> overdue = groupMemberRepository.findByGuardStatusAndStatusUpdatedAtBefore("PENDING_RESPONSE", cutoff);

        for (GroupMember member : overdue) {
            member.setGuardStatus("NO_RESPONSE");
            groupMemberRepository.save(member);

            Double lastDistance = locationUpdateRepository
                    .findFirstByGroupMemberIdOrderByRecordedAtDesc(member.getId())
                    .map(LocationUpdate::getLatitude).map(x -> 0.0).orElse(0.0);

            safetyAlertRepository.save(new SafetyAlert(member, member.getGroup(), lastDistance, "NO_RESPONSE"));
        }
    }
    
}
