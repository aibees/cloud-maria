package com.aibees.service.maria.account.domain.repo.account;

import com.aibees.service.maria.account.domain.entity.account.JournalHeaderSeq;
import com.aibees.service.maria.account.domain.entity.account.QJournalHeaderSeq;
import com.aibees.service.maria.account.domain.entity.account.pk.JournalHeaderSeqPk;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalHeaderSeqRepo extends JpaRepository<JournalHeaderSeq, JournalHeaderSeqPk>, HeaderSeqCustom {
}

interface HeaderSeqCustom {
    Long getMaxSeq(String ym, String sourceCd);
}

@AllArgsConstructor
class HeaderSeqCustomImpl implements  HeaderSeqCustom {


    private final JPAQueryFactory query;
    private static final QJournalHeaderSeq qHeaderSeq = QJournalHeaderSeq.journalHeaderSeq;

    @Override
    public Long getMaxSeq(String ym, String sourceCd) {
        return query.select(qHeaderSeq.seq.max())
            .from(qHeaderSeq)
            .where(qHeaderSeq.ym.eq(ym).and(qHeaderSeq.sourceCd.eq(sourceCd)))
            .fetchOne();
    }
}