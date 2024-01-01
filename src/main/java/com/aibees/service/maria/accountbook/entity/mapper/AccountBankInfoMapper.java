package com.aibees.service.maria.accountbook.entity.mapper;

import com.aibees.service.maria.accountbook.entity.vo.BankInfoStatement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AccountBankInfoMapper extends AccountMapper {
    List<BankInfoStatement> selectBankInfoListByCondition(Map<String, Object> param);
}
