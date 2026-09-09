package com.sih.tourism.dto.response;

import java.time.LocalDateTime;

public class GroupMemberResponse {

    private Long membershipId;
    private Long userId;
    private String name;
    private boolean leader;

    private Double latitude;
    private Double longitude;
    private LocalDateTime lastUpdated;

    private Double distanceMeters;

    public GroupMemberResponse(
            Long membershipId,
            Long userId,
            String name,
            boolean leader,
            Double latitude,
            Double longitude,
            LocalDateTime lastUpdated,
            Double distanceMeters
    ) {
        this.membershipId = membershipId;
        this.userId = userId;
        this.name = name;
        this.leader = leader;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastUpdated = lastUpdated;
        this.distanceMeters = distanceMeters;
    }

    public Long getMembershipId() {
        return membershipId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public boolean isLeader() {
        return leader;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public Double getDistanceMeters() {
        return distanceMeters;
    }
}