package com.example.ch06.controller;

import com.example.ch06.service.ImageAnalysisSerivice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
public class ImageAnalysisController {
    private final ImageAnalysisSerivice service;

    @GetMapping("/ai/image-analysis")
    public String imageAnalysis(){
        return "/image-analysis";
    }

    @ResponseBody
    @PostMapping("/ai/image-analysis")
    public Flux<String> imageAnalysis(@RequestParam("question") String question,
                              @RequestParam("attach")MultipartFile attach) throws IOException {

        Flux<String> stringFlux = service.analysis(question, attach.getContentType(), attach.getBytes());

        return stringFlux;
    }
}
