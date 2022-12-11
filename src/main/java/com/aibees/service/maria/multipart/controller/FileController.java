package com.aibees.service.maria.multipart.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileController {

    ResponseEntity<Resource> downloadResource(String res);

    ResponseEntity<Void> uploadResource(MultipartFile file, String data);
}
