package com.sih.tourism.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class GuardResponseRequest {

    @NotBlank
    @Pattern(regexp = "SAFE|LOST|NEEDS_HELP", message = "response must be SAFE, LOST, or NEEDS_HELP")
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}