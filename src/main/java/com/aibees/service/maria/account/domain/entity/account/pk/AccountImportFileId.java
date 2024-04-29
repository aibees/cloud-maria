package com.aibees.service.maria.account.domain.entity.account.pk;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccountImportFileId implements Serializable {
    private String fileId;
    private String fileType;
}
