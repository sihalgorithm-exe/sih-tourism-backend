package com.sih.tourism.controller;

import com.sih.tourism.entity.ShoppingPlace;
import com.sih.tourism.service.ShoppingPlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shopping")
public class ShoppingController {

    private final ShoppingPlaceService shoppingPlaceService;

    @Autowired
    public ShoppingController(ShoppingPlaceService shoppingPlaceService) {
        this.shoppingPlaceService = shoppingPlaceService;
    }

    @GetMapping
    public List<ShoppingPlace> getAll() {
        return shoppingPlaceService.getAll();
    }

    @GetMapping("/{id}")
    public ShoppingPlace getById(@PathVariable Long id) {
        return shoppingPlaceService.getById(id);
    }
}
