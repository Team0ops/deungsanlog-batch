package com.deungsanlog.batch.job.mountain.sun.processor;

import com.deungsanlog.batch.domain.mountain.entity.Mountain;
import com.deungsanlog.batch.domain.mountain.entity.MountainSunInfo;
import com.deungsanlog.batch.domain.mountain.repository.MountainRepository;
import com.deungsanlog.batch.domain.mountain.repository.MountainSunInfoRepository;
import com.deungsanlog.batch.job.mountain.sun.dto.MountainSunRow;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MountainSunProcessor implements ItemProcessor<MountainSunRow, List<MountainSunInfo>> {

    private final MountainRepository mountainRepository;
    private final MountainSunInfoRepository mountainSunInfoRepository;
    private final RestTemplate restTemplate;

    @Value("${publicdata.api.key.encoded}")
    private String apiKey;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private boolean firstRun = true;

    @Override
    @Transactional
    public List<MountainSunInfo> process(MountainSunRow row) {
        Mountain mountain = mountainRepository.findById(row.getMountainId())
                .orElseThrow(() -> new IllegalArgumentException("산 정보 없음: id = " + row.getMountainId()));

        if (firstRun) {
            log.info("[초기화] 기존 mountain_sun_info 전체 삭제");
            mountainSunInfoRepository.truncateTable();
            firstRun = false;
        }

        List<MountainSunInfo> result = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = LocalDate.now().plusDays(i);
            String formattedDate = date.format(FORMATTER);

            URI uri = UriComponentsBuilder
                    .fromHttpUrl("http://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getLCRiseSetInfo")
                    .queryParam("ServiceKey", apiKey)
                    .queryParam("longitude", row.getLongitude())
                    .queryParam("latitude", row.getLatitude())
                    .queryParam("locdate", formattedDate)
                    .queryParam("dnYn", "Y")
                    .build(true)
                    .toUri();



            try {
                ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
                String body = response.getBody();

                log.info("[요청 URL] {}", uri);
                log.info("[API 응답 JSON] 산: {}, 날짜: {}\n{}", mountain.getName(), formattedDate, body);

                JsonNode root = objectMapper.readTree(body);
                JsonNode item = root.path("response").path("body").path("items").path("item");

                if (item.isMissingNode() || item.path("sunrise").isMissingNode() || item.path("sunset").isMissingNode()) {
                    log.warn("[일출/일몰] 정보 없음 - 산: {}, 날짜: {}", mountain.getName(), date);
                    continue;
                }

                LocalTime sunrise = parseTime(item.path("sunrise").asText());
                LocalTime sunset = parseTime(item.path("sunset").asText());

                log.info("[일출/일몰] 저장 예정 - 산: {}, 날짜: {}, 일출: {}, 일몰: {}",
                        mountain.getName(), date, sunrise, sunset);

                result.add(MountainSunInfo.builder()
                        .mountain(mountain)
                        .date(date)
                        .sunriseTime(sunrise)
                        .sunsetTime(sunset)
                        .build());

            } catch (Exception e) {
                log.warn("[일출/일몰] 처리 실패 - 산: {}, 날짜: {}, 에러: {}", mountain.getName(), date, e.getMessage());
            }
        }

        return result;
    }

    private LocalTime parseTime(String timeStr) {
        if (timeStr == null || timeStr.length() != 4 && timeStr.length() != 6) return null;
        int h = Integer.parseInt(timeStr.substring(0, 2));
        int m = Integer.parseInt(timeStr.substring(2, 4));
        return LocalTime.of(h, m);
    }
}