package com.aibees.service.maria.accountbook.domain.card;

import com.aibees.service.maria.accountbook.domain.AbstractDataInfo;
import com.aibees.service.maria.accountbook.domain.DataInfo;
import com.aibees.service.maria.accountbook.entity.mapper.AccountCardInfoMapper;
import com.aibees.service.maria.accountbook.entity.mapper.AccountCardMapper;
import com.aibees.service.maria.accountbook.entity.vo.CardInfoStatement;
import com.aibees.service.maria.accountbook.entity.vo.CardStatement;
import com.aibees.service.maria.accountbook.util.AccConstant;
import com.aibees.service.maria.accountbook.util.handler.ExcelParseHandler;
import com.aibees.service.maria.accountbook.util.handler.TextParseHandler;
import com.aibees.service.maria.common.MapUtils;
import com.google.common.collect.ImmutableMap;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.aibees.service.maria.accountbook.util.AccConstant.IMPORT_CARD;

@Component
@SuperBuilder
@Slf4j
public class CardData extends AbstractDataInfo implements DataInfo {

    // card data mapper
    private AccountCardMapper cardMapper;

    // card infodata mapper
    private AccountCardInfoMapper infoMapper;

    // card statement List container
    private List<CardStatement> cardStatements;
    private CardStatement cardStatement;

    // card info statement List container
    private List<CardInfoStatement> cardInfoStatements;
    private CardInfoStatement cardInfoStatement;

    private String fileHashName;

    private CardData() {
        super(null, null);
    }

    /**************************
     *** static constructor ***
     **************************/
    public static CardData createWithDefault(String type, AccountCardMapper mapper) {
        return CardData.builder()
                .type(type)
                .cardMapper(mapper)
                .build();
    }

    public static CardData createWithTransfer(String type, String trx_type, AccountCardMapper mapper, AccountCardInfoMapper infoMapper) {
        return CardData.builder()
                .cardMapper(mapper)
                .infoMapper(infoMapper)
                .type(type)
                .trx_type(TRANSFER_TYPE.valueOf(trx_type))
                .build();
    }

    public static CardData createWithInfo(AccountCardInfoMapper mapper) {
        return CardData.builder()
                .trx_type(TRANSFER_TYPE.TO_INFO)
                .infoMapper(mapper)
                .build();
    }

    /**************************
     *** Override  function ***
     **************************/

