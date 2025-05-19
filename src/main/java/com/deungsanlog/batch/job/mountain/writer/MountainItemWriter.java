package com.deungsanlog.batch.job.mountain.writer;

import com.deungsanlog.batch.domain.mountain.entity.Mountain;
import com.deungsanlog.batch.domain.mountain.repository.MountainRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MountainItemWriter implements ItemWriter<Mountain> {

    private final MountainRepository mountainRepository;

    @Override
    public void write(Chunk<? extends Mountain> items) {
        for (Mountain mountain : items) {
            mountainRepository.save(mountain);
            log.info("저장 완료: {}", mountain.getName());
        }
    }
}
