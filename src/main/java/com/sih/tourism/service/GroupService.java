package com.sih.tourism.service;

import com.sih.tourism.dto.request.CreateGroupRequest;
import com.sih.tourism.entity.GroupMember;
import com.sih.tourism.entity.TravelGroup;
import com.sih.tourism.entity.User;
import com.sih.tourism.exception.DuplicateResourceException;
import com.sih.tourism.exception.ResourceNotFoundException;
import com.sih.tourism.exception.UnauthorizedActionException;
import com.sih.tourism.repository.GroupMemberRepository;
import com.sih.tourism.repository.TravelGroupRepository;
import com.sih.tourism.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    private final TravelGroupRepository travelGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;

    @Autowired
    public GroupService(TravelGroupRepository travelGroupRepository,
                         GroupMemberRepository groupMemberRepository,
                         UserRepository userRepository) {
        this.travelGroupRepository = travelGroupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.userRepository = userRepository;
    }

    public TravelGroup createGroup(Long creatorUserId, CreateGroupRequest request) {
        User creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TravelGroup group = new TravelGroup(request.getName(), creator, request.getRadiusMeters());
        TravelGroup savedGroup = travelGroupRepository.save(group);

        // The creator is automatically a member (and is treated as leader via group.leaderId).
        GroupMember leaderMembership = new GroupMember(creator, savedGroup);
        groupMemberRepository.save(leaderMembership);

        return savedGroup;
    }

    public GroupMember addMember(Long groupId, Long requesterUserId, Long newMemberUserId) {
        TravelGroup group = getGroupOrThrow(groupId);

        // Only the leader can add members - enforced server-side, never trust the frontend.
        if (!group.getLeader().getId().equals(requesterUserId)) {
            throw new UnauthorizedActionException("Only the group leader can add members");
        }

        if (groupMemberRepository.existsByUserIdAndGroupId(newMemberUserId, groupId)) {
            throw new DuplicateResourceException("User is already a member of this group");
        }

        User newMember = userRepository.findById(newMemberUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        GroupMember membership = new GroupMember(newMember, group);
        return groupMemberRepository.save(membership);
    }

    public TravelGroup getGroupOrThrow(Long groupId) {
        return travelGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found with id: " + groupId));
    }

    public GroupMember getMembershipOrThrow(Long userId, Long groupId) {
        return groupMemberRepository.findByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new UnauthorizedActionException("You are not a member of this group"));
    }

    public boolean isLeader(TravelGroup group, Long userId) {
        return group.getLeader().getId().equals(userId);
    }
}
