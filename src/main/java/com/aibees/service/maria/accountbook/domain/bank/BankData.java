package com.aibees.service.maria.accountbook.domain.bank;

import com.aibees.service.maria.accountbook.domain.AbstractDataInfo;
import com.aibees.service.maria.accountbook.domain.DataInfo;
import com.aibees.service.maria.accountbook.entity.mapper.AccountBankCloseMapper;
import com.aibees.service.maria.accountbook.entity.mapper.AccountBankInfoMapper;
import com.aibees.service.maria.accountbook.entity.mapper.AccountBankMapper;
import com.aibees.service.maria.accountbook.entity.vo.BankInfoStatement;
import com.aibees.service.maria.accountbook.entity.vo.BankStatement;
import com.aibees.service.maria.accountbook.util.AccConstant;
import com.aibees.service.maria.accountbook.util.handler.ExcelParseHandler;
import com.aibees.service.maria.common.MapUtils;
import com.google.common.collect.ImmutableMap;
import lombok.experimental.SuperBuilder;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.aibees.service.maria.accountbook.util.AccConstant.IMPORT_BANK;
import static com.aibees.service.maria.accountbook.util.AccConstant.IMPORT_CARD;

@Component
@SuperBuilder
public class BankData extends AbstractDataInfo implements DataInfo {

    // bank data
    private AccountBankMapper bankMapper;

    private List<BankStatement> bankStatements;
    private BankStatement bankStatement;

    // bank infodata mapper
    private AccountBankInfoMapper infoMapper;

    private List<BankInfoStatement> bankInfoStatements;
    private BankInfoStatement bankInfoStatement;

    //bank closeData mapper
    private AccountBankCloseMapper closeMapper;
    private List<Map<String, Object>> closeDataList;

    // bank statement List container

    private String fileHashName;

    private BankData() {
        super(null, null);
    }

    /**************************
     *** static constructor ***
     **************************/
    public static BankData createWithDefault(String type, AccountBankMapper mapper) {
        return BankData.builder()
                .type(type)
                .bankMapper(mapper)
                .build();
    }

    public static BankData createWithTransfer(String type, String trx_type, AccountBankMapper mapper, AccountBankInfoMapper infoMapper) {
        return BankData.builder()
                .bankMapper(mapper)
                .infoMapper(infoMapper)
                .type(type)
                .trx_type(TRANSFER_TYPE.valueOf(trx_type))
                .build();
    }

    public static BankData createWithInfo(AccountBankInfoMapper infoMapper) {
        return BankData.builder()
                .trx_type(TRANSFER_TYPE.TO_INFO)
                .infoMapper(infoMapper)
                .build();
    }

