package com.sih.tourism.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "shopping_places")
public class ShoppingPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String type;

    private String city;

    @Column(name = "area_locality")
    private String areaLocality;

    @Column(length = 1000)
    private String address;

    @Column(length = 1000)
    private String famousFor;

    @Column(name = "price_range")
    private String priceRange;

    private String rating;

    private String timings;

    @Column(name = "nearby_tourist_attraction", length = 1000)
    private String nearbyTouristAttraction;

    @Column(length = 2000)
    private String notes;

    @Column(name = "img_url", length = 2000)
    private String imgUrl;

    private Double latitude; private Double longitude;
    
    
    public ShoppingPlace() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getLatitude(){
        return latitude;
    }

    public void setLatitude(Double latitude){
        this.latitude = latitude;
    }

    public Double getLongitude(){
        return longitude;
    }

    public void setLongitude(Double longitude){
        this.longitude = longitude;
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

    public String getFamousFor() {
        return famousFor;
    }

    public void setFamousFor(String famousFor) {
        this.famousFor = famousFor;
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