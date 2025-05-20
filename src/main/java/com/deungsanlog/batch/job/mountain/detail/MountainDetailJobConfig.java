package com.deungsanlog.batch.job.mountain.detail;

import com.deungsanlog.batch.job.mountain.detail.dto.MountainDetailCsvRow;
import com.deungsanlog.batch.job.mountain.detail.dto.MountainDetailWrapper;
import com.deungsanlog.batch.job.mountain.detail.processor.MountainDetailProcessor;
import com.deungsanlog.batch.job.mountain.detail.writer.MountainDetailWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class MountainDetailJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final FlatFileItemReader<MountainDetailCsvRow> reader;
    private final MountainDetailProcessor processor;
    private final MountainDetailWriter writer;

    @Bean
    public Job mountainDetailJob() {
        return new JobBuilder("mountainDetailJob", jobRepository)
                .start(mountainDetailStep())
                .build();
    }

    @Bean
    public Step mountainDetailStep() {
        return new StepBuilder("mountainDetailStep", jobRepository)
                .<MountainDetailCsvRow, MountainDetailWrapper>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}