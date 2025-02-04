package com.aibees.service.maria.account.domain.entity.account.pk;

import java.io.Serializable;

import lombok.*;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountImportFileId implements Serializable {
    private String fileId;
    private String fileType;
}
