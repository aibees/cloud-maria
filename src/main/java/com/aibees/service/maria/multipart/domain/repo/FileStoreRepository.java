package com.aibees.service.maria.multipart.domain.repo;

import com.aibees.service.maria.multipart.domain.entity.CommonFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileStoreRepository extends JpaRepository<CommonFileEntity, Long> {

    CommonFileEntity findOneById(Long id);

    List<CommonFileEntity> findAllByParentId(Long parentId);

    List<CommonFileEntity> findAllByExt(String ext);

    @Query(value="SELECT max(id)+1 FROM file_store", nativeQuery = true)
    Long getMaxId();
}
