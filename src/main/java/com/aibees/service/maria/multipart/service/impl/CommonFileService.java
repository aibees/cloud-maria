package com.aibees.service.maria.multipart.service.impl;

import com.aibees.service.maria.common.StringUtils;
import com.aibees.service.maria.multipart.domain.entity.CommonFileEntity;
import com.aibees.service.maria.multipart.domain.repo.FileStoreRepository;
import com.aibees.service.maria.multipart.domain.vo.FileVo;
import com.aibees.service.maria.multipart.domain.vo.ResourceVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommonFileService {
    private final String SLASH = "/";
    private final String SLASH_WIN = "\\";

    private final FileStoreRepository fileStoreRepo;

    public List<FileVo> getResourceList(String fileId) {
        CommonFileEntity fileData = fileStoreRepo.findOneById(Long.parseLong(fileId));

        System.out.println("[CommonFileService][getResourceList]fileId=" + fileData.toString());

        return fileStoreRepo.findAllByParentId(fileData.getId())
                .stream().map(entity ->
                        FileVo.builder()
                              .id(entity.getId())
                                .parentId(entity.getParentId())
                                .filename(entity.getFilename())
                                .ext(entity.getExt())
                                .createAt(entity.getCreateAt())
                                .build()
                ).collect(Collectors.toList());
    }

    public FileVo getFileInfo(String fileId) {
        CommonFileEntity entity = fileStoreRepo.findOneById(Long.parseLong(fileId));

        FileVo data = FileVo.builder()
                            .id(entity.getId())
                            .parentId(entity.getParentId())
                            .filename(entity.getFilename())
                            .ext(entity.getExt())
                            .createAt(entity.getCreateAt())
                            .build();

        data.setAbsoluePath(this.getFullDirPath(data));
        return data;
    }

    public FileVo createDirectory(FileVo param) {
        // 0. select FileVos from DB to find absolute path;
        FileVo parent = FileVo.convertEntity(fileStoreRepo.findOneById(param.getParentId()));
        parent.setAbsoluePath(this.getFullDirPath(parent));
        String newAbsPath = parent.getAbsoluePath() + "/" + param.getFilename();

        long fileNewId = fileStoreRepo.getMaxId();

        // 1. get newFile Vo
        FileVo newFile = FileVo.builder()
                .id(fileNewId)
                .parentId(param.getParentId())
                .filename(param.getFilename())
                .ext(param.getExt())
                .absoluePath(newAbsPath)
                .createAt(LocalDateTime.now())
                .build();

        // 2. mkdir process
        String createDir = "";

        if(System.getProperty("os.name").toLowerCase().contains("win")) {
            createDir = "C:\\maria_test".concat(newFile.getAbsoluePath().replace(SLASH, SLASH_WIN));
        } else {
            createDir = "/app/maria";
            createDir = "/app/maria".concat(newFile.getAbsoluePath());
        }
        File destFolder = new File(createDir);
        boolean result = destFolder.mkdirs();

        // 3. db insert
        if(result) {
            fileStoreRepo.save(CommonFileEntity.convert(newFile));
        }

        return newFile;
    }

    public Map<String, Object> getParentFileList(String fileId) {
        Map<String, Object> data = new HashMap<>();
        List<CommonFileEntity> childList = null;
        // 1. getParentId
        // 자기 데이터 구하고
        CommonFileEntity file = fileStoreRepo.findOneById(Long.parseLong(fileId));
        System.out.println("file : " + file.toString());

        // 자기데이터에서 알아낸 parent id로 parent 데이터 받고
        CommonFileEntity parent = fileStoreRepo.findOneById(file.getParentId());
        data.put("parent", parent);
        System.out.println("parent : " + parent);

        // parent에 속한 파일 리스트 받고
        if(!Objects.isNull(parent))
            childList =
                Optional.ofNullable(fileStoreRepo.findAllByParentId(parent.getId())).orElse(null);
        data.put("list", childList);

        return data;
    }

    private String getFullDirPath(FileVo file) {
        Map<Long, FileVo> dirMap = new HashMap<>();
        fileStoreRepo.findAllByExt("dir")
                .parallelStream().forEach(entity ->
                dirMap.put(entity.getId(),
                           FileVo.builder()
                                 .id(entity.getId())
                                 .parentId(entity.getParentId())
                                 .filename(entity.getFilename())
                                 .build()));

        FileVo curFile = file;
        String abPath = SLASH.concat(file.getFilename());

        while(curFile.getId() > 0) { // root 찾을 때까지
            System.out.println("[getFullDirPath] ==> " + curFile.toString() + " // path : " + abPath);
            FileVo parentFile = dirMap.get(curFile.getParentId()); //부모 Filevo 찾기
            if(StringUtils.isNotNull(parentFile.getFilename()))
                abPath = SLASH.concat(parentFile.getFilename()).concat(abPath);

            curFile = parentFile;
        }

        return abPath;
    }
}
