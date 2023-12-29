package com.aibees.service.maria.accountbook.entity.mapper;

import com.aibees.service.maria.accountbook.entity.vo.CardInfoStatement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AccountCardInfoMapper extends AccountMapper {
    List<CardInfoStatement> selectCardInfoListByCondition(Map<String, Object> param);
    CardInfoStatement selectCardInfoByCondition(Map<String, Object> param);

    int updateCardInfoStatement(CardInfoStatement param);
}
