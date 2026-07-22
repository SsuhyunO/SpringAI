package com.example.ch07.controller;

import com.example.ch07.service.AdvisorChainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Controller
public class AdvisorChainController {
    private final AdvisorChainService service;

    @GetMapping("/ai/advisor-chain")
    public String advisorChain() {
        return "/advisor-chain";
    }

    @ResponseBody
    @PostMapping("/ai/advisor-chain")
    //public String advisorChain(@RequestParam("question") String question) {
    public Flux<String> advisorChain(@RequestParam("question") String question) {

        //String answer = service.call(question);
        Flux<String> answer = service.stream(question);

        return answer;
    }
}
