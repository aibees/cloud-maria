package com.aibees.service.maria.account.domain.repo.system;

import com.aibees.service.maria.account.domain.entity.system.Category;
import com.aibees.service.maria.account.domain.entity.system.pk.CategoryPk;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemCategoryRepo extends JpaRepository<Category, CategoryPk> {
    List<Category> findAllBySourceCd(String sourceCd);
}
