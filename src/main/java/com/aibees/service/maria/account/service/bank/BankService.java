package com.aibees.service.maria.account.service.bank;

import com.aibees.service.maria.account.domain.dto.bank.BankStatementReq;
import com.aibees.service.maria.account.service.AccountServiceCommon;
import com.aibees.service.maria.common.vo.ResponseData;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BankService extends AccountServiceCommon {

    /*************************
     **** Bank  Statement ****
     *************************/
    public ResponseEntity<ResponseData> getBankStatementList(BankStatementReq params) {
        return null;
    }
}
