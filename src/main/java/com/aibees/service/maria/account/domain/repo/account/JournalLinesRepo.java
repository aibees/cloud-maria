package com.aibees.service.maria.account.domain.repo.account;

import com.aibees.service.maria.account.domain.entity.account.JournalLines;
import com.aibees.service.maria.account.domain.entity.account.pk.JournalLinesPk;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalLinesRepo extends JpaRepository<JournalLines, JournalLinesPk> {

    List<JournalLines> findAllByJeHeaderId(long jeHeaderId);
}
