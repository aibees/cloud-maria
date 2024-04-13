package com.aibees.service.maria.account.service.bank;

import com.aibees.service.maria.account.domain.dto.account.AccountSettingRes;
import com.aibees.service.maria.account.domain.dto.bank.BankStatementReq;
import com.aibees.service.maria.account.domain.entity.account.AccountImportFile;
import com.aibees.service.maria.account.domain.entity.bank.BankStatement;
import com.aibees.service.maria.account.domain.entity.bank.BankStatementTmp;
import com.aibees.service.maria.account.domain.repo.account.ImportFileRepo;
import com.aibees.service.maria.account.domain.repo.bank.BankStatementRepo;
import com.aibees.service.maria.account.domain.repo.bank.BankStatementTmpRepo;
import com.aibees.service.maria.account.service.AccountServiceCommon;
import com.aibees.service.maria.account.utils.constant.AccConstant;
import com.aibees.service.maria.account.util.handler.ExcelParseHandler;
import com.aibees.service.maria.common.vo.ResponseData;
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
public class BankService extends AccountServiceCommon {

    private final ImportFileRepo importFileRepo;
    private final BankStatementTmpRepo statementTmpRepo;
    private final BankStatementRepo statementRepo;

    /*************************
     **** Bank  Statement ****
     *************************/
    public List<BankStatement> getBankStatementList(BankStatementReq params) {
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
            List<BankStatementTmp> bankStatements = (List<BankStatementTmp>) ExcelParseHandler.excelParser(workbook, fileHashName, type).get(AccConstant.CM_RESULT);
            statementTmpRepo.saveAll(bankStatements);

            registFileHashName(fileHashName, file.getOriginalFilename());

            return successResponse(bankStatements);
        } catch (IOException ioe) {
            return failedResponse(ioe);
        } catch (Exception e) {
            e.printStackTrace();
            return failedResponse(e);
        }
    }

    public ResponseEntity<ResponseData> getBankStatementTmpList(String fileHashId) {
        return successResponse(statementTmpRepo.findAllByFileHash(fileHashId));
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
        importFileRepo.save(importFile);
    }
}
