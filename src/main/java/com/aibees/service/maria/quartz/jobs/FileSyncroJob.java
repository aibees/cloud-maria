package com.aibees.service.maria.quartz.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FileSyncroJob extends QuartzJobBean {

    private static int interval = 2;
    private int base = 0;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        log.info("execute File syncro Job");

        int data = base + interval;
        log.info("DATA : " + data);
        base = data;
    }
}
