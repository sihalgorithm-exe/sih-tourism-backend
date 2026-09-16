package com.sih.tourism.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sih.tourism.entity.LocationUpdate;

public interface LocationUpdateRepository extends JpaRepository<LocationUpdate, Long> {

    // Fetch the single most recent location for a given group member.
    // Used both for "leader's current location" and "member's last known position".
    // Spring Data derives this correctly into a top-1-ordered query - no LIMIT needed in JPQL.
    Optional<LocationUpdate> findFirstByGroupMemberIdOrderByRecordedAtDesc(Long groupMemberId);

    void deleteByGroupMemberId(Long groupMemberId);

    void deleteByGroupMemberIdIn(java.util.List<Long> groupMemberIds);
}
