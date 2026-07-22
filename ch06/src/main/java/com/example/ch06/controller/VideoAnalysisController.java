package com.example.ch06.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Controller
public class VideoAnalysisController {


    @GetMapping("/ai/video-analysis")
    public String videoAnalysis() {
        return "/video-analysis";
    }

}
