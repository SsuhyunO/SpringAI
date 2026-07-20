package com.example.ch03.controller;

import com.example.ch03.service.StepBackService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class StepBackController {
    private final StepBackService service;

    @GetMapping("/ai/step-back-prompt")
    public String promptTemplate(){
        return "/step-back-prompt";
    }

    @PostMapping(
        value = "/ai/step-back-prompt",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces = MediaType.TEXT_PLAIN_VALUE
    )
    @ResponseBody
    public String stepBackPrompt(@RequestParam("question") String question) throws Exception{
        String answer = service.stepBackPrompt(question);

        return answer;
    }
}
