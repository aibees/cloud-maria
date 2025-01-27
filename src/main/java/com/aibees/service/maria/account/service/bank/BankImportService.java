package com.aibees.service.maria.account.service.bank;

import com.aibees.service.maria.account.domain.dto.account.ImportFileRes;
import com.aibees.service.maria.account.domain.dto.account.JournalHeaderReq;
import com.aibees.service.maria.account.domain.dto.account.JournalLinesReq;
import com.aibees.service.maria.account.domain.dto.bank.BankImportReq;
import com.aibees.service.maria.account.domain.dto.bank.BankImportRes;
import com.aibees.service.maria.account.domain.entity.account.AccountImportFile;
import com.aibees.service.maria.account.domain.entity.account.ImportStatementTmp;
import com.aibees.service.maria.account.domain.entity.bank.BankInfo;
import com.aibees.service.maria.account.domain.mapper.ImportStatementMapper;
import com.aibees.service.maria.account.domain.repo.account.ImportFileRepo;
import com.aibees.service.maria.account.domain.repo.account.ImportStatementTmpRepo;
import com.aibees.service.maria.account.domain.repo.bank.BankInfoRepo;
import com.aibees.service.maria.account.service.account.JournalService;
import com.aibees.service.maria.account.utils.handler.ExcelParseHandler;
import com.aibees.service.maria.common.excepts.MariaException;
import com.aibees.service.maria.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.aibees.service.maria.account.utils.constant.AccConstant.IMPORT_BANK;

@Service
@AllArgsConstructor
public class BankImportService {

    private final BankInfoRepo bankInfoRepo;
    private final ImportStatementTmpRepo tmpRepo;
    private final ImportFileRepo fileRepo;
    private final ImportStatementMapper tmpMapper;
    private final JournalService journalService;

    private final String TIME_DATE = "-";
    private final String TIME_HHMMSS = ":";

