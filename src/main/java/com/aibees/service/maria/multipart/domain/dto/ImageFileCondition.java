package com.aibees.service.maria.multipart.domain.dto;

import com.aibees.service.maria.common.utils.StringUtils;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ImageFileCondition extends FileCondition {
    private String category;
    private String ym;
    private Long number;
    private String filename;

    // condition check method
    public boolean categoryCondChk() {
        return !StringUtils.isNull(category);
    }

    public boolean categoryAndYmCondChk() {
        return !((category == null) || (ym == null));
    }

    public boolean filenameCondChk() {
        return !StringUtils.isNull(filename);
    }
}
