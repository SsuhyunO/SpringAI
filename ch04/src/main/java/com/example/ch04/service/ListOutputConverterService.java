package com.example.ch04.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ListOutputConverterService {
    private ChatClient chatClient;

    public ListOutputConverterService(ChatClient.Builder chatClientBuilder){
        this.chatClient = chatClientBuilder.build();
    }

    public List<String> convertLowLevel(String city) {
        // 출력 변환기 생성
        ListOutputConverter converter = new ListOutputConverter();

        // 프롬프트 템플릿 생성
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template("{city}에서 유명한 호텔 목록 5개를 출력하세요. {format}")
                .build();

        // 프롬프트 생성
        Prompt prompt = promptTemplate.create(
                Map.of("city", city, "format", converter.getFormat())
                );

        // LLM 요청 및 응답
        String answer = chatClient.prompt(prompt).call().content();
        log.info(answer);

        // Converter로 List 변환
        List<String> answerList = converter.convert(answer);
        log.info(String.valueOf(answerList));

        return answerList;
    }

    public List<String> convertHighLevel(String city) {
        List<String> answerList = chatClient.prompt()
                                            .user("%s에서 유명한 호텔 목록 5개 출력하시오.".formatted(city))
                                            .call()
                                            .entity(new ListOutputConverter());

        return answerList;
    }
}
