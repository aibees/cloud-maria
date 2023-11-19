package com.aibees.service.maria.common.web;

import lombok.AllArgsConstructor;
import org.apache.ibatis.util.MapUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SettingService {

    private final SettingMapper commonMapper;

    public List<Map<String, Object>> getSettingHeader(Map<String, Object> param) {
        return commonMapper.selectSettingHeaderList(param);
    }

    public List<Map<String, Object>> getSettingDetail(String headerId) {
        System.out.println("headerId : " + headerId);
        return commonMapper.selectSettingDetailList(Integer.parseInt(headerId));
    }

    public List<Map<String, Object>> getSettingDetailWithHeader(Map<String, Object> param) {
        return commonMapper.selectSettingDetailListWithHeader(param);
    }

    public List<String> getSettingOptions(String option) {
        if("depart".equals(option)) {
            return commonMapper.getHeaderDepartList();
        } else if("category".equals(option)) {
            return commonMapper.getHeaderCategoryList();
        } else {
            return null;
        }
    }
}
