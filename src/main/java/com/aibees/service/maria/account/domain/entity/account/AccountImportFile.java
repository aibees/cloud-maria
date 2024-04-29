package com.aibees.service.maria.account.domain.entity.account;


import com.aibees.service.maria.account.domain.entity.account.pk.AccountImportFileId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
