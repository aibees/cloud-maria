package com.aibees.service.maria.accountbook.entity.mapper;

import com.aibees.service.maria.accountbook.entity.dto.CardDto;
import com.aibees.service.maria.accountbook.entity.vo.CardStatement;
import org.apache.ibatis.annotations.Mapper;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Map;

@Mapper
public interface AccountCardMapper extends AccountMapper {

    // card
    List<Map<String, Object>> selectCardInfoForOption();
    List<CardStatement> selectCardStatementList(Map<String, Object> param);
    int insertCardStatement(CardStatement param);
    List<Map<String, Object>> selectRecentCardStatement();
    Long selectUsedAmountByYm(Map<String, Object> param);

    //// excel import tmp
    int insertCardStatementTmp(CardStatement param);
    List<CardStatement> getImportedCardStatementTmp(String fileId);
    int deleteCardStatementTmp(String fileHash);

    //// import file hashname
    int insertTmpFileHashName(Map<String, Object> param);
    int deleteTmpFileHashName(Map<String, Object> param);
    List<Map<String, Object>> selectTmpFileHashName(String fileType);

    //// card daily text sms
    int insertCardStatementSms(CardStatement param) throws SQLIntegrityConstraintViolationException;
    int deleteCardStatementSms(List<CardStatement> param);
}
