package com.sih.tourism.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateGroupRequest {

    @NotBlank(message = "Group name is required")
    private String name;

    @NotNull(message = "Radius is required")
    @Positive(message = "Radius must be positive")
    private Double radiusMeters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRadiusMeters() {
        return radiusMeters;
    }

    public void setRadiusMeters(Double radiusMeters) {
        this.radiusMeters = radiusMeters;
    }
}
