package com.aibees.service.maria.common.domain.repo;

import com.aibees.service.maria.common.domain.entity.CommonCounter;
import com.aibees.service.maria.common.domain.entity.pk.CommonCounterPk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommonCounterRepo extends JpaRepository<CommonCounter, CommonCounterPk> {

    List<CommonCounter> findAllByDivision(String division);
}
