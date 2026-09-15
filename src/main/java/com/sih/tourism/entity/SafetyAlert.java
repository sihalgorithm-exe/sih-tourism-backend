package com.sih.tourism.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "safety_alerts", indexes = {
        @Index(name = "idx_alert_group_time", columnList = "group_id, triggered_at")
})
public class SafetyAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_member_id", nullable = false)
    private GroupMember groupMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private TravelGroup group;

        @Column(name = "distance_meters", nullable = false)
    private Double distanceMeters;

    // OUT_OF_RANGE (existing/default) | LOST | NEEDS_HELP | NO_RESPONSE
    @Column(name = "alert_type", nullable = false)
    private String alertType = "OUT_OF_RANGE";

    @Column(name = "triggered_at", nullable = false, updatable = false)
    private LocalDateTime triggeredAt;

    public SafetyAlert() {
    }

    public SafetyAlert(GroupMember groupMember, TravelGroup group, Double distanceMeters) {
        this.groupMember = groupMember;
        this.group = group;
        this.distanceMeters = distanceMeters;
    }

    public SafetyAlert(GroupMember groupMember, TravelGroup group, Double distanceMeters, String alertType) {
        this.groupMember = groupMember;
        this.group = group;
        this.distanceMeters = distanceMeters;
        this.alertType = alertType;
    }

    public String getAlertType() {
        return alertType;
    }

    @PrePersist
    protected void onCreate() {
        this.triggeredAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public GroupMember getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(GroupMember groupMember) {
        this.groupMember = groupMember;
    }

    public TravelGroup getGroup() {
        return group;
    }

    public void setGroup(TravelGroup group) {
        this.group = group;
    }

    public Double getDistanceMeters() {
        return distanceMeters;
    }

    public void setDistanceMeters(Double distanceMeters) {
        this.distanceMeters = distanceMeters;
    }

    public LocalDateTime getTriggeredAt() {
        return triggeredAt;
    }
}
