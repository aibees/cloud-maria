package com.aibees.service.maria.accountbook.service;

import com.aibees.service.maria.accountbook.entity.mapper.StaticsMapper;
import com.aibees.service.maria.accountbook.entity.staticsVo.Data;
import com.aibees.service.maria.accountbook.entity.staticsVo.UsageData;
import com.aibees.service.maria.common.MapUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class StaticsService {

    private final StaticsMapper staticsMapper;

    /**
     * 월 별 사용
     * @param type
     * @param ym
     * @return
     */
    public Map<String, Object> getUsageDoughnut(String type, String ym) {
        Map<String, Object> result = new HashMap<>();
        // 월 기준 카드 별 구분 별 가격 비교 시작

        // 데이터 조회
        List<Map<String, Object>> usageData = staticsMapper.selectUsageDoughnutByYm(ym);


        Map<String, Data> allUsage = new HashMap<>();
        Map<String, Map<String, Data>> dataByCard = new HashMap<>();


        // 1. 카드 별로 분리 & All 처리를 위한 별도 분리
        usageData.forEach(data -> {
            String cardName = MapUtils.getString(data, "cardName");

            Map<String, Data> usageDatabyCard;
            // dataByCard 에 카드 별 데이터를 분리시키고 카드 별로 다시 작업한다
            if(dataByCard.containsKey(cardName)) { // 카드있으면
                usageDatabyCard = dataByCard.get(cardName); // 있던거 꺼내주고
            } else { // 없으면
                usageDatabyCard = new HashMap<>(); // 새로 container 만들어주고
            }
            // container에 usageData 객체 만들어 넣어준다.
            usageDatabyCard.put(MapUtils.getString(data, "usageNm"),
                new UsageData(
                    MapUtils.getString(data,"usageCd"),
                    MapUtils.getString(data,"usageNm"),
                    "#" + MapUtils.getString(data,"usageColor"),
                    MapUtils.getInteger(data,"sumAmt")

                ));
            // 새로 객체 넣었으면 다시 cardName에 데이터 update
            dataByCard.put(cardName, usageDatabyCard);

            // All 처리하려고 usage 별도 처리
            String usageNm = MapUtils.getString(data, "usageNm");

            UsageData usg;
            if(allUsage.containsKey(usageNm)) {
                // 이미 한번 확인한 usage가 있다면 amount sum만 해준다.
                usg = (UsageData) allUsage.get(usageNm);
                usg.sum(MapUtils.getInteger(data, "sumAmt"));
            } else {
                // 처음보는 usage면 new 객체 만들어서 put
                usg = new UsageData(
                        MapUtils.getString(data,"usageCd"),
                        MapUtils.getString(data,"usageNm"),
                        "#" + MapUtils.getString(data,"usageColor"),
                        MapUtils.getInteger(data,"sumAmt")
                );
            }
            allUsage.put(usageNm, usg);
        });

        // 2. AllData
        result.put("all", createDoughnutDataset(allUsage));
        dataByCard.keySet().forEach(k -> {
            result.put(k, createDoughnutDataset(dataByCard.get(k)));
        });

        return result;
    }

    private Map<String, Object> createDoughnutDataset(Map<String, Data> dataMap) {
        System.out.println("createDoughnutDataset");
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> dataSets = new ArrayList<>();
        Map<String, Object> dataSet = new HashMap<>();

        List<String> labels = new ArrayList<>();
        List<String> color = new ArrayList<>();
        List<Integer> amtData = new ArrayList<>();

        // Map -> List
        List<Data> dataList = new ArrayList<>();
        dataMap.keySet().forEach(k -> dataList.add(dataMap.get(k)));

        // 큰 순서대로 sorting
        Collections.sort(dataList, new Comparator<Data>() {
            @Override
            public int compare(Data o1, Data o2) {
                return (o1.getAmount() < o2.getAmount()) ? 1 : -1;
            }
        });

        // sorting 순서대로 데이터 넣기
        dataList.forEach(data -> {
            UsageData uData = (UsageData)data;
            labels.add(uData.getUsageNm());
            color.add(uData.getUsageColor());
            amtData.add(uData.getAmount());
        });

        dataSet.put("backgroundColor", color);
        dataSet.put("data", amtData);

        dataSets.add(dataSet);

        result.put("labels", labels);
        result.put("datasets", dataSets);
        /*
        data = {
          labels: [...],
          dataSets: [
            {
              backgroundColor: [...],
              data: [...]
            }
          ]
        }
         */
        return result;
    }
}
