package com.aibees.service.maria.account.domain.repo.account;

import com.aibees.service.maria.account.domain.entity.account.JournalHeader;
import com.aibees.service.maria.account.domain.entity.account.QJournalHeader;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalHeaderRepo extends JpaRepository<JournalHeader, String>, JournalHeaderCustom {

    Optional<JournalHeader> findByJeHeaderNo(String jeHeaderNo);
}

interface JournalHeaderCustom {

}

@AllArgsConstructor
class JournalHeaderCustomImpl implements JournalHeaderCustom {
    private final JPAQueryFactory query;

    private static final QJournalHeader qHeader = QJournalHeader.journalHeader;


}
