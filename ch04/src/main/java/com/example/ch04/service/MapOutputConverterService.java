package com.example.ch04.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MapOutputConverterService {
    private ChatClient chatClient;

    public MapOutputConverterService(ChatClient.Builder chatClientBuilder){
        this.chatClient = chatClientBuilder.build();
    }

    public Map<String, Object> convertLowLevel(String city) {
        // 출력 변환기 생성
        MapOutputConverter converter = new MapOutputConverter();

        // 프롬프트 템플릿 생성
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template("{city}에서 유명한 호텔 목록 5개를 출력하세요. {format}")
                .build();

        // 프롬프트 생성
        Prompt prompt = promptTemplate.create(
                Map.of("city", city, "format", converter.getFormat())
                );

        // LLM 요청 및 응답
        String json = chatClient.prompt(prompt).call().content();
        log.info(json);

        // Converter로 List 변환
        Map<String, Object> hotelInfo = converter.convert(json);
        log.info(String.valueOf(hotelInfo));

        return hotelInfo;
    }

    public Map<String, Object> convertHighLevel(String city) {
        Map<String, Object> hotelInfo = chatClient.prompt()
                                            .user("%s에서 유명한 호텔 목록 5개 출력하시오.".formatted(city))
                                            .call()
                                            .entity(new MapOutputConverter());

        return hotelInfo;
    }
}
