package com.sih.tourism.repository;

import com.sih.tourism.entity.BudgetLevel;
import com.sih.tourism.entity.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DestinationRepository extends JpaRepository<Destination, Long> {
    List<Destination> findByBudgetLevel(BudgetLevel budgetLevel);
    List<Destination> findByCategoryIgnoreCase(String category);
}
