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
				// 명령줄로 실행한 게 아니면 (Quartz 등 자동 실행을 위해) 그냥 무시
				System.out.println("[CommandLineRunner] Job 인자 없음 → 실행하지 않고 패스");
				return;
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
