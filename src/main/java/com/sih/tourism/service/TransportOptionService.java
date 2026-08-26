package com.sih.tourism.service;

import com.sih.tourism.entity.TransportOption;
import com.sih.tourism.exception.ResourceNotFoundException;
import com.sih.tourism.repository.TransportOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransportOptionService {

    private final TransportOptionRepository transportOptionRepository;

    @Autowired
    public TransportOptionService(TransportOptionRepository transportOptionRepository) {
        this.transportOptionRepository = transportOptionRepository;
    }

    public List<TransportOption> getAll() {
        return transportOptionRepository.findAll();
    }

    public TransportOption getById(Long id) {
        return transportOptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transport option not found with id: " + id));
    }
}
