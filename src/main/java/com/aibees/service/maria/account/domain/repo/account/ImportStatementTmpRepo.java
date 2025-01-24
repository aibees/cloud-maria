package com.aibees.service.maria.account.domain.repo.account;

import com.aibees.service.maria.account.domain.dto.bank.BankImportRes;
import com.aibees.service.maria.account.domain.entity.account.ImportStatementTmp;
import com.aibees.service.maria.account.domain.entity.account.QImportStatementTmp;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImportStatementTmpRepo extends JpaRepository<ImportStatementTmp, String>, ImportStatementTmpCustom {

    List<ImportStatementTmp> findAllByFileHash(String fileHash);
}

interface ImportStatementTmpCustom {
    List<String> getAllFileNameList();
    long updateBankTmpStatement(BankImportRes req, String bankId);
}

@AllArgsConstructor
class ImportStatementTmpCustomImpl implements ImportStatementTmpCustom {

    private final JPAQueryFactory query;
    private final QImportStatementTmp qTmp = QImportStatementTmp.importStatementTmp;

    @Override
    public List<String> getAllFileNameList() {
        return query.select(qTmp.fileHash).distinct()
                .from(qTmp)
                .fetch();
    }

    @Override
    public long updateBankTmpStatement(BankImportRes req, String bankId) {
        return query.update(qTmp)
                .set(qTmp.acctCd, req.getAcctCd())
                .set(qTmp.remark, req.getRemark())
                .set(qTmp.bankId, bankId)
                .where(qTmp.statementId.eq(req.getStatementId()))
                .execute();
    }
}