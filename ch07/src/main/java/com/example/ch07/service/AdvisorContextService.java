package com.example.ch07.service;

import com.example.ch07.advisor.MaxCharLengthAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AdvisorContextService {
    private ChatClient chatClient;

    public AdvisorContextService(ChatClient.Builder chatClientBuilder){
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        new MaxCharLengthAdvisor(Ordered.HIGHEST_PRECEDENCE),
                        new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE) // 로깅을 위한 Advisor 필수
                )
                .build();
    }

    public String call(String question) {
        String answer = chatClient.prompt()
                        .advisors(advisor -> advisor.param(MaxCharLengthAdvisor.MAX_CHAR_LENGTH, 100))
                        .user(question)
                        .call()
                        .content();

        return answer;
    }
}