    /**
     * 파일 해시리스트 조회
     * 개발 완
     * @return
     */
    public List<ImportFileRes> getTmpFileList() {
        return fileRepo.findAllByFileType(IMPORT_BANK)
                .stream()
                .map(data ->
                        ImportFileRes
                        .builder()
                        .fileId(data.getFileId())
                        .fileName(data.getFileName())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * 파일 해시값으로 임시저장값 조회
     * 개발 완
     * @param param
     * @return
     */
    public List<BankImportRes> getTmpStatementList(BankImportReq param) {
        List<ImportStatementTmp> resultList = tmpRepo.findAllByFileHash(param.getFileHash());

        String bankId = resultList.get(0).getBankId();
        BankInfo info = bankInfoRepo.findById(bankId).orElse(new BankInfo());

        return resultList.stream()
            .map(tmpMapper::toBankResp)
                .peek(data -> {
                    data.setBankNm(info.getBankNm());
                    data.setYmd(convertTime(data.getYmd(), TIME_DATE));
                    data.setTimes(convertTime(data.getTimes(), TIME_HHMMSS));
                })
            .collect(Collectors.toList());
    }

    /**
     * 일자 형식 변경
     * 개발 완
     * @param time
     * @param type
     * @return
     */
    private String convertTime(String time, String type) {
        if (StringUtils.isEquals(type, TIME_DATE)) {
            String yy = time.substring(0, 4);
            String mm = time.substring(4, 6);
            String dd = time.substring(6, 8);

            return String.join(TIME_DATE, yy, mm, dd);
        } else if (StringUtils.isEquals(type, TIME_HHMMSS)) {
            String hh = time.substring(0, 2);
            String mm = time.substring(2, 4);
            String ss = time.substring(4, 6);

            return String.join(TIME_HHMMSS, hh, mm, ss);
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 임시저장
     * 개발 완
     * @param param
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public BankImportRes tmpSaveStatement(BankImportReq param) {
        System.out.println(param.toString());

        List<BankImportRes> uptData = param.getData();

        if (uptData.isEmpty()) {
            throw new MariaException("업데이트할 데이터 없음");
        }

        uptData.forEach(data -> tmpRepo.updateBankTmpStatement(data, param.getBankId()));

        return BankImportRes.builder().fileHash(param.getFileHash()).build();
    }

    /**
     * 엑셀 업로드
     * 개발 완
     * @param param
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public BankImportRes uploadBankTransaction(BankImportReq param) {
        System.out.println(param);
        String bankId = param.getBankId();
        String bankCd = bankId.substring(0, 2);
        String bankType = getBankType(bankCd);
        MultipartFile uploadFile = param.getFile();

        try {
            if (Objects.isNull(uploadFile)) {
                throw new MariaException("엑셀파일이 없습니다.");
            }

            Workbook workbook = null;
            String[] fileName = Objects.requireNonNull(uploadFile.getOriginalFilename())
                    .split("\\.");
            String fileHashName = createFileHash();

            if (fileName[fileName.length - 1].equals("xlsx")) {
                workbook = new XSSFWorkbook(uploadFile.getInputStream());
            } else if (fileName[fileName.length - 1].equals("xls")) {
                workbook = new HSSFWorkbook(uploadFile.getInputStream());
            }

            if (workbook == null) {
                throw new MariaException("엑셀파일이 비정상적입니다.");
            }

            Map<String, Object> parseParam = new HashMap<>();
            parseParam.put("bankId", bankId);
            parseParam.put("bankCd", bankCd);
            parseParam.put("fileHash", fileHashName);
            parseParam.put("bankType", bankType);

            List<ImportStatementTmp> tmpStatements =
                    ExcelParseHandler.excelParser(workbook, parseParam);

            if (Objects.isNull(tmpStatements) || tmpStatements.isEmpty()) {
                throw new MariaException("비정상적인 엑셀");
            }

            tmpRepo.saveAll(tmpStatements);

            AccountImportFile importFile = AccountImportFile
                    .builder()
                    .fileId(fileHashName)
                    .fileType(IMPORT_BANK)
                    .fileName(fileName[0])
                    .build();

            fileRepo.save(importFile);

            return BankImportRes.builder()
                    .bankId(bankId)
                    .fileHash(fileHashName)
                    .build();

        } catch (IOException e) {
            e.printStackTrace();
            throw new MariaException(e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public BankImportRes createJeLines(List<BankImportRes> data) {
        for (BankImportRes row : data) {

            // 전표 헤더 셋업
            JournalHeaderReq jeHeader = new JournalHeaderReq();
            jeHeader.setJeDate(row.getYmd());
            jeHeader.setJeTimes(row.getTimes());
            jeHeader.setBankId(row.getBankId());
            jeHeader.setSourceCd("AU");
            jeHeader.setCategoryCd("01");
            jeHeader.setRemark(row.getRemark());
            jeHeader.setStatementId(row.getStatementId());
            jeHeader.setTrxType("INSERT");

            List<JournalLinesReq> jeLines = createJeLines(row);
            jeHeader.setJeLineList(jeLines);

            journalService.saveJournal(jeHeader);

            // 임시파일 삭제
        }

        return BankImportRes.builder().build();
    }

    private List<JournalLinesReq> createJeLines(BankImportRes row) {
        JournalLinesReq line = new JournalLinesReq(); // 계좌이력
        JournalLinesReq sideLine = new JournalLinesReq(); // 계좌이력 상대라인
        line.setAcctCd(row.getAcctCd());
        line.setRemark(row.getRemark());
        sideLine.setAcctCd("1010001");
        sideLine.setRemark(row.getRemark());

        String entryCd = row.getEntryCd();
        if (entryCd.equals("0")) {
            // 수입
            line.setAmountCr(row.getAmount());
            sideLine.setAmountDr(row.getAmount());

        } else {
            // 지출

            line.setAmountDr(row.getAmount());
            sideLine.setAmountCr(row.getAmount());
        }

        List<JournalLinesReq> lines = new ArrayList<>();
        lines.add(line);
        lines.add(sideLine);

        return lines;
    }

    /**
     * 업로드 파일에 대한 hash 값 생성
     * 개발 완
     * @return
     */
    private String createFileHash() {
        return LocalDateTime
                .now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                .concat(StringUtils.getRandomStr(4));
    }

    /**
     * 은행코드에서 이름을 매핑시키는 method
     * excel parser에서 은행에 맞는 parser reflection을 해야하기에 변경해야 한다.
     * 개발 완
     * @param cd
     * @return
     */
    private String getBankType(String cd) {
        if ("88".equals(cd)) {
            return "SHINHANBANK";
        } else if ("81".equals(cd)) {
            return "HANABANK";
        } else {
            return StringUtils.EMPTY;
        }
    }
}
