package com.example.ch06.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.ImageModel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

@Service
public class ImageAnalysisSerivice {
    private ChatClient chatClient;
    private ImageModel imageModel;

    public ImageAnalysisSerivice(ChatClient.Builder chatClientBuilder, ImageModel imageModel){
        this.chatClient = chatClientBuilder.build();
        this.imageModel = imageModel;
    }

    public Flux<String> analysis(String question, String contentType, byte[] bytes){
        // 시스템 메세지 작성
        SystemMessage systemMessage = SystemMessage.builder()
                .text("""
                        당신은 이미지 분석 전문가입니다.
                        사용자 질문에 맞게 이미지를 분석하고 답변하시오.
                        """)
                .build();

        // 미디어 생성
        Media media = Media.builder()
                .mimeType(MimeType.valueOf(contentType))
                .data(new ByteArrayResource(bytes))
                .build();

        // 사용자 메세지 작성
        UserMessage userMessage = UserMessage.builder()
                .text(question)
                .media(media)
                .build();

        // 프롬프트 생성
        Prompt prompt = Prompt.builder()
                .messages(systemMessage, userMessage)
                .build();

        // 모델 요청 및 응답
        Flux<String> stringFlux = chatClient.prompt(prompt).stream().content();

        return stringFlux;
    }
}
