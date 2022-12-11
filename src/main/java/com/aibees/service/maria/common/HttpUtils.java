package com.aibees.service.maria.common;


import com.aibees.service.maria.multipart.dao.vo.ResourceVo;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.aibees.service.maria.common.CONSTANT.COMPRESSED_TYPE;
import static com.aibees.service.maria.common.CONSTANT.DOCUMENT_TYPE;

@Component
public class HttpUtils {

    // CONSTANT
    private String CONTENT_TYPE = "Content-TYpe";

    public HttpHeaders getDownloadHeader(ResourceVo data) {
        HttpHeaders header = new HttpHeaders();
        header.add("Content-Disposition", "attachment; filename=" + data.getFilename());
        header.add("Content-TYpe", "application/octet-stream");

        return header;
    }

    private void setContentType(String filename, HttpHeaders headers) {
        String types = filename.split("\\.")[filename.split("\\.").length-1];

        if(Arrays.asList(COMPRESSED_TYPE).contains(types)) {
            headers.add(CONTENT_TYPE, "application/zip");
        } else if(Arrays.asList(DOCUMENT_TYPE).contains(types)) {
            headers.add(CONTENT_TYPE, "application/octet-stream");
        }
    }
}
