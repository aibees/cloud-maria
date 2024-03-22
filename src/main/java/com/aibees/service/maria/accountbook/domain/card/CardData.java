package com.aibees.service.maria.accountbook.domain.card;

import com.aibees.service.maria.accountbook.domain.AbstractDataInfo;
import com.aibees.service.maria.accountbook.domain.DataInfo;
import com.aibees.service.maria.accountbook.entity.dto.CardDto;
import com.aibees.service.maria.accountbook.entity.mapper.AccountCardInfoMapper;
import com.aibees.service.maria.accountbook.entity.mapper.AccountCardMapper;
import com.aibees.service.maria.accountbook.entity.vo.CardInfoStatement;
import com.aibees.service.maria.accountbook.entity.vo.CardStatement;
import com.aibees.service.maria.accountbook.util.AccConstant;
import com.aibees.service.maria.accountbook.util.handler.ExcelParseHandler;
import com.aibees.service.maria.accountbook.util.handler.TextParseHandler;
import com.aibees.service.maria.common.MapUtils;
import com.aibees.service.maria.common.StringUtils;
import com.google.common.collect.ImmutableMap;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Component
@SuperBuilder
@Slf4j
public class CardData extends AbstractDataInfo implements DataInfo {
    private final AccountCardMapper cardMapper;
    private final AccountCardInfoMapper cardInfoMapper;

    public static CardData createForStatement(AccountCardMapper mapper) {
        return CardData.builder()
                .cardMapper(mapper)
                .build();
    }

    public List<CardStatement> getCardStatementList(CardDto param) throws Exception {
        Map<String, Object> paramMap = paramValidate(param);
        return cardMapper.selectCardStatementList(paramMap);
    }

    private Map<String, Object> paramValidate(CardDto param) throws Exception {
        Map<String, Object> paramMap = new HashMap<>();

        // 1. invalid paramter
        if(Long.toString(param.getAmountFrom()).contains("\\.")) {
            throw new Exception("올바르지 않은 형식입니다. [" + param.getAmountFrom() + "]");
        }
        if(Long.toString(param.getAmountTo()).contains("\\.")) {
            throw new Exception("올바르지 않은 형식입니다. [" + param.getAmountTo() + "]");
        }

        return param.toMap();
    }

    @Override
    public void excelParse(MultipartFile file) throws Exception {

    }

    @Override
    public void textParse(String type, String smsText) throws Exception {

    }

    @Override
    protected boolean transferToExcelTmp() throws Exception {
        return false;
    }

    @Override
    protected boolean transferToMain() throws Exception {
        return false;
    }

    @Override
    protected boolean transferToSMS() throws Exception {
        return false;
    }

    @Override
    protected boolean transferToInfo() throws Exception {
        return false;
    }
}
