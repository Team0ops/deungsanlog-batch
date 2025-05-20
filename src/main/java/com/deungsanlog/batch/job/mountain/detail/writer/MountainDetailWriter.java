package com.deungsanlog.batch.job.mountain.detail.writer;

import com.deungsanlog.batch.domain.mountain.entity.Mountain;
import com.deungsanlog.batch.domain.mountain.entity.MountainDescription;
import com.deungsanlog.batch.domain.mountain.repository.MountainDescriptionRepository;
import com.deungsanlog.batch.domain.mountain.repository.MountainRepository;
import com.deungsanlog.batch.job.mountain.detail.dto.MountainDetailWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Component
@RequiredArgsConstructor
public class MountainDetailWriter implements ItemWriter<MountainDetailWrapper> {

    private final MountainRepository mountainRepository;
    private final MountainDescriptionRepository mountainDescriptionRepository;

    @Override
    @Transactional
    public void write(Chunk<? extends MountainDetailWrapper> chunk) {
        for (MountainDetailWrapper wrapper : chunk) {
            Mountain mountain = wrapper.getMountain();
            MountainDescription description = wrapper.getMountainDescription();

            mountainRepository.save(mountain);
            mountainDescriptionRepository.saveAndFlush(description);


            log.info("!! 저장 완료: {} !!", mountain.getName());
        }
    }
}
