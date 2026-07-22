package com.example.ch07.controller;

import com.example.ch07.service.AdvisorContextService;
import com.example.ch07.service.AdvisorSafeGuardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class AdvisorContextController {
    private final AdvisorContextService service;

    @GetMapping("/ai/advisor-context")
    String advisorContext() {
        return "/advisor-context";
    }

    @ResponseBody
    @PostMapping("/ai/advisor-context")
    public String advisorContext(String question){
        String answer = service.call(question);

        return answer;
    }
}
