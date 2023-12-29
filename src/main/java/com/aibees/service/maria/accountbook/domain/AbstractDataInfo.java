package com.aibees.service.maria.accountbook.domain;

import com.aibees.service.maria.common.StringUtils;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DataInfo 구현체에서 공통으로 사용되는 Method 생성
 * 공통적으로 구현되어야 하는 method를 abstract 선언
 *
 */

@SuperBuilder
public abstract class AbstractDataInfo {

    // types
    protected String type;
    protected TRANSFER_TYPE trx_type;

    protected AbstractDataInfo(String type, TRANSFER_TYPE trx_type) {
        this.type = type;
        this.trx_type = trx_type;
    }

    public enum TRANSFER_TYPE {
        TO_MAIN,
        TO_EXCEL,
        TO_SMS,
        TO_INFO
    }

    /**
     * tmp 테이블에서 파일 단위로 조회하기 위한 임시 Hash Value
     * @return String
     */
    protected String createFileNameHash() {
        return LocalDateTime
                .now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                .concat(StringUtils.getRandomStr(4));
    }

    public boolean transferData() throws Exception {
        if(TRANSFER_TYPE.TO_EXCEL.equals(this.trx_type)) {
            return transferToExcelTmp();
        } else if(TRANSFER_TYPE.TO_MAIN.equals(this.trx_type)) {
            return transferToMain();
        } else if(TRANSFER_TYPE.TO_SMS.equals(this.trx_type)) {
            return transferToSMS();
        } else if(TRANSFER_TYPE.TO_INFO.equals(this.trx_type)) {
            return transferToInfo();
        } else {
            return false;
        }
    }

    protected abstract boolean transferToExcelTmp() throws Exception;

    protected abstract boolean transferToMain() throws Exception;

    protected abstract boolean transferToSMS() throws Exception;

    protected abstract boolean transferToInfo() throws Exception;
}
