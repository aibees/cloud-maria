package com.aibees.service.maria.account.domain.dto.bank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BankImportReq {
    private String bankId;
    private String fileHash;
    private MultipartFile file;
    private List<BankImportRes> data;
}
