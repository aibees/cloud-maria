package com.aibees.service.maria.quartz.config;

import com.aibees.service.maria.quartz.vo.JobVo;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

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

        } catch(ClassNotFoundException cnf) {
            cnf.printStackTrace();
        } catch(SchedulerException se) {
            se.printStackTrace();
        }
    }

    private JobDetail createJobDetail(JobVo job) throws ClassNotFoundException {
        JobDataMap dataMap = new JobDataMap();
        // dataMap.putAll(param);
        String jobClassPath = String.join(".", JOB_BASE_PACKAGE, job.getClassName());

        return JobBuilder.newJob((Class<? extends Job>) Class.forName(jobClassPath)).build();
    }

    private Trigger createTrigger(JobVo job) {
        return TriggerBuilder.newTrigger().withSchedule(
                CronScheduleBuilder.cronSchedule(job.getCronjob())
        ).build();
    }
}
