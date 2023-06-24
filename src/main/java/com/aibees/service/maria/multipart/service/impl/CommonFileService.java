package com.aibees.service.maria.multipart.service.impl;

import com.aibees.service.maria.common.StringUtils;
import com.aibees.service.maria.multipart.domain.entity.CommonFileEntity;
import com.aibees.service.maria.multipart.domain.repo.FileStoreRepository;
import com.aibees.service.maria.multipart.domain.vo.FileVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CommonFileService {
    private final String SLASH = "/";
    private final String SLASH_WIN = "\\";

    private final FileStoreRepository fileStoreRepo;

    /**
     * 해당파일 child 리스트를 조회
     * @param fileId
     * @return
     */
    public List<FileVo> getResourceList(String fileId) {
        CommonFileEntity fileData = fileStoreRepo.findOneById(Long.parseLong(fileId));

        log.info("[CommonFileService][getResourceList] fileId=" + fileData.toString());

        return fileStoreRepo.findAllByParentId(fileData.getId())
                .stream().map(entity -> {
                        return new FileVo();
                    }
                ).collect(Collectors.toList());
    }

    /**
     * 파일 metadata 조회
     * @param fileId
     * @return
     */
    public FileVo getFileInfo(String fileId) {
        CommonFileEntity entity = fileStoreRepo.findOneById(Long.parseLong(fileId));

        FileVo data = FileVo.builder()
                            .id(entity.getId())
                            .parentId(entity.getParentId())
                            .fileName(entity.getFilename())
                            .ext(entity.getExt())
                            .createAt(entity.getCreateAt())
                            .build();

        data.setAbsoluePath(this.getFullDirPath(data));
        return data;
    }

    /**
     * 새로운 DIR 생성(mkdir)
     * @param param
     * @return
     */
    public FileVo createDirectory(FileVo param) {
        // 0. 새로운 DIR 만들 위치의 absolute Path를 찾는다.
        FileVo parent = FileVo.convertEntity(fileStoreRepo.findOneById(param.getParentId()));
        parent.setAbsoluePath(this.getFullDirPath(parent));
        String newAbsPath = parent.getAbsoluePath() + "/" + param.getFileName();

        long fileNewId = fileStoreRepo.getMaxId();

        // 1. get newFile Vo
        FileVo newFile = FileVo.builder()
                .id(fileNewId)
                .parentId(param.getParentId())
                .fileName(param.getFileName())
                .ext(param.getExt())
                .absoluePath(newAbsPath)
                .createAt(LocalDateTime.now())
                .build();

        // 2. mkdir process
        String createDir = "";

        if(System.getProperty("os.name").toLowerCase().contains("win")) {
            createDir = "C:\\maria_test".concat(newFile.getAbsoluePath().replace(SLASH, SLASH_WIN));
        } else {
            createDir = "/app/maria".concat(newFile.getAbsoluePath());
        }
        File destFolder = new File(createDir);
        boolean result = destFolder.mkdirs();

        // 3. db insert
        if(result) {
            fileStoreRepo.save(
                    CommonFileEntity
                            .builder()
                            .id(newFile.getId())
                            .parentId(newFile.getParentId())
                            .filename(newFile.getFileName())
                            .ext(newFile.getExt())
                            .alias(newFile.getPathName())
                            .createAt(newFile.getCreateAt())
                            .build());
        }

        return newFile;
    }

    public int deleteDirectory(FileVo param) {
        int result = -99;
        // 0. 삭제할 dir의 absolute Path를 찾는다.

        return result;
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

        // 리턴
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
                                 .fileName(entity.getFilename())
                                 .build()));

        FileVo curFile = file;
        String abPath = SLASH.concat(file.getFileName());

        while(curFile.getId() > 0) { // root 찾을 때까지
            System.out.println("[getFullDirPath] ==> " + curFile.toString() + " // path : " + abPath);
            FileVo parentFile = dirMap.get(curFile.getParentId()); //부모 Filevo 찾기
            if(StringUtils.isNotNull(parentFile.getFileName()))
                abPath = SLASH.concat(parentFile.getFileName()).concat(abPath);

            curFile = parentFile;
        }

        return abPath;
    }
}