    public static BankData createWithClose(AccountBankCloseMapper closeMapper, AccountBankInfoMapper infoMapper) {
        return BankData.builder()
                .closeMapper(closeMapper)
                .infoMapper(infoMapper)
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
            bankStatements = (List<BankStatement>) ExcelParseHandler.excelParser(workbook, fileHashName, type).get(AccConstant.CM_RESULT);
            registFileHashName(fileHashName, file.getOriginalFilename());
        } catch(Exception e) {
            e.printStackTrace();
                throw new Exception(e.getMessage());
        }
    }

    @Override
    public void textParse(String type, String smsText) throws Exception {

    }

    @Override
    public void prepareStatementListByCondition(Map<String, Object> param) throws Exception {
        this.bankStatements = bankMapper.selectBankStatementList(param);
    }

    @Override
    public void prepareStatementTmpByFileId(String fileId) throws Exception {
        this.bankStatements = bankMapper.getImportedBankStatementTmp(fileId);
    }

    @Override
    public void prepareInfoStatementByCondition(Map<String, Object> param) throws Exception {
        this.bankInfoStatements = infoMapper.selectBankInfoListByCondition(param);
    }

    @Override
    public void prepareCloseDataByCondition(Map<String, Object> param) throws Exception {
        String selectType = MapUtils.getString(param, "type");
        Map<String, Object> queryParam = (Map<String, Object>) param.get("param");

        if("closeDataList".equals(selectType)) {
            this.closeDataList = closeMapper.getBankAmountByUsageAndYm(queryParam);
        } else if("closeDetailList".equals(selectType)) {
            this.closeDataList = closeMapper.getBankCloseDetailForCheck(queryParam);
        }
    }

    @Override
    protected boolean transferToExcelTmp() throws Exception {
        AtomicInteger resultCnt = new AtomicInteger(0);
        int listCnt = bankStatements.size();
        boolean result = true;

        bankStatements.forEach(state -> {
            int insertResult = bankMapper.insertBankStatementTmp(state);
            resultCnt.addAndGet(insertResult);
        });

        if(listCnt != resultCnt.get()) {
            result = false;
        }
        return result;
    }

    @Override
    protected boolean transferToMain() throws Exception {
        AtomicInteger resultCnt = new AtomicInteger(0);
        int listCnt = bankStatements.size();
        boolean result = true;

        bankStatements.forEach(state -> {
            int insertResult = bankMapper.insertBankStatement(state);
            resultCnt.addAndGet(insertResult);
        });

        if(listCnt > resultCnt.get()) { // duplicate key 경우 2를 반환하기에 해당 경우의 수도 염두두
            result = false;
            throw new Exception("저장 실패");
        }

        bankMapper.deleteBankStatementTmp(this.fileHashName);
        bankMapper.deleteTmpFileHashName(ImmutableMap.of("fileId", this.fileHashName, "fileType", IMPORT_BANK));
        return result;
    }

    @Override
    protected boolean transferToSMS() throws Exception {
        return false;
    }

    @Override
    protected boolean transferToInfo() throws Exception {
        return false;
    }

    private void registFileHashName(String fileHash, String originName) throws Exception {
        bankMapper.insertTmpFileHashName(ImmutableMap.of(
                "fileId", fileHash,
                "fileType", IMPORT_BANK,
                "fileName", originName
        ));
    }

    /**************************
     **** get/set function ****
     **************************/
    public AccountBankCloseMapper getCloseMapper() {
        return this.closeMapper;
    }

    public List<BankStatement> getBankStatements() {
        return this.bankStatements;
    }

    public BankStatement getBankStatement() {
        return this.bankStatement;
    }

    public void setBankStatements(List<BankStatement> statements) {
        this.bankStatements = statements;
    }

    public void setBankStatementsWithMap(List<Map<String, Object>> data) {
        this.bankStatements = data.stream().map(each ->
                BankStatement.builder()
                             .ymd(MapUtils.getString(each, "ymd").replace("-", "").replace(".", ""))
                             .times(MapUtils.getString(each, "times").replace(":", ""))
                             .bankId(MapUtils.getString(each, "bankId"))
                             .bankNm(MapUtils.getString(each, "bankNm"))
                             .usageCd(MapUtils.getString(each, "usageCd"))
                             .usageNm(MapUtils.getString(each, "usageNm"))
                             .usageColor(MapUtils.getString(each, "usageColor"))
                             .entryCd(MapUtils.getString(each, "entryCd"))
                             .entryNm(MapUtils.getString(each, "entryNm"))
                             .remark(MapUtils.getString(each, "remark"))
                             .amount(MapUtils.getLong(each, "amount"))
                             .build())
                .collect(Collectors.toList());
    }

    public List<BankInfoStatement> getBankInfoStatements() {
        return this.bankInfoStatements;
    }

    public void setBankInfoStatementsWithMap(List<Map<String, Object>> data) {

    }

    public List<Map<String, Object>> getCloseDataList() {
        return this.closeDataList;
    }

    public void setFileHashName(String fileHash) {
        this.fileHashName = fileHash;
    }

    public String getFileHashName() {
        return this.fileHashName;
    }
}
