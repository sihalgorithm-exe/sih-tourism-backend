package com.sih.tourism.dto.response;

import java.time.LocalDateTime;

import com.sih.tourism.entity.Role;
import com.sih.tourism.entity.User;

/**
 * Safe, outward-facing view of a User - deliberately excludes passwordHash.
 */
public class UserProfileResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private LocalDateTime createdAt;

    public UserProfileResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}