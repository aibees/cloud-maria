package com.aibees.service.maria.multipart.dao.obj;

import com.aibees.service.maria.common.StringUtils;

public class CompressFileCondition implements FileCondition {
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
