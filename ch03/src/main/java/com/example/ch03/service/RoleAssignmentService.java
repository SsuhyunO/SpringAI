package com.example.ch03.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class RoleAssignmentService {
    private final ChatClient chatClient;

    public RoleAssignmentService(ChatClient.Builder chatClientBuilder){
        chatClient = chatClientBuilder.build();
    }

    public Flux<String> roleAssignment(String requirements){
        Flux<String> travelSuggestions = chatClient.prompt()
                .system("""
                        당신이 여행 가이드 역할을 해 주었으면 좋겠습니다.
                        아래 요청사항에서 위치를 알려주면, 근처에 있는 3곳을 제안해 주고,
                        이유를 달  아주세요. 경우에 따라서 방문하고 싶은 장소 유형을 
                        제공할 수도 있습니다.
                        """)
                .user("요청사항: %s".formatted(requirements))
                .stream()
                .content();
        return travelSuggestions;
    }
}
