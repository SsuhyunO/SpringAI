package com.example.ch03.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
public class AiService {
    private final ChatClient chatClient;

    public AiService(ChatClient.Builder chatClientBuilder){
        chatClient = chatClientBuilder.build();
    }

    private PromptTemplate systemTemplate = SystemPromptTemplate.builder()
            .template(
                    """
                    답변을 생성할 때 HTML과 CSS를 사용해서 파란색 글자로 출력하시오.
                    <span> 태그 안에 들어갈 내용만 출력하시오.
                    """
            ).build();

    private PromptTemplate userTemplate = SystemPromptTemplate.builder()
            .template("다음 질문을 {language}로 답변하시오.\n질문: {statement}")
            .build();

    public Flux<String> promptTemplate1(String statement, String language) {
        Prompt prompt = userTemplate.create(Map.of("statement", statement, "language", "language"));

        Flux<String> reponse = chatClient.prompt(prompt).stream().content();

        return reponse;
    }

    public Flux<String> promptTemplate2(String statement, String language) {
        Flux<String> fluxString = chatClient.prompt().messages(
                                        systemTemplate.createMessage(),
                                        userTemplate.createMessage(Map.of("statement", statement,
                                                                          "language", language))
                                        ).stream()
                                        .content();

        return fluxString;
    }

    public Flux<String> promptTemplate3(String statement, String language) {
        String systemText = """
                    답변을 생성할 때 HTML과 CSS를 사용해서 파란색 글자로 출력하시오.
                    <span> 태그 안에 들어갈 내용만 출력하시오.
                    """;
        String userText = "다음 질문을 %s로 답변하시오.\n질문: %s".formatted(language, statement);

        Flux<String> fluxString = chatClient.prompt()
                .system(systemText)
                .user(userText)
                .stream()
                .content();

        return fluxString;
    }

    public Flux<String> promptTemplate4(String statement, String language) {
        return null;
    }

}
