package com.example.ch03.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
public class ZeroShotPromptService {
    private final ChatClient chatClient;

    public ZeroShotPromptService(ChatClient.Builder chatClientBuilder){
        chatClient = chatClientBuilder.build();
    }

    private PromptTemplate promptTemplate = PromptTemplate.builder()
            .template(
                    """
                    영화리뷰를 [긍정적, 중립적, 부정적] 중에서 하나로 분류해주세요.
                    레이블만 반환하세요.
                    리뷴: {review}
                    """)
                    .build();

    public String prompt(String review) {
        String answer = chatClient.prompt()
                .user(promptTemplate.render(Map.of("review", review)))
                .call()
                .content();

        return answer;
    }

    public Flux<String> promptTemplate4(String statement, String language) {
        return null;
    }

}
