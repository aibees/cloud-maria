package com.aibees.service.maria.common.domain.repo;

import com.aibees.service.maria.common.domain.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailRepo extends JpaRepository<UserDetail, Long> {

}
