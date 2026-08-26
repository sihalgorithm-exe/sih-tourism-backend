package com.sih.tourism.repository;

import com.sih.tourism.entity.LocationUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationUpdateRepository extends JpaRepository<LocationUpdate, Long> {

    // Fetch the single most recent location for a given group member.
    // Used both for "leader's current location" and "member's last known position".
    // Spring Data derives this correctly into a top-1-ordered query - no LIMIT needed in JPQL.
    Optional<LocationUpdate> findFirstByGroupMemberIdOrderByRecordedAtDesc(Long groupMemberId);
}
