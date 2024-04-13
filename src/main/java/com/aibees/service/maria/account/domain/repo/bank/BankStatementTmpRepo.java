package com.aibees.service.maria.account.domain.repo.bank;

import com.aibees.service.maria.account.domain.entity.bank.BankStatementTmp;
import com.aibees.service.maria.account.domain.entity.bank.pk.BankStatementTmpId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankStatementTmpRepo extends JpaRepository<BankStatementTmp, BankStatementTmpId> {

    List<BankStatementTmp> findAllByFileHash(String fileHash);
}
