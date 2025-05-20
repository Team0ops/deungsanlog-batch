package com.deungsanlog.batch.job.mountain.detail.reader;

import com.deungsanlog.batch.job.mountain.detail.dto.MountainDetailCsvRow;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class MountainDetailReader {

    private static final String[] FIELD_NAMES = {
            // CSV와 매핑할 필드 선언
            "name", "location", "elevation", "difficulty", "feature", "summary",
            "point", "course", "transport", "latitude", "longitude"
    };

    @Bean
    public FlatFileItemReader<MountainDetailCsvRow> mountainDetailItemReader() {
        return new FlatFileItemReaderBuilder<MountainDetailCsvRow>()
                .name("mountainDetailItemReader")
                .resource(new ClassPathResource("batch/input/mountain_details.csv"))
                .encoding("UTF-8")
                .linesToSkip(1)
                .lineMapper(new DefaultLineMapper<>() {{
                    setLineTokenizer(new DelimitedLineTokenizer() {{
                        setNames(FIELD_NAMES);
                        setDelimiter(",");
                        setQuoteCharacter('"');
                    }});
                    setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                        setTargetType(MountainDetailCsvRow.class);
                    }});
                }})
                .build();
    }
}
