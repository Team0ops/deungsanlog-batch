package com.deungsanlog.batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // 문자열 응답을 UTF-8로 처리하기 위한 컨버터 설정 -> 한글 깨짐 방지
        StringHttpMessageConverter utf8Converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);

        // index에 0을 넣어 가장 우선적으로 UTF-8 변환기가 적용
        restTemplate.getMessageConverters().add(0, utf8Converter);

        return restTemplate;
    }
}