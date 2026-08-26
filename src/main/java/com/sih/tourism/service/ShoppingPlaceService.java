package com.sih.tourism.service;

import com.sih.tourism.entity.ShoppingPlace;
import com.sih.tourism.exception.ResourceNotFoundException;
import com.sih.tourism.repository.ShoppingPlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingPlaceService {

    private final ShoppingPlaceRepository shoppingPlaceRepository;

    @Autowired
    public ShoppingPlaceService(ShoppingPlaceRepository shoppingPlaceRepository) {
        this.shoppingPlaceRepository = shoppingPlaceRepository;
    }

    public List<ShoppingPlace> getAll() {
        return shoppingPlaceRepository.findAll();
    }

    public ShoppingPlace getById(Long id) {
        return shoppingPlaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shopping place not found with id: " + id));
    }
}
