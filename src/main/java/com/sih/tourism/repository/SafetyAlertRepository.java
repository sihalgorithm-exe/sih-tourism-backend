package com.sih.tourism.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sih.tourism.entity.SafetyAlert;

public interface SafetyAlertRepository extends JpaRepository<SafetyAlert, Long> {
    List<SafetyAlert> findByGroupIdOrderByTriggeredAtDesc(Long groupId);
    void deleteByGroupMemberId(Long groupMemberId);
    void deleteByGroupId(Long groupId);
}
