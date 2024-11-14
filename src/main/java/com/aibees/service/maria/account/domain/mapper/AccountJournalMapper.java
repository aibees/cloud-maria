package com.aibees.service.maria.account.domain.mapper;

import com.aibees.service.maria.account.domain.dto.account.JournalHeaderRes;
import com.aibees.service.maria.account.domain.dto.account.JournalLinesReq;
import com.aibees.service.maria.account.domain.dto.account.JournalLinesRes;
import com.aibees.service.maria.account.domain.entity.account.JournalHeader;
import com.aibees.service.maria.account.domain.entity.account.JournalLines;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountJournalMapper {

    JournalHeaderRes toHeaderRes(JournalHeader entity);
    JournalHeader toHeaderEntity(JournalLinesReq param);

    JournalLinesRes toLineRes(JournalLines entity);
    JournalLines toLineEntity(JournalLinesReq param);
}
