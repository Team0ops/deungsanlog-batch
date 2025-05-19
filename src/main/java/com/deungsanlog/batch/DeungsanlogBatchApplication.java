package com.deungsanlog.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DeungsanlogBatchApplication {
	public static void main(String[] args) {
		SpringApplication.run(DeungsanlogBatchApplication.class, args);
	}
	@Bean
	public CommandLineRunner run(JobLauncher jobLauncher, JobRegistry jobRegistry) {
		return args -> {
			if (args.length == 0) {
				throw new IllegalArgumentException("실행하고 싶은 Job 이름을 파라미터로 넘겨주세요");
			}

			for (String jobName : args) {
				Job job = jobRegistry.getJob(jobName);
				JobParameters jobParameters = new JobParametersBuilder()
						.addLong("timestamp", System.currentTimeMillis())
						.toJobParameters();

				jobLauncher.run(job, jobParameters);
			}
		};
	}
}
