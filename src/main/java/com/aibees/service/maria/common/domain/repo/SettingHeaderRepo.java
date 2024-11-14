package com.aibees.service.maria.common.domain.repo;

import com.aibees.service.maria.common.domain.entity.SettingHeader;

import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingHeaderRepo extends JpaRepository<SettingHeader, Long> {
    Optional<SettingHeader> findByMainCategoryAndSubCategoryAndCode(String mainCategory, String subCategory, String code);
}
