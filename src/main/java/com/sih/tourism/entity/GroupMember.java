package com.sih.tourism.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "group_members", uniqueConstraints = {
        // A user can only join a given group once.
        @UniqueConstraint(columnNames = {"user_id", "group_id"})
})
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private TravelGroup group;

        @Column(name = "is_out_of_bounds", nullable = false)
    private boolean outOfBounds = false;

    // Reuses the outOfBounds flag above for detection; this field only tracks
    // the member's response to the "Are you lost?" prompt that follows it.
    // OK -> PENDING_RESPONSE -> (SAFE | LOST | NEEDS_HELP | NO_RESPONSE) -> OK
    @Column(name = "guard_status", nullable = false)
    private String guardStatus = "OK";

    // When guardStatus last changed to PENDING_RESPONSE - used by the
    // escalation sweep to detect the 5-minute no-response window.
    @Column(name = "status_updated_at")
    private LocalDateTime statusUpdatedAt;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    public String getGuardStatus() {
        return guardStatus;
    }

    public void setGuardStatus(String guardStatus) {
        this.guardStatus = guardStatus;
    }

    public LocalDateTime getStatusUpdatedAt() {
        return statusUpdatedAt;
    }

    public void setStatusUpdatedAt(LocalDateTime statusUpdatedAt) {
        this.statusUpdatedAt = statusUpdatedAt;
    }

    public GroupMember() {
    }

    public GroupMember(User user, TravelGroup group) {
        this.user = user;
        this.group = group;
    }

    @PrePersist
    protected void onCreate() {
        this.joinedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TravelGroup getGroup() {
        return group;
    }

    public void setGroup(TravelGroup group) {
        this.group = group;
    }

    public boolean isOutOfBounds() {
        return outOfBounds;
    }

    public void setOutOfBounds(boolean outOfBounds) {
        this.outOfBounds = outOfBounds;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
}
