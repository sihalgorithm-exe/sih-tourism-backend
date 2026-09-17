package com.sih.tourism.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

/**
 * Curated, approximate intercity/local transport cost estimate, used by the
 * Trip Feasibility Checker's budget layer. NOT a live pricing feed -
 * min/max are researched ranges, refreshed manually (see lastUpdated).
 * A row where fromCity == toCity represents local transport WITHIN that
 * city (auto/cab between destinations in the same place).
 */
@Entity
@Table(name = "travel_estimates")
public class TravelEstimate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromCity;
    private String toCity;
    private String transportMode; // bus | train | cab | local | flight

    private Double minCost;
    private Double maxCost;
    private Double approximateDurationHours;

    private String availability;
    private String source;
    private LocalDate lastUpdated;

    public TravelEstimate() {
    }

    public Long getId() { return id; }

    public String getFromCity() { return fromCity; }
    public void setFromCity(String fromCity) { this.fromCity = fromCity; }

    public String getToCity() { return toCity; }
    public void setToCity(String toCity) { this.toCity = toCity; }

    public String getTransportMode() { return transportMode; }
    public void setTransportMode(String transportMode) { this.transportMode = transportMode; }

    public Double getMinCost() { return minCost; }
    public void setMinCost(Double minCost) { this.minCost = minCost; }

    public Double getMaxCost() { return maxCost; }
    public void setMaxCost(Double maxCost) { this.maxCost = maxCost; }

    public Double getApproximateDurationHours() { return approximateDurationHours; }
    public void setApproximateDurationHours(Double h) { this.approximateDurationHours = h; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public LocalDate getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDate lastUpdated) { this.lastUpdated = lastUpdated; }
}