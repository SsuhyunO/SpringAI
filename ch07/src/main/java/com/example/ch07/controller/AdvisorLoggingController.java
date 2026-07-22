package com.example.ch07.controller;

import com.example.ch07.service.AdvisorChainService;
import com.example.ch07.service.AdvisorLoggingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Controller
public class AdvisorLoggingController {
    private final AdvisorLoggingService service;

    @GetMapping("/ai/advisor-logging")
    public String advisorLogging() {
        return "/advisor-logging";
    }

    @ResponseBody
    @PostMapping("/ai/advisor-logging")
    public String advisorLogging(String question){
        String answer = service.call(question);

        return answer;
    }
}
