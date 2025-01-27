package com.aibees.service.maria.account.service.bank;

import com.aibees.service.maria.account.domain.dto.bank.BankStatementReq;
import com.aibees.service.maria.account.domain.entity.account.AccountImportFile;
import com.aibees.service.maria.account.domain.entity.bank.BankStatement;
import com.aibees.service.maria.account.domain.repo.bank.BankStatementRepo;
import com.aibees.service.maria.account.utils.constant.AccConstant;
import com.aibees.service.maria.account.utils.handler.ExcelParseHandler;
import com.aibees.service.maria.common.utils.StringUtils;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.common.service.ServiceCommon;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.aibees.service.maria.account.utils.constant.AccConstant.IMPORT_BANK;

@Service
@AllArgsConstructor
public class BankService extends ServiceCommon {

//    private final ImportFileRepo importFileRepo;
    private final BankStatementRepo statementRepo;

    /*************************
     **** Bank  Statement ****
     *************************/
    public List<BankStatement> getBankStatementList(BankStatementReq params) {
        checkParams(params);
        return statementRepo.getBankStatementListByCondition(params);
    }

    public ResponseEntity<ResponseData> excelParse(String type, MultipartFile file) {
        try {
            if (Objects.isNull(file)) {
                return failedResponse(new Exception("엑셀파일이 없습니다."));
            }

            Workbook workbook = null;
            String[] fileName = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
            String fileHashName = this.createFileNameHash();

            if (fileName[fileName.length - 1].equals("xlsx")) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else if (fileName[fileName.length - 1].equals("xls")) {
                workbook = new HSSFWorkbook(file.getInputStream());
            }

            if (workbook == null) {
                throw new Exception("workBook is Null");
            }

            registFileHashName(fileHashName, file.getOriginalFilename());

            return null;
        } catch (IOException ioe) {
            return failedResponse(ioe);
        } catch (Exception e) {
            e.printStackTrace();
            return failedResponse(e);
        }
    }

    public ResponseEntity<ResponseData> saveBankStatement() {

        // save statement

        // delete statement tmp
        return successResponse(null);
    }

    private void registFileHashName(String fileHash, String originName) {

        AccountImportFile importFile = AccountImportFile
                .builder()
                .fileId(fileHash)
                .fileType(IMPORT_BANK)
                .fileName(originName)
                .build();
//        importFileRepo.save(importFile);
    }

    private void checkParams(BankStatementReq stateParam) {
        if(StringUtils.isNull(stateParam.getYmdFrom())) {
            stateParam.setYmdFrom("19000101");
        }

        if(StringUtils.isNull(stateParam.getYmdTo())) {
            stateParam.setYmdTo("29991231");
        }

        if(Objects.isNull(stateParam.getAmountFrom())) {
            stateParam.setAmountFrom(0L);
        }

        if(Objects.isNull(stateParam.getAmountTo())) {
            stateParam.setAmountTo(999999999L);
        }
    }
}
