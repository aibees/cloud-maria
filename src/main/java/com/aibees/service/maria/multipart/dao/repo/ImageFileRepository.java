package com.aibees.service.maria.multipart.dao.repo;

import com.aibees.service.maria.multipart.dao.entity.ImageFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFileEntity, Long> {

    ImageFileEntity findByCategoryAndYmAndNumber(String category, String ym, Long number);

    List<ImageFileEntity> findByCategory(String category);

    List<ImageFileEntity> findByYm(String ym);

    List<ImageFileEntity> findByCategoryAndYm(String category, String ym);
}
