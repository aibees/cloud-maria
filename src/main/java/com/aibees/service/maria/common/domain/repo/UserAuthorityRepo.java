package com.aibees.service.maria.common.domain.repo;

import com.aibees.service.maria.common.domain.entity.UserAuthority;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthorityRepo extends JpaRepository<UserAuthority, String> {
    Optional<UserAuthority> findById(String key);
}
