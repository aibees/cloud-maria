package com.aibees.service.maria.account.domain.repo.account;

import com.aibees.service.maria.account.domain.dto.account.AcctMasterReq;
import com.aibees.service.maria.account.domain.dto.account.AcctMasterRes;
import com.aibees.service.maria.account.domain.entity.account.AcctMaster;
import com.aibees.service.maria.account.domain.entity.account.QAcctMaster;
import com.aibees.service.maria.common.utils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AcctMasterRepo extends JpaRepository<AcctMaster, String>, AcctMasterCustom {

    List<AcctMaster> findAllByAcctCdContainingOrAcctNmContaining(String acctCd, String acctNm);
    List<AcctMaster> findAllByEnabledFlagAndFinalFlag(String enabledFlag, String finalFlag);
}

interface AcctMasterCustom {
    List<AcctMasterRes> selectAcctMasterSetting(AcctMasterReq param);
}

@AllArgsConstructor
class AcctMasterCustomImpl implements  AcctMasterCustom {

    private final JPAQueryFactory query;
    private static final QAcctMaster qAcctMaster = QAcctMaster.acctMaster;

    @Override
    public List<AcctMasterRes> selectAcctMasterSetting(AcctMasterReq param) {
        return query.select(Projections.bean(
            AcctMasterRes.class,
            qAcctMaster.acctCd,
            qAcctMaster.acctNm,
            qAcctMaster.parentAcctCd,
            qAcctMaster.enabledFlag,
            qAcctMaster.finalFlag,
            qAcctMaster.acctType
        )).from(qAcctMaster)
            .where(predicate(param))
            .fetch();
    }

    private BooleanBuilder predicate(AcctMasterReq param) {
        BooleanBuilder whereClause = new BooleanBuilder();

        if (StringUtils.isEquals(param.getSettingType(), "first")) {
            whereClause.and(qAcctMaster.parentAcctCd.isNull());
            return whereClause;
        } else {
            whereClause.and(qAcctMaster.parentAcctCd.eq(param.getAcctCd()));
        }

        return whereClause;
    }
}