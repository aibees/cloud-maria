package com.aibees.service.maria.accountbook.entity.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AccountStaticsMapper extends AccountMapper {

    public List<Map<String, Object>> selectUsageDoughnutByYm(String ym);
}
