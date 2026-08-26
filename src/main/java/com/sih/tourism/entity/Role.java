package com.sih.tourism.entity;

/**
 * Global application roles.
 *
 * Note: GROUP_LEADER is intentionally NOT here. Group leadership is
 * relative to a specific TravelGroup (see TravelGroup.leaderId), not
 * a global property of a user - a user can lead one group and be a
 * plain member of another at the same time.
 */
public enum Role {
    TOURIST,
    ADMIN
}
