package com.aibees.service.maria.accountbook.repository;

import com.aibees.service.maria.accountbook.entity.vo.BankStatement;
import com.aibees.service.maria.accountbook.entity.vo.pk.BankStatementId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankStatementRepo extends JpaRepository<BankStatement, BankStatementId> {

    List<BankStatement> findAllByBankId(String bankId);
}
