package com.deungsanlog.batch.job.mountain.detail.dto;

import lombok.Data;

// CSV 한 줄 매핑용, 파일 컬럼 순서와 꼭 맞추기
@Data
public class MountainDetailCsvRow {
    private String name;
    private String location;
    private String elevation;
    private String difficulty; // 난이도
    private String feature;
    private String summary;
    private String point; // 산행 포인트
    private String course; // 산행 코스
    private String transport; // 교통 정보
    private String latitude;  // 위도 (Y좌표)
    private String longitude; // 경도 (X좌표)
}
