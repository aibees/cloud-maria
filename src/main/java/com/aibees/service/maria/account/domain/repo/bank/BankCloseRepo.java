package com.aibees.service.maria.account.domain.repo.bank;

import com.aibees.service.maria.account.domain.entity.BankCloseStatement;
import com.aibees.service.maria.account.domain.entity.pk.BankCloseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankCloseRepo extends JpaRepository<BankCloseStatement, BankCloseId> {


}
