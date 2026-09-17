package com.sih.tourism.controller;

import com.sih.tourism.entity.TravelEstimate;
import com.sih.tourism.repository.TravelEstimateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The dataset is small and curated (~30 rows) so this returns everything
 * in one call - Wayfare's frontend fetches it once and embeds the relevant
 * rows into the Feasibility Checker payload, same pattern as hotels.
 */
@RestController
@RequestMapping("/api/travel-estimates")
public class TravelEstimateController {

    private final TravelEstimateRepository travelEstimateRepository;

    @Autowired
    public TravelEstimateController(TravelEstimateRepository travelEstimateRepository) {
        this.travelEstimateRepository = travelEstimateRepository;
    }

    @GetMapping
    public List<TravelEstimate> getAll() {
        return travelEstimateRepository.findAll();
    }
}