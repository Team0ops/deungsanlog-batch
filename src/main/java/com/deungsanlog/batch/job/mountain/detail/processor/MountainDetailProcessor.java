package com.deungsanlog.batch.job.mountain.detail.processor;

import com.deungsanlog.batch.domain.mountain.entity.Mountain;
import com.deungsanlog.batch.domain.mountain.entity.MountainDescription;
import com.deungsanlog.batch.domain.mountain.repository.MountainDescriptionRepository;
import com.deungsanlog.batch.domain.mountain.repository.MountainRepository;
import com.deungsanlog.batch.job.mountain.detail.dto.MountainDetailCsvRow;
import com.deungsanlog.batch.job.mountain.detail.dto.MountainDetailWrapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import java.util.*;
import org.apache.commons.text.StringEscapeUtils;

@Component
@Slf4j
@RequiredArgsConstructor
public class MountainDetailProcessor implements ItemProcessor<MountainDetailCsvRow, MountainDetailWrapper> {

    private final MountainRepository mountainRepository;
    private final MountainDescriptionRepository mountainDescriptionRepository;

    private List<Mountain> allMountains; // DB 산 목록 전체

    private static final Map<String, String> NAME_FIX_MAP = Map.of(
            "변산", "내변산" //변산의 경우 API마다 이름이 달라 따로 매핑
    );

    // DB 속 데이터 미리 저장
    @PostConstruct
    public void loadAllMountains() {
        this.allMountains = mountainRepository.findAll();
    }

    @Override
    public MountainDetailWrapper process(MountainDetailCsvRow row) {
        String rawCsvName = row.getName().trim();
        String fixedName = NAME_FIX_MAP.getOrDefault(rawCsvName, rawCsvName);
        double csvElevation;

        csvElevation = Double.parseDouble(row.getElevation().trim());

        // normalize 후 비교
        String normCsvName = normalize(fixedName);

        // DB에서 normalize된 이름과 가장 고도 비슷한 산 찾기
        Mountain match = allMountains.stream()
                .filter(m -> normalize(m.getName()).equals(normCsvName))
                .min(Comparator.comparing(m -> Math.abs(m.getElevation() - csvElevation)))
                .orElse(null);

        if (match == null) {
            log.warn("이름 매칭 실패: name={}, elevation={}", fixedName, csvElevation);
            return null;
        }

        match.setLatitude(Double.parseDouble(row.getLatitude().trim()));
        match.setLongitude(Double.parseDouble(row.getLongitude().trim()));

        MountainDescription description = mountainDescriptionRepository.findByMountain_Id(match.getId());

        log.info("원본 row 값 확인 → difficulty: {}, point: {}, course: {}, transport: {}",
                row.getDifficulty(), row.getPoint(), row.getCourse(), row.getTransport());

        String cleanDifficulty = StringEscapeUtils.unescapeHtml4(row.getDifficulty());
        String cleanPoint = StringEscapeUtils.unescapeHtml4(row.getPoint());
        String cleanCourse = StringEscapeUtils.unescapeHtml4(row.getCourse());
        String cleanTransport = StringEscapeUtils.unescapeHtml4(row.getTransport());

        log.info("디코딩 후 값 → difficulty: {}, point: {}, course: {}, transport: {}",
                cleanDifficulty, cleanPoint, cleanCourse, cleanTransport);


        description.setDifficulty(cleanDifficulty);
        description.setHikingPointInfo(cleanPoint);
        description.setHikingCourseInfo(cleanCourse);
        description.setTransportInfo(cleanTransport);

        description.setMountain(match);
        match.setDescription(description);

        return new MountainDetailWrapper(match, description);
    }

    private String normalize(String input) {
        return input.replaceAll("\\(.*?\\)", "")
                .replaceAll("\\s+", "")
                .trim();
    }
}
