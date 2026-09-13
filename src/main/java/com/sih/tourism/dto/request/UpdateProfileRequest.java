package com.sih.tourism.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Only name is editable for now. Email is intentionally left out here -
 * if email verification gets added later, changing email should re-trigger
 * verification rather than silently updating it through this endpoint.
 */
public class UpdateProfileRequest {

    @NotBlank(message = "Name is required")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}