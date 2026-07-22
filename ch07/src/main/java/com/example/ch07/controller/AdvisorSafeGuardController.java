package com.example.ch07.controller;

import com.example.ch07.service.AdvisorLoggingService;
import com.example.ch07.service.AdvisorSafeGuardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class AdvisorSafeGuardController {
    private final AdvisorSafeGuardService service;

    @GetMapping("/ai/advisor-safe-guard")
    String advisorSafeGuard() {
        return "/advisor-safe-guard";
    }

    @ResponseBody
    @PostMapping("/ai/advisor-safe-guard")
    public String advisorSafeGuard(String question){
        String answer = service.call(question);

        return answer;
    }
}
