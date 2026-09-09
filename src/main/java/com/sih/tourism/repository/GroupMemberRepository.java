package com.sih.tourism.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sih.tourism.entity.GroupMember;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {

    List<GroupMember> findByGroupId(Long groupId);

    List<GroupMember> findByUserId(Long userId);

    Optional<GroupMember> findByUserIdAndGroupId(Long userId, Long groupId);

    boolean existsByUserIdAndGroupId(Long userId, Long groupId);
}