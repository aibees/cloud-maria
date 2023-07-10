package com.aibees.service.maria.quartz.vo;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class JobVo {
    private String groupCode;
    private String jobCode;
    private String jobName;
    private String className;
    private String cronjob;
    private String desc;
}
