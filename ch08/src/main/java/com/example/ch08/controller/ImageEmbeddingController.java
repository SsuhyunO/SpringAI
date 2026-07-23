package com.example.ch08.controller;

import com.example.ch08.service.ImageEmbeddingService;
import com.example.ch08.service.TextEmbeddingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ImageEmbeddingController {
    private final ImageEmbeddingService service;

    @GetMapping("/ai/image-embedding")
    public String imageEmbedding(){
        return "/image-embedding";
    }

    @ResponseBody
    @PostMapping("/ai/add-face")
    public String addFace(@RequestParam("personName") String personName,
                                 @RequestParam("attach") MultipartFile[] attach){

        for(MultipartFile mfile:attach){
            try {
                service.addFace(personName, mfile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return "얼굴을 저장했습니다.";
    }

    @ResponseBody
    @PostMapping("/ai/find-face")
    public String findFace(@RequestParam("attach") MultipartFile attach){
        String personName = null;

        try {
            personName = service.findFace(attach);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return personName;
    }
}
