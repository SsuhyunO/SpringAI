package com.example.ch07.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;

@Slf4j
public class MaxCharLengthAdvisor implements CallAdvisor {
    public static final String MAX_CHAR_LENGTH = "maxCharLength";
    private int maxCharLength = 300;
    private int order;

    public MaxCharLengthAdvisor (int order) {
        this.order = order;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        // 사용자 프롬프트 증강
        ChatClientRequest mutatedRequest = augmentPrompt(chatClientRequest);

        ChatClientResponse response = callAdvisorChain.nextCall(mutatedRequest);

        return response;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    public ChatClientRequest augmentPrompt(ChatClientRequest request) {
        String userText = this.maxCharLength + "자 이내로 답변하시오.";

        Integer maxCharLength = (Integer) request.context().get(MAX_CHAR_LENGTH);

        if(maxCharLength != null){
            userText = maxCharLength + "자 이내로 답변하시오.";
        }

        String finalUserText = userText;

        // 사용자 메시지를 강화한 Prompt 생성
        Prompt originalPrompt = request.prompt();

        Prompt augmentedPrompt = originalPrompt.augmentUserMessage(userMessage -> UserMessage.builder()
                                                .text(userMessage.getText() + "" + finalUserText)
                                                .build());

        // 수정된 ChatClientRequest 생성
        ChatClientRequest mutatedRequest = request.mutate()
                                                .prompt(augmentedPrompt)
                                                .build();

        return mutatedRequest;
    }
}
