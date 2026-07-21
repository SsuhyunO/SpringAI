package com.example.ch04.controller;

import com.example.ch04.service.ListOutputConverterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ListOutputConverterController {
    private final ListOutputConverterService service;

    @GetMapping("/ai/list-output-converter")
    public String listOutputConverter(){
        return "/list-output-converter";
    }

    @ResponseBody
    @PostMapping("/ai/list-output-converter")
    public List<String> listOutputConverter(@RequestParam("city") String city){
        // List<String> answerList = service.convertLowLevel(city);
        List<String> answerList = service.convertHighLevel(city);

        return answerList;
    }

}
