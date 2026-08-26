package com.sih.tourism.dto.response;

public class GroupResponse {

    private Long groupId;
    private String name;
    private Long leaderId;
    private String leaderName;
    private Double radiusMeters;

    public GroupResponse(Long groupId, String name, Long leaderId, String leaderName, Double radiusMeters) {
        this.groupId = groupId;
        this.name = name;
        this.leaderId = leaderId;
        this.leaderName = leaderName;
        this.radiusMeters = radiusMeters;
    }

    public Long getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    public Long getLeaderId() {
        return leaderId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public Double getRadiusMeters() {
        return radiusMeters;
    }
}
