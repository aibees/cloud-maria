package com.aibees.service.maria.accountbook.entity.mapper;

import com.aibees.service.maria.accountbook.entity.vo.BankCloseStatement;
import com.aibees.service.maria.accountbook.entity.vo.BankStatement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AccountBankCloseMapper {
    List<Map<String, Object>> getBankAmountByUsageAndYm(Map<String, Object> param);
    BankCloseStatement getBankCloseByBankidAndYm(Map<String, Object> param);

    List<Map<String, Object>> getBankCloseDetailForCheck(Map<String, Object> param);
    int updateBankStatementStatus(BankStatement statement);
    int insertBankCloseData(BankCloseStatement param);
}
