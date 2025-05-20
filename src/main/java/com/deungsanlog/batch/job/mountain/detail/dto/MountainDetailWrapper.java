package com.deungsanlog.batch.job.mountain.detail.dto;

import com.deungsanlog.batch.domain.mountain.entity.Mountain;
import com.deungsanlog.batch.domain.mountain.entity.MountainDescription;
import lombok.Getter;

@Getter
public class MountainDetailWrapper {
    private Mountain mountain;
    private MountainDescription mountainDescription;

    public MountainDetailWrapper(Mountain mountain, MountainDescription description) {
        this.mountain = mountain;
        this.mountainDescription = description;
    }
}