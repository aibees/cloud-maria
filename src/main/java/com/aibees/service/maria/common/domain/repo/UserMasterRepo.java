package com.aibees.service.maria.common.domain.repo;

import com.aibees.service.maria.common.domain.entity.UserMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMasterRepo extends JpaRepository<UserMaster, Long> {
    Optional<UserMaster> findByEmailAndEmailPostfix(String email, String emailPostfix);
}
