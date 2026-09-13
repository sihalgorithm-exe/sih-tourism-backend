package com.sih.tourism.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sih.tourism.entity.Destination;
import com.sih.tourism.exception.ResourceNotFoundException;
import com.sih.tourism.repository.DestinationRepository;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    @Autowired
    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    public List<Destination> getAll() {
        return destinationRepository.findAll();
    }

    public Destination getById(Long id) {
        return destinationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destination not found with id: " + id));
    }

    public List<String> getAllStates() {
        return destinationRepository.findDistinctStates();
    }

    public List<String> getCitiesByState(String state) {
        return destinationRepository.findDistinctCitiesByState(state);
    }
}