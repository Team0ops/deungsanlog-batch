package com.deungsanlog.batch.job.mountain.processor;

import com.deungsanlog.batch.domain.mountain.entity.Mountain;
import com.deungsanlog.batch.domain.mountain.entity.MountainDescription;
import com.deungsanlog.batch.job.mountain.dto.CsvFilterItem;
import com.deungsanlog.batch.job.mountain.dto.MountainDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

@Slf4j
@Component
@RequiredArgsConstructor

public class MountainApiProcessor implements ItemProcessor<CsvFilterItem, Mountain> {

    @Value("${publicdata.api.key.encoded}")
    private String apiKey;

    private final RestTemplate restTemplate;

    @Override
    public Mountain process(CsvFilterItem item) {
        String name = item.getName();
        Double expectedElevation = item.getElevation();

        try {
            String url = "http://api.forest.go.kr/openapi/service/trailInfoService/getforeststoryservice"
                    + "?mntnNm="    + name
                    + "&ServiceKey="+ apiKey
                    + "&_type=xml"
                    + "&pageNo=1"           // ← 페이지 번호
                    + "&numOfRows=30";
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(new HttpHeaders()),
                    String.class
            );

            log.warn("🌐 응답 원문 → name: {}, response: \n{}", name, response.getBody());

            String cleanXml = removeBOM(response.getBody()).trim();
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader(cleanXml)));

            String resultCode = doc.getElementsByTagName("resultCode").item(0).getTextContent();
            if (!"00".equals(resultCode)) {
                log.warn("응답 실패: {} → resultCode: {}", name, resultCode);
                return null;
            }

            NodeList items = doc.getElementsByTagName("item");
            Node matchedItem = null;

            for (int i = 0; i < items.getLength(); i++) {
                Node node = items.item(i);
                Double actualElevation = parseDoubleSafe(getTagValue("mntninfohght", node));
                if (actualElevation != null && expectedElevation != null &&
                        Math.abs(actualElevation - expectedElevation) < 1.5) {
                    matchedItem = node;
                    break;
                }
            }

            if (matchedItem == null) {
                log.warn("고도 일치 실패: {} (기준 고도: {})", name, expectedElevation);
                return null;
            }

            MountainDto dto = MountainDto.builder()
                    .mntnid(getTagValue("mntnid", matchedItem))
                    .mntnnm(getTagValue("mntnnm", matchedItem))
                    .mntninfopoflc(getTagValue("mntninfopoflc", matchedItem))
                    .mntninfohght(parseIntSafe(getTagValue("mntninfohght", matchedItem)))
                    .mntnattchimageseq(getTagValue("mntnattchimageseq", matchedItem))
                    .mntnsbttlinfo(getTagValue("mntnsbttlinfo", matchedItem))
                    .mntninfodscrt(getTagValue("mntninfodscrt", matchedItem))
                    .crcmrsghtnginfodscrt(getTagValue("crcmrsghtnginfodscrt", matchedItem))
                    .build();

            Mountain mountain = Mountain.builder()
                    .name(dto.getMntnnm())
                    .location(dto.getMntninfopoflc())
                    .externalId(dto.getMntnid())
                    .elevation(dto.getMntninfohght())
                    .thumbnailImgUrl(dto.getMntnattchimageseq())
                    .build();

            MountainDescription description = MountainDescription.builder()
                    .summary(dto.getMntnsbttlinfo())
                    .fullDescription(dto.getMntninfodscrt())
                    .nearbyTourInfo(dto.getCrcmrsghtnginfodscrt())
                    .mountain(mountain)
                    .build();

            mountain.setDescription(description);
            return mountain;

        } catch (Exception e) {
            log.error("API 에러 {} - {}", name, e.getMessage());
            return null;
        }
    }

    private Double parseDoubleSafe(String text) {
        try {
            return (text == null) ? null : Double.parseDouble(text);
        } catch (Exception e) {
            return null;
        }
    }

    private String getTagValue(String tag, Node node) {
        try {
            return ((org.w3c.dom.Element) node).getElementsByTagName(tag).item(0).getTextContent();
        } catch (Exception e) {
            return null;
        }
    }

    private Integer parseIntSafe(String text) {
        try {
            return Integer.parseInt(text);
        } catch (Exception e) {
            return null;
        }
    }

    private String removeBOM(String xml) {
        if (xml != null && xml.startsWith("\uFEFF")) {
            return xml.substring(1);
        }
        return xml;
    }

}
