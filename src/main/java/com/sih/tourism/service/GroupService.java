package com.sih.tourism.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sih.tourism.dto.request.CreateGroupRequest;
import com.sih.tourism.entity.GroupMember;
import com.sih.tourism.entity.TravelGroup;
import com.sih.tourism.entity.User;
import com.sih.tourism.exception.DuplicateResourceException;
import com.sih.tourism.exception.ResourceNotFoundException;
import com.sih.tourism.exception.UnauthorizedActionException;
import com.sih.tourism.repository.GroupMemberRepository;
import com.sih.tourism.repository.LocationUpdateRepository;
import com.sih.tourism.repository.SafetyAlertRepository;
import com.sih.tourism.repository.TravelGroupRepository;
import com.sih.tourism.repository.UserRepository;
@Service
public class GroupService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final LocationUpdateRepository locationUpdateRepository;
    private final SafetyAlertRepository safetyAlertRepository;

    @Autowired
    public GroupService(
            TravelGroupRepository travelGroupRepository,
            GroupMemberRepository groupMemberRepository,
            UserRepository userRepository,
            LocationUpdateRepository locationUpdateRepository,
            SafetyAlertRepository safetyAlertRepository
    ) {
        this.travelGroupRepository = travelGroupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userRepository = userRepository;
        this.locationUpdateRepository = locationUpdateRepository;
        this.safetyAlertRepository = safetyAlertRepository;
    }

    public TravelGroup createGroup(Long creatorUserId, CreateGroupRequest request) {

        User creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TravelGroup group = new TravelGroup(
                request.getName(),
                creator,
                request.getRadiusMeters()
        );

        TravelGroup savedGroup = travelGroupRepository.save(group);

        GroupMember leaderMembership = new GroupMember(creator, savedGroup);
        groupMemberRepository.save(leaderMembership);

        return savedGroup;
    }

    /**
     * Current authenticated user joins the group.
     */
    public GroupMember joinGroup(Long groupId, Long userId) {

        TravelGroup group = getGroupOrThrow(groupId);

        if (groupMemberRepository.existsByUserIdAndGroupId(userId, groupId)) {
            throw new DuplicateResourceException("You are already a member of this group");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        GroupMember membership = new GroupMember(user, group);

        return groupMemberRepository.save(membership);
    }

    /**
     * Kept for compatibility if another part of the project still uses it.
     * New frontend flow should use joinGroup().
     */
    public GroupMember addMember(Long groupId, Long requesterUserId, Long newMemberUserId) {

        TravelGroup group = getGroupOrThrow(groupId);

        if (!group.getLeader().getId().equals(requesterUserId)) {
            throw new UnauthorizedActionException(
                    "Only the group leader can add members"
            );
        }

        if (groupMemberRepository.existsByUserIdAndGroupId(newMemberUserId, groupId)) {
            throw new DuplicateResourceException(
                    "User is already a member of this group"
            );
        }

        User newMember = userRepository.findById(newMemberUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        GroupMember membership = new GroupMember(newMember, group);

        return groupMemberRepository.save(membership);
    }

    public TravelGroup getGroupOrThrow(Long groupId) {

        return travelGroupRepository.findById(groupId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Group not found with id: " + groupId
                        )
                );
    }

    public GroupMember getMembershipOrThrow(Long userId, Long groupId) {

        return groupMemberRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() ->
                        new UnauthorizedActionException(
                                "You are not a member of this group"
                        )
                );
    }

    public List<GroupMember> getMembers(Long groupId) {

        getGroupOrThrow(groupId);

        return groupMemberRepository.findByGroupId(groupId);
    }

    public List<GroupMember> getMyGroups(Long userId) {

        return groupMemberRepository.findByUserId(userId);
    }

    public boolean isLeader(TravelGroup group, Long userId) {

        return group.getLeader().getId().equals(userId);
    }
        /**
     * A member (not the leader) exits the group at will. The leader must use
     * terminateGroup() instead - a group can't be left leaderless.
     */
    @Transactional
    public void leaveGroup(Long groupId, Long userId) {

        TravelGroup group = getGroupOrThrow(groupId);

        if (isLeader(group, userId)) {
            throw new UnauthorizedActionException(
                    "The group leader cannot leave the group. Terminate the group instead."
            );
        }

        GroupMember membership = getMembershipOrThrow(userId, groupId);

        safetyAlertRepository.deleteByGroupMemberId(membership.getId());
        locationUpdateRepository.deleteByGroupMemberId(membership.getId());
        groupMemberRepository.delete(membership);
    }

    /**
     * Leader deletes the entire group. Cleans up all dependent rows first
     * (location history, safety alerts, memberships) before removing the
     * group itself, to satisfy foreign key constraints.
     */
    @Transactional
    public void terminateGroup(Long groupId, Long requesterUserId) {

        TravelGroup group = getGroupOrThrow(groupId);

        if (!isLeader(group, requesterUserId)) {
            throw new UnauthorizedActionException(
                    "Only the group leader can terminate the group"
            );
        }

        List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
        List<Long> memberIds = members.stream().map(GroupMember::getId).toList();

        safetyAlertRepository.deleteByGroupId(groupId);
        if (!memberIds.isEmpty()) {
            locationUpdateRepository.deleteByGroupMemberIdIn(memberIds);
        }
        groupMemberRepository.deleteAll(members);
        travelGroupRepository.delete(group);
    }
}