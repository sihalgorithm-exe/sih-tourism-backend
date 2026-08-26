package com.sih.tourism.controller;

import com.sih.tourism.dto.request.UserPreferenceRequest;
import com.sih.tourism.entity.UserPreference;
import com.sih.tourism.security.SecurityUtil;
import com.sih.tourism.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;

    @Autowired
    public UserPreferenceController(UserPreferenceService userPreferenceService) {
        this.userPreferenceService = userPreferenceService;
    }

    // Identity comes from the JWT, never from a client-supplied userId.
    @GetMapping
    public UserPreference getMyPreferences() {
        Long userId = SecurityUtil.getCurrentUserId();
        return userPreferenceService.getForUser(userId);
    }

    @PutMapping
    public UserPreference updateMyPreferences(@RequestBody UserPreferenceRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        return userPreferenceService.upsert(userId, request);
    }
}
