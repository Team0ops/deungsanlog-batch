package com.deungsanlog.batch.job.mountain.sun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
// mountain 테이블 컬럼 중 API 호출에 필요한 정보를 담은 객체
public class MountainSunRow {
    private Long mountainId;
    private double latitude;
    private double longitude;
}
