package com.aibees.service.maria.account.domain.repo.system;

import com.aibees.service.maria.account.domain.dto.system.PresetMasterReq;
import com.aibees.service.maria.account.domain.entity.system.PresetMaster;
import com.aibees.service.maria.account.domain.entity.system.QPresetMaster;
import com.aibees.service.maria.common.utils.StringUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemPresetRepo extends JpaRepository<PresetMaster, String>, PresetRepoCustom {

    List<PresetMaster> findByPresetCdContainingOrPresetNmContaining(String presetCd, String presetNm);
}

interface PresetRepoCustom {
    List<PresetMaster> getPresetMasterByCondition(PresetMasterReq param);
}

@AllArgsConstructor
class PresetRepoCustomImpl implements  PresetRepoCustom {

    private final JPAQueryFactory query;
    private static final QPresetMaster qPresetmaster = QPresetMaster.presetMaster;

    @Override
    public List<PresetMaster> getPresetMasterByCondition(PresetMasterReq param) {
        return query.selectFrom(qPresetmaster)
            .where(whereClause(param))
            .fetch();
    }

    private BooleanBuilder whereClause(PresetMasterReq param) {
        BooleanBuilder where = new BooleanBuilder();
        if (StringUtils.isEquals(param.getSearchType(), "popup")) {
            popupSearchOption(where, param);
        } else {

        }


        return where;
    }

    private void searchOption(BooleanBuilder where, PresetMasterReq param) {
        if (StringUtils.isNull(param.getPresetCd())) {

        }
    }

    private void popupSearchOption(BooleanBuilder where, PresetMasterReq param) {
        where.and(
            qPresetmaster.presetCd.contains(param.getPresetCd() == null ? StringUtils.EMPTY : param.getPresetCd())
                .or(qPresetmaster.presetNm.contains(param.getPresetNm() == null ? StringUtils.EMPTY : param.getPresetNm()))
        );
    }
}