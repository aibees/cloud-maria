package com.aibees.service.maria.account.domain.repo.bank;

import com.aibees.service.maria.account.domain.dto.bank.BankStatementReq;
import com.aibees.service.maria.account.domain.entity.bank.BankStatement;
import com.aibees.service.maria.account.domain.entity.bank.QBankStatement;
import com.aibees.service.maria.account.domain.entity.bank.pk.BankStatementId;
import com.aibees.service.maria.common.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface BankStatementRepo extends JpaRepository<BankStatement, BankStatementId>, BankStatementCustom {

}

interface BankStatementCustom {
    List<BankStatement> getBankStatementListByCondition(BankStatementReq param);
}

@AllArgsConstructor
class BankStatementCustomImpl implements BankStatementCustom {

    private final JPAQueryFactory query;
    private final QBankStatement qBankStatement;

    @Override
    public List<BankStatement> getBankStatementListByCondition(BankStatementReq param) {

        return query.select(qBankStatement)
                .from(qBankStatement)
                .where(productPredicate(param))
                .fetch();
    }

    private BooleanBuilder productPredicate(BankStatementReq param) {
        BooleanBuilder whereBuilder = new BooleanBuilder();

        if(StringUtils.isNotNull(param.getYmdFrom()) &&
            StringUtils.isNotNull(param.getYmdTo())) {
            whereBuilder.and(qBankStatement.ymd.between(param.getYmdFrom(), param.getYmdTo()));
        }

        if(Objects.isNull(param.getAmountFrom()) &&
                Objects.isNull(param.getAmountTo())) {
            whereBuilder.and(qBankStatement.ymd.between(param.getYmdFrom(), param.getYmdTo()));
        }

        if(StringUtils.isNotNull(param.getRemark())) {
            whereBuilder.and(qBankStatement.remark.contains(param.getRemark()));
        }

        if(StringUtils.isNotNull(param.getUsageCd())) {
            whereBuilder.and(qBankStatement.usageCd.eq(param.getUsageCd()));
        }

        if(StringUtils.isNotNull(param.getEntryCd())) {
            whereBuilder.and(qBankStatement.entryCd.eq(param.getEntryCd()));
        }

        if(StringUtils.isNotNull(param.getBankId())) {
            whereBuilder.and(qBankStatement.bankId.eq(param.getBankId()));
        }

        return whereBuilder;
    }
}