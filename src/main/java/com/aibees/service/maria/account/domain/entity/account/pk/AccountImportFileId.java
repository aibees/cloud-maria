package com.aibees.service.maria.account.domain.entity.account.pk;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccountImportFileId implements Serializable {
    private String fileId;
    private String fileType;
}
