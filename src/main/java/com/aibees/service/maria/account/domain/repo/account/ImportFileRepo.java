package com.aibees.service.maria.account.domain.repo.account;

import com.aibees.service.maria.account.domain.entity.account.AccountImportFile;
import com.aibees.service.maria.account.domain.entity.account.pk.AccountImportFileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportFileRepo extends JpaRepository<AccountImportFile, AccountImportFileId> {

    List<AccountImportFile> findAllByFileType(String fileType);
}
