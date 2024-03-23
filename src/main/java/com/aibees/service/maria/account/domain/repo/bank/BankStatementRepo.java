package com.aibees.service.maria.account.domain.repo.bank;

import com.aibees.service.maria.account.domain.entity.BankStatement;
import com.aibees.service.maria.account.domain.entity.pk.BankStatementId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankStatementRepo extends JpaRepository<BankStatement, BankStatementId> {

    List<BankStatement> findAllByBankIdAndEntryCdAndUsageCdAndRemarkContainingAndAmountBetween
            (String bankId, String entryCd, String usageCd, String remark, long amountBefore, long amountAfter);
}
