package com.aibees.service.maria.accountbook.entity.mapper;

import com.aibees.service.maria.accountbook.entity.dto.CardDto;
import com.aibees.service.maria.accountbook.entity.vo.CardStatement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AccountMapper {

    // bank account

    // card
    List<Map<String, Object>> selectCardInfoForOption();
    List<CardStatement> selectCardStatementList(CardDto param);
    int insertCardStatement(Map<String, Object> param);

    //// excel import tmp
    int insertCardStatementTmp(CardStatement param);
    List<CardStatement> getImportedCardStatementTmp(String fileId);
    int deleteCardStatementTmp(String fileHash);

    //// import file hashname
    int insertTmpFileHashName(Map<String, Object> param);
    int deleteTmpFileHashName(Map<String, Object> param);
    List<Map<String, Object>> selectTmpFileHashName(String fileType);
}
