package com.aibees.service.maria.account.domain.repo.account;


import com.aibees.service.maria.account.domain.entity.account.AccountImportFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportFileRepo extends JpaRepository<AccountImportFile, String> {

}
