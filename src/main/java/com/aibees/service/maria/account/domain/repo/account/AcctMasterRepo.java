package com.aibees.service.maria.account.domain.repo.account;

import com.aibees.service.maria.account.domain.entity.account.AcctMaster;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AcctMasterRepo extends JpaRepository<AcctMaster, String> {

    List<AcctMaster> findAllByAcctCdContainingOrAcctNmContaining(String acctCd, String acctNm);
}
