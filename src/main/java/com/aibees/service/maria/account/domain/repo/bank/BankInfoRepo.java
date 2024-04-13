package com.aibees.service.maria.account.domain.repo.bank;

import com.aibees.service.maria.account.domain.entity.bank.BankInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankInfoRepo extends JpaRepository<BankInfo, String> {

    List<BankInfo> findAllByBankNmContainingAndBankAcctContainingAndUseYn(String bankNm, String bankAcct, String useYn);
    Optional<BankInfo> findByBankIdAndBankCd(String bankId, String bankCd);
}

