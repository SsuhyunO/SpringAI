package com.example.ch04.service;

import com.example.ch04.dto.HotelDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class GenericBeanOutputConverterService {
    private ChatClient chatClient;

    public GenericBeanOutputConverterService(ChatClient.Builder chatClientBuilder){
        this.chatClient = chatClientBuilder.build();
    }

    public List<HotelDTO> convertLowLevel(String cities) {
        // 출력 변환기 생성
        BeanOutputConverter<List<HotelDTO>> converter = new BeanOutputConverter<>(new ParameterizedTypeReference<List<HotelDTO>>() {});

        // 프롬프트 템플릿 생성
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template("다음 도시들에서 유명한 호텔 3개를 출력하시오. {cities} {format}")
                .build();

        // 프롬프트 생성
        Prompt prompt = promptTemplate.create(
                Map.of("cities", cities, "format", converter.getFormat())
        );

        // LLM 요청 및 응답
        String json = chatClient.prompt(prompt).call().content();
        log.info(json);

        // Converter로 List 변환
        List<HotelDTO> hotelList = converter.convert(json);
        log.info(String.valueOf(hotelList));

        return hotelList;
    }

    public List<HotelDTO> convertHighLevel(String cities) {
        List<HotelDTO> hotelList = chatClient.prompt()
                .user("다음 도시들에서 유명한 호텔 3개를 출력하세요.%s".formatted(cities))
                .call()
                .entity(new ParameterizedTypeReference<List<HotelDTO>>() {});

        return hotelList;
    }
}
