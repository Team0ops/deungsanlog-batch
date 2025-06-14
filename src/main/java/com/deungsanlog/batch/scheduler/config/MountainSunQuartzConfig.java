package com.deungsanlog.batch.scheduler.config;

import com.deungsanlog.batch.scheduler.job.MountainSunQuartzJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MountainSunQuartzConfig {

    private final JobLauncher jobLauncher;

    @Qualifier("mountainSunJob")
    private final Job mountainSunJob;

    @Bean
    public JobDetail mountainSunJobDetail() {
        JobDataMap map = new JobDataMap();
        map.put("jobLauncher", jobLauncher);
        map.put("job", mountainSunJob);

        return JobBuilder.newJob(MountainSunQuartzJob.class)
                .withIdentity("mountainSunJobDetail")
                .setJobData(map)
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger mountainSunJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(mountainSunJobDetail())
                .withIdentity("mountainSunJobTrigger")
                // 매일 자정마다 실행
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))
                .build();
    }

    @Bean
    public JobDataMap jobDataMap() {
        JobDataMap map = new JobDataMap();
        map.put("jobLauncher", jobLauncher);
        map.put("job", mountainSunJob);
        return map;
    }
}