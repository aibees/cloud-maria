package com.aibees.service.maria.quartz.jobs;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

@Component
@Slf4j
public class FileSyncroJob extends QuartzJobBean {

    private static int interval = 2;
    private int base = 0;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        log.info("execute File syncro Job");

        JobDataMap jobParam = context.getJobDetail().getJobDataMap();
        if(!jobParam.isEmpty())
            log.info("jobParam => " + String.join(", ", jobParam.getKeys()));


        String rootDir = "C:\\maria_test";

        Set<String> fileList = new HashSet<>();
        Files.walkFileTree(Paths.get(rootDir), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (!Files.isDirectory(file)) {
                    fileList.add(file.toAbsolutePath().toString());


//                    System.out.println(file.toString());
//                    Map<String, Object> at = Files.readAttributes(file, "*");
//                    at.keySet().forEach(k -> {
//                        System.out.println("[" + k + "] => " + at.get(k));
//                    });
//                    fileList.add(file.getFileName().toString());
                }
                return FileVisitResult.CONTINUE;
            }
        });

        String[] allowedExt = {"jpg", "jpeg", "png"};

        fileList.forEach(filePath -> {
            String[] splitedPath = filePath.split("\\\\");
            String fileName = splitedPath[splitedPath.length-1];
            String ext = fileName.split("\\.")[1];

            if(!Arrays.asList(allowedExt).contains(ext)) {
                return;
            }
        });
    }
}
