package com.sih.tourism.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupGuardService {

    private final GroupService groupService;
    private final GroupMemberRepository groupMemberRepository;
    private final LocationUpdateRepository locationUpdateRepository;
    private final SafetyAlertRepository safetyAlertRepository;

    @Autowired
    public GroupGuardService(GroupService groupService,
                              GroupMemberRepository groupMemberRepository,
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
            member.setOutOfBounds(true);
            groupMemberRepository.save(member);

            SafetyAlert alert = new SafetyAlert(member, group, distance);
            safetyAlertRepository.save(alert);

        } else if (!isOutside && member.isOutOfBounds()) {
            // Re-entry event: reset state, no alert created.
            member.setOutOfBounds(false);
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
}
