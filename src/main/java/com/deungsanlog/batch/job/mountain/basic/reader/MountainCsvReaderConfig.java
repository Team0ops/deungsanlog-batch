package com.deungsanlog.batch.job.mountain.basic.reader;

import com.deungsanlog.batch.job.mountain.basic.dto.CsvFilterItem;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class MountainCsvReaderConfig {

    @Bean
    public FlatFileItemReader<CsvFilterItem> mountainCsvReader() {
        return new FlatFileItemReaderBuilder<CsvFilterItem>()
                .name("mountainCsvReader")
                .resource(new ClassPathResource("filters/mountain_list.csv"))
                .delimited()
                .names("name", "location") // CsvFilterItem 필드명과 매핑
                .targetType(CsvFilterItem.class)
                .linesToSkip(1) // 헤더 무시
                .build();
    }
}
