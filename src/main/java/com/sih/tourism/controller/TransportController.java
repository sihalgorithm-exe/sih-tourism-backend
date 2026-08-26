package com.sih.tourism.controller;

import com.sih.tourism.entity.TransportOption;
import com.sih.tourism.service.TransportOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transport")
public class TransportController {

    private final TransportOptionService transportOptionService;

    @Autowired
    public TransportController(TransportOptionService transportOptionService) {
        this.transportOptionService = transportOptionService;
    }

    @GetMapping
    public List<TransportOption> getAll() {
        return transportOptionService.getAll();
    }

    @GetMapping("/{id}")
    public TransportOption getById(@PathVariable Long id) {
        return transportOptionService.getById(id);
    }
}
