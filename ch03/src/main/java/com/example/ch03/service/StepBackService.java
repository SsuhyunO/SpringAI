package com.example.ch03.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class StepBackService {
    private final ChatClient chatClient;

    public StepBackService(ChatClient.Builder chatClientBuilder){
        chatClient = chatClientBuilder.build();
    }

    public String stepBackPrompt(String question) throws Exception{
        String questions = chatClient.prompt()
                .user("""
                    사용자 질문을 처리하기 Step-Back 프롬프트 기법을 사용하려고 합니다.
                    사용자 질문을 단계별 질문들로 재구성해 주세요.
                    맨 마지막 질문은 사용자 질문과 일치해야 합니다.
                    단계별 질문을 항목으로 하는 JSON 배열로 출력해 주세요.
                    예시: ["...","...","...","..."]
                    사용자 질문: %s
                    """.formatted(question))
                .call()
                .content();

        String json = questions.substring(questions.indexOf("["), questions.indexOf("]")+1);
        log.info(json);

        ObjectMapper objectMapper = new ObjectMapper();
        List<String> listQusetion = objectMapper.readValue(
                json,
                new TypeReference<List<String>>() {}
        );

        String[] answerArray = new String[listQusetion.size()];
        for(int i=0;i<listQusetion.size();i++){
            String stepQuestion = listQusetion.get(i);
            String stepAnswer = getStepAnswer(stepQuestion, answerArray);
            answerArray[i] = stepAnswer;
            log.info("단계{} 질문: {}, 답변: {}", i+1, stepQuestion, stepAnswer);
        }

        return answerArray[answerArray.length-1];
    }

    public String getStepAnswer(String question, String... prevStepAnswers){
        String context = "";

        for(String prevStepAnswer : prevStepAnswers){
            context += Objects.requireNonNullElse(prevStepAnswer, "");
        }

        String answer = chatClient.prompt()
                .user("""
                        %s
                        문맥: %s
                        """.formatted(question, context))
                .call()
                .content();

        return answer;
    }
}
