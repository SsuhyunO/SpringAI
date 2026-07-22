package com.example.ch07.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AdvisorSafeGuardService {
    private ChatClient chatClient;

    public AdvisorSafeGuardService(ChatClient.Builder chatClientBuilder){
        SafeGuardAdvisor safeGuardAdvisor = new SafeGuardAdvisor(
                List.of("욕", "욕설", "폭탄", "폭력"),
                "해당 질문은 민감한 콘텐츠 요청이므로 답변할 수 없습니다.",
                Ordered.HIGHEST_PRECEDENCE + 1
                );

        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        // properties파일에 logging.level DEBUG 설정을 반드시 해야 됨
                        safeGuardAdvisor,
                        new SimpleLoggerAdvisor() // 로깅을 위한 Advisor 필수
                )
                .build();
    }

    public String call(String question) {
        String answer = chatClient.prompt()
                        .user(question)
                        .call()
                        .content();

        return answer;
    }
}
