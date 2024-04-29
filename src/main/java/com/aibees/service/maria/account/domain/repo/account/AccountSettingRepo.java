package com.aibees.service.maria.account.domain.repo.account;

import com.aibees.service.maria.account.domain.dto.account.AccountSettingReq;
import com.aibees.service.maria.account.domain.dto.account.AccountSettingRes;
import com.aibees.service.maria.account.domain.entity.account.AccountSetting;
import com.aibees.service.maria.account.domain.entity.account.QAccountSetting;
import com.aibees.service.maria.account.domain.entity.account.QAccountSettingDetail;
import com.aibees.service.maria.account.domain.entity.account.pk.AccountSettingId;
import com.aibees.service.maria.common.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountSettingRepo extends JpaRepository<AccountSetting, AccountSettingId>, AccountSettingCustom {


}

interface AccountSettingCustom {
    List<AccountSettingRes> getSettingDetails(AccountSettingReq param);
}

@AllArgsConstructor
class AccountSettingCustomImpl implements AccountSettingCustom {
    private final JPAQueryFactory query;
    private static final QAccountSetting qHeader = QAccountSetting.accountSetting;
    private static final QAccountSettingDetail qDetail = QAccountSettingDetail.accountSettingDetail;

    @Override
    public List<AccountSettingRes> getSettingDetails(AccountSettingReq param) {
        List<AccountSettingRes> settingResults = query.select(
            Projections.bean(
                AccountSettingRes.class
               ,qHeader.headerId.as("headerId")
               ,qHeader.mainCategory.as("mainCategory")
               ,qHeader.subCategory.as("subCategory")
               ,qHeader.code.as("headerCode")
               ,qHeader.desc.as("headerDesc")
               ,qDetail.code.as("detailCode")
               ,qDetail.name.as("name")
               ,qDetail.sort.as("sort")
               ,qDetail.enabledFlag.as("enabledFlag")
               ,qDetail.attribute01.as("attribute01")
               ,qDetail.attribute02.as("attribute02")
               ,qDetail.attribute03.as("attribute03")
               ,qDetail.attribute04.as("attribute04")
               ,qDetail.attribute05.as("attribute05")
            )
        )
        .from(qHeader)
        .join(qDetail).on(qHeader.headerId.eq(qDetail.headerId))
        .where(productPredicate(param))
        .fetch();

        return settingResults;
    }

    private BooleanBuilder productPredicate(AccountSettingReq param) {
        BooleanBuilder whereClause = new BooleanBuilder();

        if(StringUtils.isNotNull(param.getMainCategory())) {
            whereClause.and(qHeader.mainCategory.eq(param.getMainCategory()));
        }

        if(StringUtils.isNotNull(param.getSubCategory())) {
            whereClause.and(qHeader.subCategory.eq(param.getSubCategory()));
        }

        return whereClause;
    }
}