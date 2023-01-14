package com.aibees.service.maria.multipart.domain.repo;

import com.aibees.service.maria.multipart.domain.entity.MetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataRepository extends JpaRepository<MetadataEntity, String> {

    MetadataEntity findByFileid(String fileid);

    @Query(value="SELECT MAX(fm.fileid) AS fileid FROM file_metadata fm WHERE fm.fileid LIKE CONCAT(?1, '%')", nativeQuery=true)
    String findMetadataByFilename(String name);
}
