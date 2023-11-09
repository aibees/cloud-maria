package com.aibees.service.maria.common.web;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SettingMapper {

    public List<Map<String, Object>> selectSettingHeaderList(Map<String, Object> param);

    public List<Map<String, Object>> selectSettingDetailList(Integer headerId);

    public List<String> getHeaderDepartList();

    public List<String> getHeaderCategoryList();
}
