package com.sih.tourism.controller;

import com.sih.tourism.entity.FoodPlace;
import com.sih.tourism.service.FoodPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {

    private final FoodPlaceService foodPlaceService;

    @Autowired
    public FoodController(FoodPlaceService foodPlaceService) {
        this.foodPlaceService = foodPlaceService;
    }

    @GetMapping
    public List<FoodPlace> getAll() {
        return foodPlaceService.getAll();
    }

    @GetMapping("/{id}")
    public FoodPlace getById(@PathVariable Long id) {
        return foodPlaceService.getById(id);
    }
}
