package com.sih.tourism.controller;

import com.sih.tourism.dto.request.LocationUpdateRequest;
import com.sih.tourism.dto.response.SafetyAlertResponse;
import com.sih.tourism.entity.LocationUpdate;
import com.sih.tourism.entity.SafetyAlert;
import com.sih.tourism.security.SecurityUtil;
import com.sih.tourism.service.GroupGuardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                        alert.getTriggeredAt()
                ))
                .toList();
    }
}
