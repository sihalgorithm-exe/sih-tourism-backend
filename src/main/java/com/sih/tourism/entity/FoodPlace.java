package com.sih.tourism.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "food_places")
public class FoodPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_index")
    private Integer sourceIndex;

    @Column(nullable = false)
    private String name;

    private String type;

    private String cuisine;

    private String city;

    @Column(name = "area_locality")
    private String areaLocality;

    @Column(length = 1000)
    private String address;

    @Column(name = "price_range")
    private String priceRange;

    private String rating;

    @Column(name = "speciality", length = 1000)
    private String speciality;

    @Column(name = "veg_non_veg")
    private String vegNonVeg;

    private String timings;

    @Column(name = "nearby_tourist_attraction", length = 1000)
    private String nearbyTouristAttraction;

    @Column(length = 2000)
    private String notes;

    @Column(name = "img_url", length = 2000)
    private String imgUrl;

    public FoodPlace() {
    }

    public Long getId() {
        return id;
    }

    public Integer getSourceIndex() {
        return sourceIndex;
    }

    public void setSourceIndex(Integer sourceIndex) {
        this.sourceIndex = sourceIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAreaLocality() {
        return areaLocality;
    }

    public void setAreaLocality(String areaLocality) {
        this.areaLocality = areaLocality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getVegNonVeg() {
        return vegNonVeg;
    }

    public void setVegNonVeg(String vegNonVeg) {
        this.vegNonVeg = vegNonVeg;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }

    public String getNearbyTouristAttraction() {
        return nearbyTouristAttraction;
    }

    public void setNearbyTouristAttraction(String nearbyTouristAttraction) {
        this.nearbyTouristAttraction = nearbyTouristAttraction;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}