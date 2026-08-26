package com.sih.tourism.dto.response;

import java.time.LocalDateTime;

public class SafetyAlertResponse {

    private Long alertId;
    private Long groupMemberId;
    private Long userId;
    private String userName;
    private Double distanceMeters;
    private LocalDateTime triggeredAt;

    public SafetyAlertResponse(Long alertId, Long groupMemberId, Long userId, String userName,
                                Double distanceMeters, LocalDateTime triggeredAt) {
        this.alertId = alertId;
        this.groupMemberId = groupMemberId;
        this.userId = userId;
        this.userName = userName;
        this.distanceMeters = distanceMeters;
        this.triggeredAt = triggeredAt;
    }

    public Long getAlertId() {
        return alertId;
    }

    public Long getGroupMemberId() {
        return groupMemberId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public Double getDistanceMeters() {
        return distanceMeters;
    }

    public LocalDateTime getTriggeredAt() {
        return triggeredAt;
    }
}
