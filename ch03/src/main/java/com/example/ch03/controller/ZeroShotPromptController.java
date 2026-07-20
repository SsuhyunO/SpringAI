package com.example.ch03.controller;

import com.example.ch03.service.AiService;
import com.example.ch03.service.ZeroShotPromptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ZeroShotPromptController {
    private final ZeroShotPromptService service;

    @GetMapping("/ai/zero-shot-prompt")
    public String promptTemplate(){
        return "/zero-shot-prompt";
    }

    @PostMapping("/ai/zero-shot-prompt")
    @ResponseBody
    public String zeroShotPrompt(@RequestParam("review") String review){
        String answer = service.prompt(review);

        return answer;
    }

}
