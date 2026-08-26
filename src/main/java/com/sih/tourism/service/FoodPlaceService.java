package com.sih.tourism.service;

import com.sih.tourism.entity.FoodPlace;
import com.sih.tourism.exception.ResourceNotFoundException;
import com.sih.tourism.repository.FoodPlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodPlaceService {

    private final FoodPlaceRepository foodPlaceRepository;

    @Autowired
    public FoodPlaceService(FoodPlaceRepository foodPlaceRepository) {
        this.foodPlaceRepository = foodPlaceRepository;
    }

    public List<FoodPlace> getAll() {
        return foodPlaceRepository.findAll();
    }

    public FoodPlace getById(Long id) {
        return foodPlaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Food place not found with id: " + id));
    }
}
