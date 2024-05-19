package com.aibees.service.maria.account.domain.repo.bank;

import com.aibees.service.maria.account.domain.dto.bank.BankInfoReq;
import com.aibees.service.maria.account.domain.entity.bank.BankInfo;
import com.aibees.service.maria.account.domain.entity.bank.QBankInfo;
import com.aibees.service.maria.common.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankInfoRepo extends JpaRepository<BankInfo, String>, BankInfoCustom {

    Optional<BankInfo> findByBankIdAndBankCd(String bankId, String bankCd);
}

interface BankInfoCustom {
    List<BankInfo> getBankInfoListByCondition(BankInfoReq param);
}

@AllArgsConstructor
class BankInfoCustomImpl implements BankInfoCustom {

    private final JPAQueryFactory query;
    private static final QBankInfo qBankInfo = QBankInfo.bankInfo;

    @Override
    public List<BankInfo> getBankInfoListByCondition(BankInfoReq param) {
        return query.select(qBankInfo)
                .from(qBankInfo)
                .where(productPredicate(param))
                .fetch();
    }

    private BooleanBuilder productPredicate(BankInfoReq param) {
        BooleanBuilder whereBuilder = new BooleanBuilder();

        if(StringUtils.isNotNull(param.getBankId())) {
            whereBuilder.and(qBankInfo.bankId.eq(param.getBankId()));
        }

        if(StringUtils.isNotNull(param.getBankNm())) {
            whereBuilder.and(qBankInfo.bankId.contains(param.getBankId()));
        }

        if(StringUtils.isNotNull(param.getBankCd())) {
            whereBuilder.and(qBankInfo.bankId.eq(param.getBankCd()));
        }

        if(StringUtils.isNotNull(param.getBankType())) {
            whereBuilder.and(qBankInfo.bankType.eq(param.getBankType()));
        }

        if(StringUtils.isNotNull(param.getBankAcct())) {
            whereBuilder.and(qBankInfo.bankAcct.eq(param.getBankAcct()));
        }

        if(StringUtils.isNotNull(param.getUseYn())) {
            whereBuilder.and(qBankInfo.useYn.eq(param.getUseYn()));
        }

        return whereBuilder;
    }
}