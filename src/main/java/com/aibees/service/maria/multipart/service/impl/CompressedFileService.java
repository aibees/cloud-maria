package com.aibees.service.maria.multipart.service.impl;

import com.aibees.service.maria.common.utils.StringUtils;
import com.aibees.service.maria.multipart.domain.dto.FileCondition;
import com.aibees.service.maria.multipart.domain.repo.MetadataRepository;
import com.aibees.service.maria.multipart.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
@AllArgsConstructor
public class CompressedFileService implements FileService {

    private final MetadataRepository metadataRepository;

    @Override
    public Resource getResource(FileCondition param) throws IOException {
        return null;
    }

    @Override
    public String getUUID(String types) {
        // form : type + date + number
        // ex) DOC 202212 000001 -> 15 byte
        // DOC / IMG / ZIP...
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        String param = types + today.getYear() + today.getMonth().getValue(); // ex) ZIP202212
        String fileid_max = metadataRepository.findMetadataByFilename(param);

        String newUUID;
        if(StringUtils.isNull(fileid_max)) {
            // param으로 준 코드에 해당하는 id가 없다(2022 12월에 zip 파일 넣은 적 없다.)
            newUUID = param.concat("000001");
        } else {
            newUUID = param.concat(
                    StringUtils.UuidNumberFormat(
                            Integer.parseInt(fileid_max.substring(9, 14)) + 1
                    )
            );
        }

        System.out.println(newUUID);
        return newUUID;
    }
}
