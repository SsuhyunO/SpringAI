package com.example.ch07.service;

import com.example.ch07.advisor.AdvisorA;
import com.example.ch07.advisor.AdvisorB;
import com.example.ch07.advisor.AdvisorC;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class AdvisorLoggingService {
    private ChatClient chatClient;

    public AdvisorLoggingService(ChatClient.Builder chatClientBuilder){
        this.chatClient = chatClientBuilder
                            .defaultAdvisors(
                                // properties파일에 logging.level DEBUG 설정을 반드시 해야 됨
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
