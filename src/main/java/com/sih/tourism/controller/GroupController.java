package com.sih.tourism.controller;

import com.sih.tourism.dto.request.AddMemberRequest;
import com.sih.tourism.dto.request.CreateGroupRequest;
import com.sih.tourism.dto.response.GroupResponse;
import com.sih.tourism.entity.GroupMember;
import com.sih.tourism.entity.TravelGroup;
import com.sih.tourism.security.SecurityUtil;
import com.sih.tourism.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@Valid @RequestBody CreateGroupRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        TravelGroup group = groupService.createGroup(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(group));
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<Void> addMember(@PathVariable Long groupId, @Valid @RequestBody AddMemberRequest request) {
        Long requesterId = SecurityUtil.getCurrentUserId();
        GroupMember member = groupService.addMember(groupId, requesterId, request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{groupId}")
    public GroupResponse getGroup(@PathVariable Long groupId) {
        TravelGroup group = groupService.getGroupOrThrow(groupId);
        return toResponse(group);
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
