package com.aibees.service.maria.account.service.account;

import com.aibees.service.maria.account.domain.dto.account.JournalHeaderReq;
import com.aibees.service.maria.account.domain.dto.account.JournalHeaderRes;
import com.aibees.service.maria.account.domain.dto.account.JournalLinesReq;
import com.aibees.service.maria.account.domain.dto.account.JournalLinesRes;
import com.aibees.service.maria.account.domain.entity.account.JournalHeader;
import com.aibees.service.maria.account.domain.entity.account.JournalHeaderSeq;
import com.aibees.service.maria.account.domain.entity.account.JournalLines;
import com.aibees.service.maria.account.domain.mapper.AccountJournalMapper;
import com.aibees.service.maria.account.domain.repo.account.JournalHeaderRepo;
import com.aibees.service.maria.account.domain.repo.account.JournalHeaderSeqRepo;
import com.aibees.service.maria.account.domain.repo.account.JournalLinesRepo;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.common.excepts.MariaException;
import com.aibees.service.maria.common.service.ServiceCommon;
import com.aibees.service.maria.common.utils.StringUtils;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class JournalService extends ServiceCommon {

    private final AccountJournalMapper journalMapper;
    private final JournalHeaderRepo headerRepo;
    private final JournalLinesRepo lineRepo;
    private final JournalHeaderSeqRepo headerSeq;

    private final DateTimeFormatter ymdFormat = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final DateTimeFormatter ymFormat = DateTimeFormatter.ofPattern("yyyyMM");
    private final DateTimeFormatter fullFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 전표 조회
     */
    public JournalHeaderRes getJournalHeaderById(JournalHeaderReq param) {
        try {
            String headerNo = param.getJeHeaderNo();

            JournalHeader header = headerRepo.findByJeHeaderNo(headerNo)
                .orElseThrow(
                    () -> new Exception("전표번호에 해당하는 전표가 없습니다.")
                );

            JournalHeaderRes result = journalMapper.toHeaderRes(header);

            List<JournalLinesRes> lineList = lineRepo.findAllByJeHeaderId(result.getJeHeaderId())
                .stream()
                .map(journalMapper::toLineRes)
                .collect(Collectors.toList());

            result.setJeLineList(lineList);

            return result;
        } catch (Exception e) {
            throw new MariaException(e.getMessage());
        }
    }

    /**
     * 전표 저장
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String saveJournal(JournalHeaderReq headerParam) {
        try {
            String headerNo = "";

            if (StringUtils.isEquals(headerParam.getTrxType(), "INSERT")) {
                createHeaderNo(headerParam);
                headerNo = insertNewJournal(headerParam);
                System.out.println("headerNo : " + headerNo);
            }

            return headerNo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new MariaException(e.getMessage());
        }
    }

    /**
     * transaction - INSERT
     */
    private String insertNewJournal(JournalHeaderReq headerParam) {
        String jeDateStr = headerParam.getJeDate().replace("-", "");
        String jeTimeStr = headerParam.getJeTimes().replace(":", "");

        String transactionTimeStr = String.join("", jeDateStr, jeTimeStr);

        JournalHeader newHeader = JournalHeader.builder()
            .jeHeaderId(headerParam.getJeHeaderId())
            .jeHeaderNo(headerParam.getJeHeaderNo())
            .jeDate(LocalDate.parse(jeDateStr, ymdFormat))
            .bankId(headerParam.getBankId())
            .sourceCd(headerParam.getSourceCd())
            .categoryCd(headerParam.getCategoryCd())
            .remark(headerParam.getRemark())
            .status("CONFIRM")
            .createDate(LocalDateTime.now())
            .internalYn(headerParam.getInternalYn())
            .statementId(headerParam.getStatementId())
            .transactionDate(LocalDateTime.parse(transactionTimeStr, fullFormat))
            .build();

        headerRepo.save(newHeader);

        List<JournalLinesReq> jeLines = headerParam.getJeLineList();

        int idx = 0;
        for (JournalLinesReq lineReq : jeLines) {

            String lineRemark = StringUtils.isNull(lineReq.getRemark()) ? headerParam.getRemark() : lineReq.getRemark();

            JournalLines newLine = JournalLines.builder()
                .jeHeaderId(headerParam.getJeHeaderId())
                .lineNo(idx)
                .acctCd(lineReq.getAcctCd())
                .amountDr(lineReq.getAmountDr())
                .amountCr(lineReq.getAmountCr())
                .remark(lineRemark)
                .build();

            lineRepo.save(newLine);

            idx++;
        }

        return headerParam.getJeHeaderNo();
    }

    /**
     * 전표 ID 생성
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void createHeaderNo(JournalHeaderReq header) {
        String ym = header.getJeDate().replace("-", "").substring(0, 6);
        String sourceCd = header.getSourceCd();
        System.out.println("ym : " + ym + " / sourceCd : " + sourceCd);
        int currSeq = -1;

        try {
            int lastSeq = headerSeq.getMaxSeq(ym, sourceCd).intValue();
            currSeq = lastSeq + 1;
        } catch (NullPointerException e) {
            currSeq = 1;
        }
        String numbering = StringUtils.lpad(Integer.toString(currSeq), 6, "0");

        String headerNo = String.join("-", ym, numbering, sourceCd); // 202411-00001-EX
        header.setJeHeaderNo(headerNo);

        long jeHeaderCnt = headerRepo.count() + 1;
        header.setJeHeaderId(jeHeaderCnt);

        // seq insert
        JournalHeaderSeq newSeq = new JournalHeaderSeq(ym, sourceCd, currSeq);
        headerSeq.save(newSeq);
    }
}
