package com.sih.tourism.repository;

import com.sih.tourism.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Used during login (lookup by email) and registration (check for duplicates).
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
