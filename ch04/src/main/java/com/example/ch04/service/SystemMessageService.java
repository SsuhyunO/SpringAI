package com.example.ch04.service;

import com.example.ch04.dto.HotelDTO;
import com.example.ch04.dto.ReviewClassification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SystemMessageService {
    private ChatClient chatClient;

    public SystemMessageService(ChatClient.Builder chatClientBuilder){
        this.chatClient = chatClientBuilder.build();
    }

    public ReviewClassification classifyReview(String review){
        ReviewClassification reviewClassification = chatClient.prompt()
                .system("""
                        영화 리뷰를 [POSITIVE, NEUTRAL, NEGATIVE] 중에서 하나로 분류하고,
                        유효한 JSON을 반환하세요.
                        """)
                .user("%s".formatted(review))
                .call()
                .entity(ReviewClassification.class);
        return reviewClassification;
    }
}