    @Override
    public void excelParse(MultipartFile file) throws Exception {
        try {

            Workbook workbook = null;
            String[] fileName = file.getOriginalFilename().split("\\.");
            this.fileHashName = this.createFileNameHash();

            if (fileName[fileName.length-1].equals("xlsx")) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else if (fileName[fileName.length-1].equals("xls")) {
                workbook = new HSSFWorkbook(file.getInputStream());
            }

            if(workbook == null) {
                throw new Exception("workBook is Null");
            }

            cardStatements = (List<CardStatement>) ExcelParseHandler.excelParser(workbook, fileHashName, type).get(AccConstant.CM_RESULT);
            registFileHashName(fileHashName, file.getOriginalFilename());
        } catch(Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void textParse(String type, String smsText) throws Exception {
        Map<String, Object> statement = TextParseHandler.textParser(type, smsText, infoMapper);
        this.cardStatement = (CardStatement) statement.get("statement");

        if(Objects.isNull(this.cardStatement)) {
            throw new Exception("정의되지 않은 형식이거나 없는 카드정보입니다.");
        }
    }

    @Override
    public void prepareStatementListByCondition(Map<String, Object> param) throws Exception {
        this.cardStatements = cardMapper.selectCardStatementList(param);
    }

    @Override
    public void prepareStatementTmpByFileId(String fileId) throws Exception {
        this.cardStatements = cardMapper.getImportedCardStatementTmp(fileId);
    }

    @Override
    public void prepareInfoStatementByCondition(Map<String, Object> param) throws Exception {
        this.cardInfoStatements = infoMapper.selectCardInfoListByCondition(param)
                                            .stream()
                                            .peek(infoData -> {
                                                // 만료년월
                                                String exYm = infoData.getExpiredYm();
                                                infoData.setExpiredYm(String.join("-", exYm.substring(0, 4), exYm.substring(4)));

                                                //사용한도
                                                infoData.setLimitAmt(Optional.ofNullable(infoData.getLimitAmt()).orElse(-1L));
                                            })
                                            .collect(Collectors.toList());
    }

    // abstractDataInfo
    @Override
    protected boolean transferToExcelTmp() {
        AtomicInteger resultCnt = new AtomicInteger(0);
        int listCnt = cardStatements.size();
        boolean result = true;

        cardStatements.forEach(state -> {
            int insertResult = cardMapper.insertCardStatementTmp(state);
            resultCnt.addAndGet(insertResult);
        });

        if(listCnt != resultCnt.get()) {
            result = false;
        }
        return result;
    }

    @Override
    protected boolean transferToMain() {
        log.info("========= transferToMain =========");
        AtomicInteger resultCnt = new AtomicInteger(0);
        int listCnt = cardStatements.size();
        boolean result = true;

        cardStatements.forEach(state -> {
            int insertResult = cardMapper.insertCardStatement(state);
            resultCnt.addAndGet(insertResult);
        });

        log.info("insert completed");
        if(listCnt > resultCnt.get()) { // duplicate key 경우 2를 반환하기에 해당 경우의 수도 염두
            log.info("resultCnt Error");
            result = false;
        } else {
            // 후속작업, sms에 있는 tmp내용 삭제
            List<CardStatement> paramList = this.cardStatements.stream()
                    .map(state ->
                            CardStatement.builder()
                                         .ymd(state.getYmd().replace(".", ""))
                                         .times(state.getTimes().replace(":", "").substring(0, 4).concat("00"))
                                         .cardNo(state.getCardNo())
                                         .build())
                    .collect(Collectors.toList());

            cardMapper.deleteCardStatementSms(paramList);
        }

        cardMapper.deleteCardStatementTmp(this.fileHashName);
        cardMapper.deleteTmpFileHashName(ImmutableMap.of("fileId", this.fileHashName, "fileType", IMPORT_CARD));
        return result;
    }

    @Override
    protected boolean transferToSMS() {
        boolean result = false;

        if(cardMapper.insertCardStatement(this.cardStatement) == 1)
            result = true;

        return result;
    }

    @Override
    protected boolean transferToInfo() throws Exception {
        AtomicInteger resultCnt = new AtomicInteger(0);
        int listCnt = cardInfoStatements.size();
        boolean result = true;

        cardInfoStatements.forEach(state -> {
            int updateResult = infoMapper.updateCardInfoStatement(state);
            resultCnt.addAndGet(updateResult);
        });

        if(listCnt > resultCnt.get()) { // duplicate key 경우 2를 반환하기에 해당 경우의 수도 염두
            result = false;
        }

        return result;
    }

    private void registFileHashName(String fileHash, String originName) throws Exception {
        cardMapper.insertTmpFileHashName(ImmutableMap.of(
                "fileId", fileHash,
                "fileType", IMPORT_CARD,
                "fileName", originName
        ));
    }

    /**************************
     *** Original  function ***
     **************************/
    public List<CardStatement> getCardStatements() {
        return this.cardStatements;
    }

    public void setCardStatements(List<CardStatement> statements) {
        this.cardStatements = statements;
    }

    public void setCardStatementsWithMap(List<Map<String, Object>> data) {
        this.cardStatements = data.stream().map(each ->
                CardStatement.builder()
                             .ymd(MapUtils.getString(each, "ymd"))
                             .times(MapUtils.getString(each, "times"))
                             .approvNum(MapUtils.getString(each, "approvNum"))
                             .cardNo(MapUtils.getString(each, "cardNo"))
                             .cardNm(MapUtils.getString(each, "cardNm"))
                             .usageCd(MapUtils.getString(each, "usageCd"))
                             .usageNm(MapUtils.getString(each, "usageNm"))
                             .usageColor(MapUtils.getString(each, "usageColor"))
                             .remark(MapUtils.getString(each, "remark"))
                             .amount(MapUtils.getLong(each, "amount"))
                             .apYn(MapUtils.getString(each, "apYn"))
                             .status(MapUtils.getString(each, "status"))
                             .build())
                .collect(Collectors.toList());
    }

    public List<CardInfoStatement> getCardInfoStatements() {
        return this.cardInfoStatements;
    }

    public void setCardInfoStatementsWithMap(List<Map<String, Object>> data) {
        this.cardInfoStatements = data.stream().map(each ->
                CardInfoStatement.builder()
                                 .cardNo(MapUtils.getString(each, "cardNo"))
                                 .cardName(MapUtils.getString(each, "cardName"))
                                 .bankCd(MapUtils.getString(each, "bankCd"))
                                 .deadlineDate(MapUtils.getString(each, "deadlineDate"))
                                 .paymentDate(MapUtils.getString(each, "paymentDate"))
                                 .expiredYm(MapUtils.getString(each, "expiredYm").replace("-", ""))
                                 .limitAmt(Long.parseLong(MapUtils.getString(each, "limitAmt").replace(",", "")))
                                 .creditYn(MapUtils.getString(each, "creditYn"))
                                 .useYn(MapUtils.getString(each, "useYn"))
                                 .selectedMain(MapUtils.getString(each, "selectedMain"))
                                 .company(MapUtils.getString(each, "company"))
                                 .build())
                .collect(Collectors.toList());
    }

    public void setFileHashName(String name) {
        this.fileHashName = name;
    }

    public String getFileHashName() {
        return this.fileHashName;
    }
}
