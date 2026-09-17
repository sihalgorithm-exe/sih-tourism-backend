package com.sih.tourism.repository;

import com.sih.tourism.entity.TravelEstimate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelEstimateRepository extends JpaRepository<TravelEstimate, Long> {
}