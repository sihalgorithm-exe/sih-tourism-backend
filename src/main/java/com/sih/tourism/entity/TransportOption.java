package com.sih.tourism.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transport_options")
public class TransportOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // e.g. "City Bus", "Auto Rickshaw Stand", "Local Ferry"

    private String type; // e.g. "bus", "auto", "ferry", "metro"

    private String city;

    private String state; 

    private String address;

    private Double latitude;

    private Double longitude;

    private int contact;

    private String nearbyAttributes;

    private String imgUrl;

    public TransportOption() {
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

    public String getState(){
        return state;
    }

    public void setState(String state){
        this.state = state;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
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

    public int getContact(){
        return contact;
    }

    public void setContact(int contact){
        this.contact = contact;
    }

    public String getAttribute(){
        return nearbyAttributes;
    }

    public void setAttribute(String nearbyAttributes){
        this.nearbyAttributes = nearbyAttributes;
    }

    public String getImgUrl(){
        return imgUrl;
    }

    public void setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }
}
