package com.sih.tourism.repository;

import com.sih.tourism.entity.SafetyAlert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SafetyAlertRepository extends JpaRepository<SafetyAlert, Long> {
    List<SafetyAlert> findByGroupIdOrderByTriggeredAtDesc(Long groupId);
}
