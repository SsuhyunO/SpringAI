package com.example.ch03.controller;

import com.example.ch03.service.FewShotPromptService;
import com.example.ch03.service.ZeroShotPromptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class FewShotPromptController {
    private final FewShotPromptService service;

    @GetMapping("/ai/few-shot-prompt")
    public String promptTemplate(){
        return "/few-shot-prompt";
    }

    @PostMapping("/ai/few-shot-prompt")
    @ResponseBody
    public String fewShotPrompt(@RequestParam("order") String order){
        String answer = service.prompt(order);

        return answer;
    }

}
