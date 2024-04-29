package com.aibees.service.maria.account.facade;

import com.aibees.service.maria.account.domain.dto.account.ImportFileRes;
import com.aibees.service.maria.account.domain.repo.account.ImportFileRepo;
import com.aibees.service.maria.account.service.bank.BankService;
import com.aibees.service.maria.account.utils.constant.AccConstant;
import com.aibees.service.maria.common.StringUtils;
import com.aibees.service.maria.common.vo.ResponseData;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountFacade {

    private final BankService bankService;
    private final ImportFileRepo importFileRepo;

    public ResponseEntity<ResponseData> excelParse(String type, MultipartFile file) {

        if (type.endsWith(AccConstant.IMPORT_BANK)) {
            return bankService.excelParse(type, file);
        } else { // IMPORT_CARD
            return null;
        }
    }

    /**
     * 현재 import file 리스트 이름으로 전체조회
     * @return
     */
    public ResponseEntity<ResponseData> getImportFileNameList(String type) {
        try {
            List<ImportFileRes> fileNameList = importFileRepo.findAll()
                .stream()
                .map(file -> ImportFileRes
                    .builder()
                    .fileId(file.getFileId())
                    .fileName(file.getFileName())
                    .build())
                .collect(Collectors.toList());

            return ResponseEntity.ok(
                ResponseData.builder()
                    .data(fileNameList)
                    .message(AccConstant.CM_SUCCESS).build()
            );
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ResponseData.builder()
                    .message(AccConstant.CM_FAILED)
                    .data(e.getMessage())
                    .build()
            );
        }
    }

    public ResponseEntity<ResponseData> getStatementTmpByFileHash(String type, String hashId) {
        if(StringUtils.isEquals(type, AccConstant.IMPORT_BANK)) {
            return bankService.getBankStatementTmpList(hashId);
        } else {
            return null;
        }
    }
}
