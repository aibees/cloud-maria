package com.aibees.service.maria.account.domain.repo.account;

import com.aibees.service.maria.account.domain.dto.account.CashflowRes;
import com.aibees.service.maria.account.domain.dto.account.JournalLinesReq;
import com.aibees.service.maria.account.domain.entity.account.JournalLines;
import com.aibees.service.maria.account.domain.entity.account.QAcctMaster;
import com.aibees.service.maria.account.domain.entity.account.QJournalHeader;
import com.aibees.service.maria.account.domain.entity.account.QJournalLines;
import com.aibees.service.maria.account.domain.entity.account.pk.JournalLinesPk;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalLinesRepo extends JpaRepository<JournalLines, JournalLinesPk>, JournalLineCustom {

    List<JournalLines> findAllByJeHeaderId(long jeHeaderId);
}

interface JournalLineCustom {
    List<Long> selectJeHeaderIdForCashflow(JournalLinesReq param);
    List<CashflowRes> selectCashflowByGrouping(JournalLinesReq param);
    List<CashflowRes> selectCashflowByAcctType(JournalLinesReq param);
}

@AllArgsConstructor
class JournalLineCustomImpl implements JournalLineCustom {

    private final JPAQueryFactory query;

    private static final QJournalHeader qHeader = QJournalHeader.journalHeader;
    private static final QJournalLines qLines = QJournalLines.journalLines;
    private static final QAcctMaster qAcctMaster = QAcctMaster.acctMaster;
    private static final QAcctMaster qParentMaster = QAcctMaster.acctMaster;

    @Override
    public List<CashflowRes> selectCashflowByAcctType(JournalLinesReq param) {
        return query.select(Projections.bean(
            CashflowRes.class,
            qLines.acctCd,
            qAcctMaster.acctNm,
            qParentMaster.acctCd,
            qParentMaster.acctNm,
            qLines.remark,
            qLines.amountCr.as("crAmount"),
            qLines.amountDr.as("drAmount"),
            qHeader.transactionDate
            ))
            .from(qLines)
            .join(qHeader).on(qLines.jeHeaderId.eq(qHeader.jeHeaderId))
            .join(qAcctMaster).on(qLines.acctCd.eq(qAcctMaster.acctCd))
            .where(predicate(param))
            .fetch();
    }

    @Override
    public List<CashflowRes> selectCashflowByGrouping(JournalLinesReq param) {
        List<Long> ids = selectJeHeaderIdForCashflow(param);

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(qLines.jeHeaderId.in(ids));
        if ("revenue".equals(param.getFlowType())) {
            whereClause.and(qLines.amountCr.gt(0L));
            whereClause.and(qLines.amountDr.eq(0L));
        } else {
            whereClause.and(qLines.amountDr.gt(0L));
            whereClause.and(qLines.amountCr.eq(0L));
        }

        if ( !param.getYm().isEmpty() ) {
            LocalDate date = LocalDate.parse(param.getYm().concat("-01"));
            LocalDateTime startTime = date.withDayOfMonth(1).atStartOfDay();
            LocalDateTime endTime = date.withDayOfMonth(date.lengthOfMonth()).atTime(LocalTime.MAX);

            whereClause.and(qHeader.transactionDate.between(startTime, endTime));
        }

        return query.select(Projections.bean(
            CashflowRes.class,
            qLines.acctCd,
            qAcctMaster.acctNm,
            qAcctMaster.parentAcctCd,
            qLines.amountCr.sum().as("crAmount"),
            qLines.amountDr.sum().as("drAmount")
        )).from(qLines)
            .join(qHeader).on(qLines.jeHeaderId.eq(qHeader.jeHeaderId))
            .join(qAcctMaster).on(qLines.acctCd.eq(qAcctMaster.acctCd))
            .where(whereClause)
            .groupBy(
                qLines.acctCd,
                qAcctMaster.acctNm,
                qAcctMaster.parentAcctCd
            )
            .fetch();
    }

    @Override
    public List<Long> selectJeHeaderIdForCashflow(JournalLinesReq param) {
        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(qLines.acctCd.in(param.getAcctCdList()));
        if ("revenue".equals(param.getFlowType())) {
            whereClause.and(qLines.amountCr.eq(0L));
        } else {
            whereClause.and(qLines.amountDr.eq(0L));
        }

        return query.select(qLines.jeHeaderId)
            .from(qLines)
            .where(whereClause)
            .fetch();
    }

    private BooleanBuilder predicate(JournalLinesReq param) {
        System.out.println(param);
        BooleanBuilder whereClause = new BooleanBuilder();

        if ( param.getAcctCd() != null && !param.getAcctCd().isEmpty() ) {
            whereClause.and(qLines.acctCd.startsWith(param.getAcctCd()));
        }

        if ( !param.getYm().isEmpty() ) {
            LocalDate date = LocalDate.parse(param.getYm().concat("-01"));
            LocalDateTime startTime = date.withDayOfMonth(1).atStartOfDay();
            LocalDateTime endTime = date.withDayOfMonth(date.lengthOfMonth()).atTime(LocalTime.MAX);

            whereClause.and(qHeader.transactionDate.between(startTime, endTime));
        }

        return whereClause;
    }
}