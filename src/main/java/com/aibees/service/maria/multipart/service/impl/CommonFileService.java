package com.aibees.service.maria.multipart.service.impl;

import com.aibees.service.maria.common.MapUtils;
import com.aibees.service.maria.common.StringUtils;
import com.aibees.service.maria.common.domain.entity.ResponseData;
import com.aibees.service.maria.multipart.domain.entity.CommonFileEntity;
import com.aibees.service.maria.multipart.domain.repo.FileStoreRepository;
import com.aibees.service.maria.multipart.domain.vo.FileVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private static final String ABS_PATH_ROUTE = "absPathRoute";
    private static final String ABS_PATH_NAME = "absPathName";

    private final FileStoreRepository fileStoreRepo;

    /**
     * 해당파일 child 리스트를 조회
     * @param fileId
     * @return
     */
    public ResponseEntity<ResponseData> getResourceList(Long fileId) {
        System.out.println("==========getResourceList :: " + fileId);
        CommonFileEntity fileData = fileStoreRepo.findOneById(fileId);

        // Get Child Dir&file List
        List<FileVo> resultList = fileStoreRepo.findAllByParentId(fileData.getId())
                .stream().map(entity -> {

                        FileVo curFile = FileVo.builder()
                                .id(entity.getId())
                                .parentId(entity.getParentId())
                                .fileName(entity.getFilename())
                                .name(StringUtils.isNotNull(entity.getAlias()) ? entity.getAlias() : entity.getFilename())
                                .ext((entity.getExt()))
                                . build();

                        Map<String, Object> paths = getFullDirPathAll(curFile);
                        curFile.setPath(MapUtils.getString(paths, ABS_PATH_ROUTE).replace("/app/maria", ""));
                        curFile.setPathName(MapUtils.getString(paths, ABS_PATH_NAME));

                        return curFile;
                    }
                ).collect(Collectors.toList());

        Comparator<FileVo> extReverse = Comparator.comparing(FileVo::getExt);
        resultList.sort(extReverse.thenComparing(FileVo::getName));

        FileVo toBefore = FileVo.builder()
                .id(fileData.getParentId())
                .fileName("..")
                .name("..")
                .ext("dir")
                .build();

        if(fileId != 0) {
            resultList.add(0, toBefore);
        }

        // Get Absolute Path
        FileVo curFile = new FileVo();
        curFile.setId(fileData.getId());
        curFile.setParentId(fileData.getParentId());
        curFile.setFileName(fileData.getFilename());

        Map<String, Object> result = getFullDirPathAll(curFile);
        result.put("childList", resultList);

        return ResponseEntity.ok().body(
                ResponseData.builder().data(result).build()
        );
    }

    /**
     * 파일 metadata 조회
     * @param fileId
     * @return
     */
    public FileVo getFileInfo(String fileId) {
        System.out.println("fileId : " + fileId);
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
    public ResponseEntity<ResponseData> createDirectory(FileVo param) {
        try {
            // 0. 새로운 DIR 만들 위치의 absolute Path를 찾는다.
            FileVo parent = FileVo.convertEntity(fileStoreRepo.findOneById(param.getParentId()));
            System.out.println("=======parent File Vo : " + parent.toString());
            parent.setAbsoluePath(this.getFullDirPath(parent));
            String newAbsPath = parent.getAbsoluePath() + "/" + param.getFileName();

            long fileNewId = fileStoreRepo.getMaxId();

            // 1. get newFile Vo
            FileVo newFile = FileVo.builder()
                    .id(fileNewId)
                    .parentId(param.getParentId())
                    .fileName(param.getFileName())
                    .name(param.getName())
                    .ext("dir")
                    .absoluePath(newAbsPath)
                    .createAt(LocalDateTime.now())
                    .build();

            // 2. mkdir process
            String createDir = "";

            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                createDir = "C:\\maria_test".concat(newFile.getAbsoluePath().replace(SLASH, SLASH_WIN));
            } else {
                createDir = "/app/maria".concat(newFile.getAbsoluePath());
            }
            File destFolder = new File(createDir);
            System.out.println(destFolder.isDirectory());
            boolean result = destFolder.mkdirs();

            System.out.println(createDir + " / File mkdirs result : " + result);

            // 3. db insert
            if (result || destFolder.exists()) {
                fileStoreRepo.save(
                        CommonFileEntity
                                .builder()
                                .id(newFile.getId())
                                .parentId(newFile.getParentId())
                                .parentName(parent.getFileName())
                                .filename(newFile.getFileName())
                                .ext(newFile.getExt())
                                .alias(newFile.getName())
                                .createAt(newFile.getCreateAt())
                                .build());
            }

            return ResponseEntity.ok().body(
                    ResponseData.builder().message("SUCCESS").build()
            );
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ResponseData.builder().message("FAILED").build()
            );
        }
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
        String path = MapUtils.getString(getFullDirPathAll(file), ABS_PATH_ROUTE);
        return path.replace("/app/maria", "");
    }

    private String getFullDirPathName(FileVo file) {
        return MapUtils.getString(getFullDirPathAll(file), ABS_PATH_NAME);
    }

    private Map<String, Object> getFullDirPathAll(FileVo file) {
        Map<String, Object> result = new HashMap<>();

        // directory 는 전부 가져온다.
        List<CommonFileEntity> dirList = fileStoreRepo.findAllByExt("dir");
        Map<Long, FileVo> dirMap = new HashMap<>();

        dirList.parallelStream().forEach(
            entity -> {
                FileVo data = new FileVo();
                data.setId(entity.getId());
                data.setParentId(entity.getParentId());
                data.setFileName(entity.getFilename());

                dirMap.put(entity.getId(), data);
            }
        );

        FileVo curFile = file;
        String pathRoute = SLASH.concat(file.getFileName());
        String pathName = SLASH.concat((file.getFileName()));

        while(curFile.getId() > 0) { // root 찾을 때까지
            FileVo parentFile = dirMap.get(curFile.getParentId()); //부모 Filevo 찾기

            if(StringUtils.isNotNull(parentFile.getFileName()))
                pathRoute = SLASH.concat(parentFile.getFileName()).concat(pathRoute);

            if(StringUtils.isNotNull(parentFile.getName())) {
                pathName = SLASH.concat(parentFile.getName()).concat(pathName);
            } else {
                pathName = SLASH.concat(parentFile.getFileName()).concat(pathName);
            }

            curFile = parentFile;
        }

        result.put(ABS_PATH_ROUTE, pathRoute);
        result.put(ABS_PATH_NAME, pathName);

        return result;
    }
}
