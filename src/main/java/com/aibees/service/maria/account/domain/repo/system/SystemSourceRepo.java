package com.aibees.service.maria.account.domain.repo.system;

import com.aibees.service.maria.account.domain.entity.system.Source;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemSourceRepo extends JpaRepository<Source, String> {

}
