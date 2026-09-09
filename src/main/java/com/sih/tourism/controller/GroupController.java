package com.sih.tourism.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sih.tourism.dto.response.GroupMemberResponse;
import com.sih.tourism.dto.response.GroupMembersResponse;
import com.sih.tourism.dto.response.GroupResponse;
import com.sih.tourism.entity.GroupMember;
import com.sih.tourism.entity.LocationUpdate;
import com.sih.tourism.entity.TravelGroup;
import com.sih.tourism.exception.UnauthorizedActionException;
import com.sih.tourism.repository.LocationUpdateRepository;
import com.sih.tourism.security.SecurityUtil;
import com.sih.tourism.service.GroupService;
import com.sih.tourism.util.HaversineCalculator;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;
    private final LocationUpdateRepository locationUpdateRepository;

    @Autowired
    public GroupController(
            GroupService groupService,
            LocationUpdateRepository locationUpdateRepository
    ) {
        this.groupService = groupService;
        this.locationUpdateRepository = locationUpdateRepository;
    }

    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(
            @RequestBody com.sih.tourism.dto.request.CreateGroupRequest request
    ) {

        Long userId = SecurityUtil.getCurrentUserId();

        TravelGroup group = groupService.createGroup(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(group));
    }

    /**
     * Current authenticated user joins the group.
     */
    @PostMapping("/{groupId}/join")
    public ResponseEntity<GroupResponse> joinGroup(
            @PathVariable Long groupId
    ) {

        Long userId = SecurityUtil.getCurrentUserId();

        groupService.joinGroup(groupId, userId);

        TravelGroup group = groupService.getGroupOrThrow(groupId);

        return ResponseEntity.ok(toResponse(group));
    }

    @GetMapping("/{groupId}")
    public GroupResponse getGroup(@PathVariable Long groupId) {

        TravelGroup group = groupService.getGroupOrThrow(groupId);

        return toResponse(group);
    }

    /**
     * Returns the members and their latest known locations.
     * Only the group leader can access this.
     */
    @GetMapping("/{groupId}/members")
    public GroupMembersResponse getMembers(@PathVariable Long groupId) {

        Long requesterId = SecurityUtil.getCurrentUserId();

        TravelGroup group = groupService.getGroupOrThrow(groupId);

        if (!groupService.isLeader(group, requesterId)) {
            throw new UnauthorizedActionException(
                    "Only the group leader can view member locations"
            );
        }

        List<GroupMember> members = groupService.getMembers(groupId);

        GroupMember leaderMembership = groupService
                .getMembershipOrThrow(group.getLeader().getId(), groupId);

        Optional<LocationUpdate> leaderLocation =
                locationUpdateRepository
                        .findFirstByGroupMemberIdOrderByRecordedAtDesc(
                                leaderMembership.getId()
                        );

        List<GroupMemberResponse> responses = new ArrayList<>();

        for (GroupMember member : members) {

            Optional<LocationUpdate> latest =
                    locationUpdateRepository
                            .findFirstByGroupMemberIdOrderByRecordedAtDesc(
                                    member.getId()
                            );

            Double latitude = null;
            Double longitude = null;
            java.time.LocalDateTime lastUpdated = null;
            Double distanceMeters = null;

            if (latest.isPresent()) {

                LocationUpdate location = latest.get();

                latitude = location.getLatitude();
                longitude = location.getLongitude();
                lastUpdated = location.getRecordedAt();

                if (leaderLocation.isPresent()
                        && !member.getUser().getId()
                        .equals(group.getLeader().getId())) {

                    LocationUpdate leader = leaderLocation.get();

                    distanceMeters = HaversineCalculator.distanceInMeters(
                            leader.getLatitude(),
                            leader.getLongitude(),
                            location.getLatitude(),
                            location.getLongitude()
                    );
                }
            }

            responses.add(
                    new GroupMemberResponse(
                            member.getId(),
                            member.getUser().getId(),
                            member.getUser().getName(),
                            member.getUser().getId()
                                    .equals(group.getLeader().getId()),
                            latitude,
                            longitude,
                            lastUpdated,
                            distanceMeters
                    )
            );
        }

        return new GroupMembersResponse(groupId, responses);
    }

    /**
     * Returns the groups that the currently authenticated user belongs to.
     */
    @GetMapping("/my")
    public List<GroupResponse> getMyGroups() {

        Long userId = SecurityUtil.getCurrentUserId();

        return groupService.getMyGroups(userId)
                .stream()
                .map(GroupMember::getGroup)
                .distinct()
                .map(this::toResponse)
                .toList();
    }

    private GroupResponse toResponse(TravelGroup group) {

        return new GroupResponse(
                group.getId(),
                group.getName(),
                group.getLeader().getId(),
                group.getLeader().getName(),
                group.getRadiusMeters()
        );
    }
}