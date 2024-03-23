package com.aibees.service.maria.account.domain.repo.bank;

import com.aibees.service.maria.account.domain.entity.BankInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankInfoRepo extends JpaRepository<BankInfo, String> {


}
