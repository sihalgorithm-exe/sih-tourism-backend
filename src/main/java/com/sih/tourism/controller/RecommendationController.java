package com.sih.tourism.controller;

import com.sih.tourism.entity.Destination;
import com.sih.tourism.security.SecurityUtil;
import com.sih.tourism.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    // No userId query param - identity comes from JWT only, so a user cannot
    // request another user's personalized recommendations by editing the URL.
    @GetMapping
    public List<Destination> getMyRecommendations() {
        Long userId = SecurityUtil.getCurrentUserId();
        return recommendationService.recommendForUser(userId);
    }
}
