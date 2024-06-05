package com.aibees.service.maria.common.service;

import com.aibees.service.maria.common.domain.dto.CounterDto;
import com.aibees.service.maria.common.domain.entity.CommonCounter;
import com.aibees.service.maria.common.domain.entity.CommonHistory;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.common.domain.entity.pk.CommonCounterPk;
import com.aibees.service.maria.common.domain.repo.CommonCounterRepo;
import com.aibees.service.maria.common.domain.repo.CommonHistoryRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommonService extends ServiceCommon {

    private final CommonHistoryRepo historyRepo;
    private final CommonCounterRepo counterRepo;

    @Transactional
    public ResponseEntity<ResponseData> getTodayCounter(CounterDto param) {

        String toDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        try {
            // division 별 일별 cnt 조회
            Optional<CommonCounter> dailyCntOpt = counterRepo.findById(
                CommonCounterPk.builder()
                    .division(param.getDivision())
                    .cntDate(toDate)
                    .build()
            );

            Long addedCnt = null;
            CommonCounter daily;
            System.out.println("isPresent : " + dailyCntOpt.isPresent());
            if(dailyCntOpt.isPresent()) { // 존재한다면
                // add count
                daily = dailyCntOpt.get();
                addedCnt = daily.getCnt() + 1L;
                daily.setCnt(addedCnt);

            } else {
                daily = CommonCounter.builder()
                        .division(param.getDivision())
                        .cntDate(toDate)
                        .cnt(1L)
                        .build();
                addedCnt = 1L;
            }

            Long totalCnt = counterRepo.findAllByDivision(param.getDivision())
                    .stream().mapToLong(CommonCounter::getCnt).sum() + 1L;

            saveCounterInfo(daily, param);

            Map<String, Long> result = new HashMap<>();
            result.put("todayCnt", addedCnt);
            result.put("totalCnt", totalCnt);

            return successResponse(result);
        } catch(Exception e) {
            e.printStackTrace();
            return failedResponse(e);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void saveCounterInfo(CommonCounter counter, CounterDto param) {
        counterRepo.save(counter);

        // add to common_history
        CommonHistory historyEntity = CommonHistory.builder()
            .uri(param.getUri())
            .userAgent(param.getUserAgent())
            .createTime(LocalDateTime.now())
            .build();

        historyRepo.save(historyEntity);
    }
}
