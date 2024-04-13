package com.aibees.service.maria.account.domain.entity.account.pk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccountImportFileId {
    private String fileId;
    private String fileType;
}
