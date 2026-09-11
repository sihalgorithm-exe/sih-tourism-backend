package com.sih.tourism.repository;

import com.sih.tourism.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByCityIgnoreCase(String city);
}