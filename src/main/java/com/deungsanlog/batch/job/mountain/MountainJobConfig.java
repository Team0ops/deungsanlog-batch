package com.deungsanlog.batch.job.mountain;

import com.deungsanlog.batch.domain.mountain.entity.Mountain;
import com.deungsanlog.batch.job.mountain.dto.CsvFilterItem;
import com.deungsanlog.batch.job.mountain.processor.MountainApiProcessor;
import com.deungsanlog.batch.job.mountain.writer.MountainItemWriter;
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
public class MountainJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final FlatFileItemReader<CsvFilterItem> reader;
    private final MountainApiProcessor processor;
    private final MountainItemWriter writer;

    @Bean
    public Job mountainInfoFetchJob() {
        return new JobBuilder("mountainInfoFetchJob", jobRepository)
                .start(mountainStep())
                .build();
    }

    @Bean
    public Step mountainStep() {
        return new StepBuilder("mountainStep", jobRepository)
                .<CsvFilterItem, Mountain>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}