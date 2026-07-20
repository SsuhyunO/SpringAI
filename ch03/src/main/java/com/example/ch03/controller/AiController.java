package com.example.ch03.controller;

import com.example.ch03.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AiController {
    private final AiService aiService;

    @GetMapping("/ai/prompt-template")
    public String promptTemplate(){
        return "/prompt-template";
    }

    @PostMapping(
        value = "/ai/prompt-template",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, // 클라이언트가 보내는 데이터타입, 기본 폼 전송, 일반적으로 생략
            produces = MediaType.APPLICATION_NDJSON_VALUE           // NDJSON(Newline Delimited JSON), JSON 객체를 행단위로 전송하는 형식
    )
    public Flux<String> promptTemplate(String statement, String language) {
        System.out.println("statement : " + statement + ", language : " + language);

        Flux<String> fluxString = aiService.promptTemplate3(statement, language);

        return fluxString;
    }

}
