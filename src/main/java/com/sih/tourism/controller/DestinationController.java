package com.sih.tourism.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sih.tourism.entity.Destination;
import com.sih.tourism.service.DestinationService;

@RestController
@RequestMapping("/api/destinations")
public class DestinationController {

    private final DestinationService destinationService;

    @Autowired
    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }

    @GetMapping
    public List<Destination> getAll() {
        return destinationService.getAll();
    }

    @GetMapping("/{id}")
    public Destination getById(@PathVariable Long id) {
        return destinationService.getById(id);
    }

        @GetMapping("/states")
    public List<String> getStates() {
        return destinationService.getAllStates();
    }

    @GetMapping("/cities")
    public List<String> getCities(@RequestParam String state) {
        return destinationService.getCitiesByState(state);
    }
}
