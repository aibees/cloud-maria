package com.aibees.service.maria.account.domain.entity.account;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.aibees.service.maria.account.domain.entity.account.pk.AccountImportFileId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name="account_import_file")
@IdClass(AccountImportFileId.class)
public class AccountImportFile {
    @Id
    private String fileId;
    @Id
    private String fileType;
    private String fileName;
}
