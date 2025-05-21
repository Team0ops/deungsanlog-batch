package com.deungsanlog.batch.job.mountain.sun;

import com.deungsanlog.batch.domain.mountain.entity.MountainSunInfo;
import com.deungsanlog.batch.job.mountain.sun.dto.MountainSunRow;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class MountainSunJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final ItemReader<MountainSunRow> mountainSunItemReader;
    private final ItemProcessor<MountainSunRow, List<MountainSunInfo>> mountainSunProcessor;
    private final ItemWriter<List<MountainSunInfo>> mountainSunWriter;

    @Bean
    public Job mountainSunJob() {
        return new JobBuilder("mountainSunJob", jobRepository)
                .start(fetchAndSaveSunInfoStep())
                .build();
    }

    @Bean
    public Step fetchAndSaveSunInfoStep() {
        return new StepBuilder("fetchAndSaveSunInfoStep", jobRepository)
                .<MountainSunRow, List<MountainSunInfo>>chunk(10, transactionManager)
                .reader(mountainSunItemReader)
                .processor(mountainSunProcessor)
                .writer(mountainSunWriter)
                .build();
    }
}