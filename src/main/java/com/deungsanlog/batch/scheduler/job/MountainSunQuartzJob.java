package com.deungsanlog.batch.scheduler.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;

@Slf4j
public class MountainSunQuartzJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            JobLauncher jobLauncher = (JobLauncher) context.getJobDetail().getJobDataMap().get("jobLauncher");
            Job job = (Job) context.getJobDetail().getJobDataMap().get("job");

            jobLauncher.run(job, new JobParametersBuilder()
                    .addString("runTime", LocalDateTime.now().toString())
                    .toJobParameters());

            log.info("[Quartz 실행] MountainSunJob 실행 완료");
        } catch (Exception e) {
            log.error("[Quartz 실행 실패] {}", e.getMessage());
            throw new JobExecutionException(e);
        }
    }
}
