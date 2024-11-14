package com.aibees.service.maria.account.domain.repo.card;

import com.aibees.service.maria.account.domain.dto.card.CardInfoReq;
import com.aibees.service.maria.account.domain.entity.card.AccountCardInfo;
import com.aibees.service.maria.account.domain.entity.card.QAccountCardInfo;
import com.aibees.service.maria.account.domain.entity.card.pk.AccountCardInfoPk;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountCardInfoRepo extends JpaRepository<AccountCardInfo, AccountCardInfoPk>, CardInfoCustom {

}

interface CardInfoCustom {
    List<AccountCardInfo> cardInfoList(CardInfoReq param);
}

@AllArgsConstructor
class CardInfoCustomImpl implements CardInfoCustom {
    private final JPAQueryFactory query;
    private static final QAccountCardInfo qCardInfo = QAccountCardInfo.accountCardInfo;

    @Override
    public List<AccountCardInfo> cardInfoList(CardInfoReq param) {
        return query.selectFrom(qCardInfo)
            .where(whereClause(param))
            .fetch();
    }

    private BooleanBuilder whereClause(CardInfoReq param) {
        BooleanBuilder where = new BooleanBuilder();
        // TODO : where 추가
        return where;
    }
}
