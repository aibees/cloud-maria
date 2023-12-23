package com.aibees.service.maria.accountbook.entity.mapper;

import com.aibees.service.maria.accountbook.entity.vo.BankStatement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountBankMapper {

    int insertBankStatementTmp(BankStatement params);
}
