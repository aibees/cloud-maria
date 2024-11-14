package com.aibees.service.maria.account.service.card;

import com.aibees.service.maria.account.domain.dto.card.CardInfoReq;
import com.aibees.service.maria.account.domain.dto.card.CardInfoRes;
import com.aibees.service.maria.account.domain.entity.card.AccountCardInfo;
import com.aibees.service.maria.account.domain.entity.card.pk.AccountCardInfoPk;
import com.aibees.service.maria.account.domain.mapper.AccountCardInfoMapper;
import com.aibees.service.maria.account.domain.repo.card.AccountCardInfoRepo;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.common.service.ServiceCommon;
import com.aibees.service.maria.common.utils.StringUtils;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CardService extends ServiceCommon {

    private final AccountCardInfoRepo cardInfoRepo;
    private final AccountCardInfoMapper cardInfoMapper;

    public ResponseEntity<ResponseData> getCardInfoList(CardInfoReq param) {
        try {
            List<CardInfoRes> cardInfoList = cardInfoRepo.cardInfoList(param)
                .stream()
                .map(entity -> {
                    CardInfoRes resp = cardInfoMapper.toResp(entity);
                    String eym = resp.getExpiredYm();
                    String eymForm = eym.substring(0, 4) + '-' + eym.substring(4, 6);
                    resp.setExpiredYm(eymForm);

                    return resp;
                })
                .collect(Collectors.toList());


            return successResponse(cardInfoList);
        } catch(Exception e) {
            e.printStackTrace();
            return failedResponse(e);
        }
    }

    @Transactional
    public ResponseEntity<ResponseData> saveCardInfo(List<CardInfoReq> saveParam) {
        try {
            for (CardInfoReq target : saveParam) {
                if (StringUtils.isEquals("INSERT", target.getTrxType())) {
                    insertCardInfo(target);
                } else if (StringUtils.isEquals("DELETE", target.getTrxType())) {
                    deleteCardInfo(target);
                } else {
                    updateCardInfo(target);
                }
            }
        } catch (Exception e) {
            return failedResponse(e);
        }

        return successResponse(ResponseData.builder().data("").message("SUCCESS").build());
    }

    private void insertCardInfo(CardInfoReq data) {
        AccountCardInfo entity = cardInfoMapper.toEntity(data);

        // 데이터 추가 가공
        entity.setExpiredYm(data.getExpiredYm().replace("-", ""));

        cardInfoRepo.save(entity);
    }

    private void updateCardInfo(CardInfoReq data) throws Exception {
        AccountCardInfoPk pk = new AccountCardInfoPk(data.getCardNo(), data.getCardComp());
        AccountCardInfo info = cardInfoRepo.findById(pk).orElseThrow(() -> new Exception("수정할 데이터가 없습니다."));

        info.setCardName(data.getCardName());
        info.setDeadlineDate(data.getDeadlineDate());
        info.setPaymentDate(data.getPaymentDate());
        info.setExpiredYm(data.getExpiredYm().replace("-", ""));
        info.setCreditYn(data.getCreditYn());
        info.setUseYn(data.getUseYn());
        info.setCompany(data.getCompany());
        info.setLimitAmt(data.getLimitAmt());
        info.setExposeMain(data.getExposeMain());
    }

    private void deleteCardInfo(CardInfoReq data) throws Exception {
        AccountCardInfoPk pk = new AccountCardInfoPk(data.getCardNo(), data.getCardComp());
        Optional<AccountCardInfo> info = cardInfoRepo.findById(pk);

        if (info.isPresent()) {
            cardInfoRepo.delete(info.get());
        } else {
            throw new Exception("삭제할 데이터가 없습니다... cardNo : " + data.getCardNo());
        }
    }
}
