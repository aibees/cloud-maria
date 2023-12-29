package com.aibees.service.maria.accountbook.entity.mapper;

import com.aibees.service.maria.accountbook.entity.dto.BankDto;
import com.aibees.service.maria.accountbook.entity.vo.BankStatement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface AccountBankMapper extends AccountMapper {
    List<BankStatement> selectBankStatementList(Map<String, Object> params);
    List<BankStatement> getImportedBankStatementTmp(String fileId);

    int insertBankStatement(BankStatement params);
    int insertBankStatementTmp(BankStatement params);
    int insertTmpFileHashName(Map<String, Object> param);

    int deleteBankStatementTmp(String fileHash);
    int deleteTmpFileHashName(Map<String, Object> param);
}
