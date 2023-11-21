package com.aibees.service.maria.accountbook.entity.mapper;

import com.aibees.service.maria.accountbook.entity.dto.CardDto;
import com.aibees.service.maria.accountbook.entity.vo.CardStatement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AccountMapper {

    // bank account

    // card Account
    List<CardStatement> selectCardStatementList(CardDto param);
    int insertCardStatement(Map<String, Object> param);

    //// excel import tmp
    int insertCardStatementTmp(CardStatement param);
    List<CardStatement> getImportedCardStatementTmp(String fileId);
    int deleteCardStatementTmp(String fileHash);
}
