package com.aibees.service.maria.common.domain.repo;


import com.aibees.service.maria.common.domain.entity.UserEncryption;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEncryptionRepo extends JpaRepository<UserEncryption, Long> {

}
