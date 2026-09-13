package com.sih.tourism.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sih.tourism.entity.Destination;

public interface DestinationRepository extends JpaRepository<Destination, Long> {

    List<Destination> findByCategoryIgnoreCase(String category);
    @Query("SELECT DISTINCT d.state FROM Destination d WHERE d.state IS NOT NULL ORDER BY d.state ASC")
    List<String> findDistinctStates();

    @Query("SELECT DISTINCT d.city FROM Destination d WHERE d.state = :state AND d.city IS NOT NULL ORDER BY d.city ASC")
    List<String> findDistinctCitiesByState(@org.springframework.data.repository.query.Param("state") String state);
}