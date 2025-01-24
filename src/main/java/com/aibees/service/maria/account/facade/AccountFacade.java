package com.aibees.service.maria.account.facade;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aibees.service.maria.account.domain.dto.account.ImportFileRes;
import com.aibees.service.maria.account.service.bank.BankService;
import com.aibees.service.maria.account.utils.constant.AccConstant;
import com.aibees.service.maria.common.utils.StringUtils;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountFacade {

    private final BankService bankService;

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
            List<ImportFileRes> fileNameList = null;
//                importFileRepo.findAll()
//                .stream()
//                .filter(file -> StringUtils.isEquals(file.getFileType(), type))
//                .map(file -> ImportFileRes
//                    .builder()
//                    .fileId(file.getFileId())
//                    .fileName(file.getFileName())
//                    .build())
//                .collect(Collectors.toList());

            return ResponseEntity.ok(
                ResponseData.builder()
                    .data(fileNameList)
                    .build()
            );
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ResponseData.builder()
                    .data(e.getMessage())
                    .build()
            );
        }
    }
}
