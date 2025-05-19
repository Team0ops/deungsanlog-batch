package com.deungsanlog.batch.job.mountain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

// API 응답용
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true) //API값 반환값 없어도 냅두고 넘어감
public class MountainDto {
    private String mntnid;               // 산 코드 → mountains.external_id
    private String mntnnm;               // 산명 → mountains.name
    private String mntninfopoflc;        // 소재지 → mountains.location
    private Integer mntninfohght;        // 산 고도 → mountains.elevation
    private String mntnattchimageseq;    // 대표 이미지 → mountains.thumbnail_img_url

    private String mntnsbttlinfo;        // 부제 → mountain_descriptions.summary
    private String mntninfodscrt;        // 개관 → mountain_descriptions.full_description
    private String crcmrsghtnginfodscrt; // 주변 관광 정보 → mountain_descriptions.nearby_tour_info
}