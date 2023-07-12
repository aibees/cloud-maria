package com.aibees.service.maria.quartz.config;

import com.aibees.service.maria.quartz.vo.JobVo;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ScheduleConfiguration {

    @Autowired
    private Scheduler scheduler;

    private final String JOB_BASE_PACKAGE = "com.aibees.service.maria.quartz.jobs";

    @PostConstruct
    public void scheduleRun() {

        List<JobVo> jobList = new ArrayList<>();
        jobList.add(
                JobVo.builder()
                        .groupCode("MARIA")
                        .jobCode("FILE_SYNC")
                        .jobName("Maria File Syncro Job")
                        .cronjob("0/5 * * * * ?")
                        .className("FileSyncroJob")
                        .build()
        );

        try {
            for(JobVo job : jobList) {
                JobDetail detail = createJobDetail(job);
                Trigger trigger = createTrigger(job);

                scheduler.scheduleJob(detail, trigger);
            }

        } catch(ClassNotFoundException | SchedulerException cnf) {
            cnf.printStackTrace();
        }
    }

    private JobDetail createJobDetail(JobVo job) throws ClassNotFoundException {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("year", 2022);
        dataMap.put("month", 11);
        dataMap.put("day", 28);
        // dataMap.putAll(param);
        String jobClassPath = String.join(".", JOB_BASE_PACKAGE, job.getClassName());

        return JobBuilder.newJob((Class<? extends Job>) Class.forName(jobClassPath))
                .setJobData(dataMap)
                .build();
    }

    private Trigger createTrigger(JobVo job) {
        return TriggerBuilder.newTrigger().withSchedule(
                CronScheduleBuilder.cronSchedule(job.getCronjob())
        ).build();
    }
}
