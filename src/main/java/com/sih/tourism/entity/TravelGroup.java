package com.sih.tourism.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "travel_groups")
public class TravelGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Whoever created the group. Leadership is per-group, not a global User role.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id", nullable = false)
    private User leader;

    // Configured at group creation; the reference POINT is the leader's live location,
    // not a fixed lat/lng stored here.
    @Column(name = "radius_meters", nullable = false)
    private Double radiusMeters;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public TravelGroup() {
    }

    public TravelGroup(String name, User leader, Double radiusMeters) {
        this.name = name;
        this.leader = leader;
        this.radiusMeters = radiusMeters;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public Double getRadiusMeters() {
        return radiusMeters;
    }

    public void setRadiusMeters(Double radiusMeters) {
        this.radiusMeters = radiusMeters;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
