package com.aibees.service.maria.common.domain.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aibees.service.maria.common.domain.entity.UserAuth;
import com.aibees.service.maria.common.domain.entity.pk.UserAuthPk;

@Repository
public interface UserAuthRepo extends JpaRepository<UserAuth, UserAuthPk> {
    List<UserAuth> findAllByUuid(Long uuid);

}
