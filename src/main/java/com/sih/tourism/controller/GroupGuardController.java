package com.sih.tourism.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sih.tourism.dto.request.LocationUpdateRequest;
import com.sih.tourism.dto.response.SafetyAlertResponse;
import com.sih.tourism.entity.LocationUpdate;
import com.sih.tourism.entity.SafetyAlert;
import com.sih.tourism.security.SecurityUtil;
import com.sih.tourism.service.GroupGuardService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/groups/{groupId}")
public class GroupGuardController {

    private final GroupGuardService groupGuardService;

    @Autowired
    public GroupGuardController(GroupGuardService groupGuardService) {
        this.groupGuardService = groupGuardService;
    }

    // Used by both the leader (establishes the reference point) and regular
    // members (triggers the exit-event distance check against the leader).
    @PostMapping("/locations")
    public ResponseEntity<Void> submitLocation(@PathVariable Long groupId,
                                                @Valid @RequestBody LocationUpdateRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        LocationUpdate update = groupGuardService.submitLocation(groupId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

        @GetMapping("/my-status")
    public com.sih.tourism.dto.response.MyGuardStatusResponse getMyStatus(@PathVariable Long groupId) {
        Long userId = SecurityUtil.getCurrentUserId();
        var member = groupGuardService.getMyMembership(groupId, userId);
        var myLocation = groupGuardService.getLatestLocation(member.getId());

        var leaderMember = com.sih.tourism.security.SecurityUtil.getCurrentUserId() != null
                ? member.getGroup().getLeader() : null;
        var leaderMembership = groupGuardService.getMyMembership(groupId, member.getGroup().getLeader().getId());
        var leaderLocation = groupGuardService.getLatestLocation(leaderMembership.getId());

        return new com.sih.tourism.dto.response.MyGuardStatusResponse(
                member.getGuardStatus(),
                myLocation.map(com.sih.tourism.entity.LocationUpdate::getLatitude).orElse(null),
                myLocation.map(com.sih.tourism.entity.LocationUpdate::getLongitude).orElse(null),
                leaderLocation.map(com.sih.tourism.entity.LocationUpdate::getLatitude).orElse(null),
                leaderLocation.map(com.sih.tourism.entity.LocationUpdate::getLongitude).orElse(null)
        );
    }

    @PostMapping("/guard-response")
    public ResponseEntity<Void> submitGuardResponse(@PathVariable Long groupId,
                                                      @Valid @RequestBody com.sih.tourism.dto.request.GuardResponseRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        groupGuardService.respondToGuardPrompt(groupId, userId, request.getResponse());
        return ResponseEntity.ok().build();
    }

    // Leader-only, enforced in the service layer via group.leaderId comparison.
    @GetMapping("/alerts")
    public List<SafetyAlertResponse> getAlerts(@PathVariable Long groupId) {
        Long userId = SecurityUtil.getCurrentUserId();
        List<SafetyAlert> alerts = groupGuardService.getAlertsForGroup(groupId, userId);

                return alerts.stream()
                .map(alert -> new SafetyAlertResponse(
                        alert.getId(),
                        alert.getGroupMember().getId(),
                        alert.getGroupMember().getUser().getId(),
                        alert.getGroupMember().getUser().getName(),
                        alert.getDistanceMeters(),
                        alert.getTriggeredAt(),
                        alert.getAlertType()
                ))
                .toList();
    }
}
