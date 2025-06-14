package com.deungsanlog.batch.job.mountain.sun.writer;

import com.deungsanlog.batch.domain.mountain.entity.MountainSunInfo;
import com.deungsanlog.batch.domain.mountain.repository.MountainSunInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MountainSunWriter implements ItemWriter<List<MountainSunInfo>> {

    private final MountainSunInfoRepository repository;

    @Override
    public void write(Chunk<? extends List<MountainSunInfo>> chunk){
        List<MountainSunInfo> flatList = chunk.getItems().stream()
                .flatMap(List::stream)
                .toList();

        repository.saveAll(flatList);
        log.info("[일출/일몰 저장] {}건 저장 완료", flatList.size());
    }
}
