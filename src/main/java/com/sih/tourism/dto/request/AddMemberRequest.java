package com.sih.tourism.dto.request;

import jakarta.validation.constraints.NotNull;

public class AddMemberRequest {

    @NotNull(message = "userId is required")
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
