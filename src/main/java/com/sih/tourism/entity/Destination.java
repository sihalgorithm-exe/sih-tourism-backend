package com.sih.tourism.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "destinations")
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String city;

    private String category; // e.g. "Heritage & Monuments", "Nature & Parks"

    private Double latitude;

    private Double longitude;

    private String imgUrl;

    // --- Legacy curated fields -----------------------------------------
    // NOT populated by the CSV import (the new dataset has no clean numeric
    // equivalent for either). Kept nullable so existing consumers keep
    // compiling; fill in manually per-destination later if you want them
    // to actually drive ranking/estimates again.
    private Double popularityScore;

    private Double visitDurationHours;

    // --- New fields from destinations.csv --------------------------------
    @Column(name = "short_description", columnDefinition = "TEXT")
    private String shortDescription;

    @Column(name = "detailed_description", columnDefinition = "TEXT")
    private String detailedDescription;

    @Column(name = "district")
    private String district;

    @Column(name = "state")
    private String state;

    @Column(name = "subcategory")
    private String subcategory;

    @Column(name = "interests", columnDefinition = "TEXT")
    private String interests;

    @Column(name = "mood_tags", columnDefinition = "TEXT")
    private String moodTags;

    @Column(name = "experience_types", columnDefinition = "TEXT")
    private String experienceTypes;

    @Column(name = "suitable_for", columnDefinition = "TEXT")
    private String suitableFor;

    @Column(name = "family_friendly")
    private String familyFriendly; // free text, e.g. "Yes", "Moderate (limited facilities)"

    @Column(name = "child_friendly")
    private String childFriendly; // free text, not boolean - values like "Moderate (steep climb)"

    @Column(name = "accessibility", columnDefinition = "TEXT")
    private String accessibility;

    @Column(name = "crowd_level")
    private String crowdLevel;

    @Column(name = "hidden_gem")
    private String hiddenGem; // free text ("Yes" / "No" / "No (increasingly popular)")

    @Column(name = "popularity_level")
    private String popularityLevel;

    @Column(name = "recommended_visit_duration")
    private String recommendedVisitDuration;

    @Column(name = "opening_time")
    private String openingTime;

    @Column(name = "closing_time")
    private String closingTime;

    @Column(name = "closed_days")
    private String closedDays;

    @Column(name = "best_time_of_day")
    private String bestTimeOfDay;

    @Column(name = "best_season")
    private String bestSeason;

    @Column(name = "weather_considerations", columnDefinition = "TEXT")
    private String weatherConsiderations;

    @Column(name = "nearby_attractions", columnDefinition = "TEXT")
    private String nearbyAttractions;

    @Column(name = "recommended_activities", columnDefinition = "TEXT")
    private String recommendedActivities;

    @Column(name = "entry_fee")
    private String entryFee;

    @Column(name = "parking_fee")
    private String parkingFee;

    @Column(name = "avg_local_transport_cost")
    private String avgLocalTransportCost;

    @Column(name = "avg_food_cost")
    private String avgFoodCost;

    @Column(name = "estimated_visit_cost")
    private String estimatedVisitCost;

    @Column(name = "cost_level")
    private String costLevel; // free text, e.g. "Mid-range (mainly due to distance/transport)"

    @Column(name = "nearest_railway_station")
    private String nearestRailwayStation;

    @Column(name = "nearest_airport")
    private String nearestAirport;

    @Column(name = "nearest_bus_station")
    private String nearestBusStation;

    @Column(name = "available_transport_modes", columnDefinition = "TEXT")
    private String availableTransportModes;

    @Column(name = "local_transport_options", columnDefinition = "TEXT")
    private String localTransportOptions;

    @Column(name = "road_accessibility", columnDefinition = "TEXT")
    private String roadAccessibility;

    @Column(name = "local_culture", columnDefinition = "TEXT")
    private String localCulture;

    @Column(name = "local_cuisine", columnDefinition = "TEXT")
    private String localCuisine;

    @Column(name = "local_crafts", columnDefinition = "TEXT")
    private String localCrafts;

    @Column(name = "local_experiences", columnDefinition = "TEXT")
    private String localExperiences;

    @Column(name = "supports_local_businesses")
    private String supportsLocalBusinesses;

    @Column(name = "tourism_potential")
    private String tourismPotential;

    @Column(name = "reason_for_tourism_promotion", columnDefinition = "TEXT")
    private String reasonForTourismPromotion;

    @Column(name = "safety_notes", columnDefinition = "TEXT")
    private String safetyNotes;

    @Column(name = "family_child_safety_notes", columnDefinition = "TEXT")
    private String familyChildSafetyNotes;

    @Column(name = "emergency_info", columnDefinition = "TEXT")
    private String emergencyInfo;

    @Column(name = "special_restrictions", columnDefinition = "TEXT")
    private String specialRestrictions;

    public Destination() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Double getPopularityScore() {
        return popularityScore;
    }

    public void setPopularityScore(Double popularityScore) {
        this.popularityScore = popularityScore;
    }

    public Double getVisitDurationHours() {
        return visitDurationHours;
    }

    public void setVisitDurationHours(Double visitDurationHours) {
        this.visitDurationHours = visitDurationHours;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getMoodTags() {
        return moodTags;
    }

    public void setMoodTags(String moodTags) {
        this.moodTags = moodTags;
    }

    public String getExperienceTypes() {
        return experienceTypes;
    }

    public void setExperienceTypes(String experienceTypes) {
        this.experienceTypes = experienceTypes;
    }

    public String getSuitableFor() {
        return suitableFor;
    }

    public void setSuitableFor(String suitableFor) {
        this.suitableFor = suitableFor;
    }

    public String getFamilyFriendly() {
        return familyFriendly;
    }

    public void setFamilyFriendly(String familyFriendly) {
        this.familyFriendly = familyFriendly;
    }

    public String getChildFriendly() {
        return childFriendly;
    }

    public void setChildFriendly(String childFriendly) {
        this.childFriendly = childFriendly;
    }

    public String getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(String accessibility) {
        this.accessibility = accessibility;
    }

    public String getCrowdLevel() {
        return crowdLevel;
    }

    public void setCrowdLevel(String crowdLevel) {
        this.crowdLevel = crowdLevel;
    }

    public String getHiddenGem() {
        return hiddenGem;
    }

    public void setHiddenGem(String hiddenGem) {
        this.hiddenGem = hiddenGem;
    }

    public String getPopularityLevel() {
        return popularityLevel;
    }

    public void setPopularityLevel(String popularityLevel) {
        this.popularityLevel = popularityLevel;
    }

    public String getRecommendedVisitDuration() {
        return recommendedVisitDuration;
    }

    public void setRecommendedVisitDuration(String recommendedVisitDuration) {
        this.recommendedVisitDuration = recommendedVisitDuration;
    }

    public String getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(String openingTime) {
        this.openingTime = openingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getClosedDays() {
        return closedDays;
    }

    public void setClosedDays(String closedDays) {
        this.closedDays = closedDays;
    }

    public String getBestTimeOfDay() {
        return bestTimeOfDay;
    }

    public void setBestTimeOfDay(String bestTimeOfDay) {
        this.bestTimeOfDay = bestTimeOfDay;
    }

    public String getBestSeason() {
        return bestSeason;
    }

    public void setBestSeason(String bestSeason) {
        this.bestSeason = bestSeason;
    }

    public String getWeatherConsiderations() {
        return weatherConsiderations;
    }

    public void setWeatherConsiderations(String weatherConsiderations) {
        this.weatherConsiderations = weatherConsiderations;
    }

    public String getNearbyAttractions() {
        return nearbyAttractions;
    }

    public void setNearbyAttractions(String nearbyAttractions) {
        this.nearbyAttractions = nearbyAttractions;
    }

    public String getRecommendedActivities() {
        return recommendedActivities;
    }

    public void setRecommendedActivities(String recommendedActivities) {
        this.recommendedActivities = recommendedActivities;
    }

    public String getEntryFee() {
        return entryFee;
    }

    public void setEntryFee(String entryFee) {
        this.entryFee = entryFee;
    }

    public String getParkingFee() {
        return parkingFee;
    }

    public void setParkingFee(String parkingFee) {
        this.parkingFee = parkingFee;
    }

    public String getAvgLocalTransportCost() {
        return avgLocalTransportCost;
    }

    public void setAvgLocalTransportCost(String avgLocalTransportCost) {
        this.avgLocalTransportCost = avgLocalTransportCost;
    }

    public String getAvgFoodCost() {
        return avgFoodCost;
    }

    public void setAvgFoodCost(String avgFoodCost) {
        this.avgFoodCost = avgFoodCost;
    }

    public String getEstimatedVisitCost() {
        return estimatedVisitCost;
    }

    public void setEstimatedVisitCost(String estimatedVisitCost) {
        this.estimatedVisitCost = estimatedVisitCost;
    }

    public String getCostLevel() {
        return costLevel;
    }

    public void setCostLevel(String costLevel) {
        this.costLevel = costLevel;
    }

    public String getNearestRailwayStation() {
        return nearestRailwayStation;
    }

    public void setNearestRailwayStation(String nearestRailwayStation) {
        this.nearestRailwayStation = nearestRailwayStation;
    }

    public String getNearestAirport() {
        return nearestAirport;
    }

    public void setNearestAirport(String nearestAirport) {
        this.nearestAirport = nearestAirport;
    }

    public String getNearestBusStation() {
        return nearestBusStation;
    }

    public void setNearestBusStation(String nearestBusStation) {
        this.nearestBusStation = nearestBusStation;
    }

    public String getAvailableTransportModes() {
        return availableTransportModes;
    }

    public void setAvailableTransportModes(String availableTransportModes) {
        this.availableTransportModes = availableTransportModes;
    }

    public String getLocalTransportOptions() {
        return localTransportOptions;
    }

    public void setLocalTransportOptions(String localTransportOptions) {
        this.localTransportOptions = localTransportOptions;
    }

    public String getRoadAccessibility() {
        return roadAccessibility;
    }

    public void setRoadAccessibility(String roadAccessibility) {
        this.roadAccessibility = roadAccessibility;
    }

    public String getLocalCulture() {
        return localCulture;
    }

    public void setLocalCulture(String localCulture) {
        this.localCulture = localCulture;
    }

    public String getLocalCuisine() {
        return localCuisine;
    }

    public void setLocalCuisine(String localCuisine) {
        this.localCuisine = localCuisine;
    }

    public String getLocalCrafts() {
        return localCrafts;
    }

    public void setLocalCrafts(String localCrafts) {
        this.localCrafts = localCrafts;
    }

    public String getLocalExperiences() {
        return localExperiences;
    }

    public void setLocalExperiences(String localExperiences) {
        this.localExperiences = localExperiences;
    }

    public String getSupportsLocalBusinesses() {
        return supportsLocalBusinesses;
    }

    public void setSupportsLocalBusinesses(String supportsLocalBusinesses) {
        this.supportsLocalBusinesses = supportsLocalBusinesses;
    }

    public String getTourismPotential() {
        return tourismPotential;
    }

    public void setTourismPotential(String tourismPotential) {
        this.tourismPotential = tourismPotential;
    }

    public String getReasonForTourismPromotion() {
        return reasonForTourismPromotion;
    }

    public void setReasonForTourismPromotion(String reasonForTourismPromotion) {
        this.reasonForTourismPromotion = reasonForTourismPromotion;
    }

    public String getSafetyNotes() {
        return safetyNotes;
    }

    public void setSafetyNotes(String safetyNotes) {
        this.safetyNotes = safetyNotes;
    }

    public String getFamilyChildSafetyNotes() {
        return familyChildSafetyNotes;
    }

    public void setFamilyChildSafetyNotes(String familyChildSafetyNotes) {
        this.familyChildSafetyNotes = familyChildSafetyNotes;
    }

    public String getEmergencyInfo() {
        return emergencyInfo;
    }

    public void setEmergencyInfo(String emergencyInfo) {
        this.emergencyInfo = emergencyInfo;
    }

    public String getSpecialRestrictions() {
        return specialRestrictions;
    }

    public void setSpecialRestrictions(String specialRestrictions) {
        this.specialRestrictions = specialRestrictions;
    }
}