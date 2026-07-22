package com.example.ch06.controller;

import com.example.ch06.service.ImageAnalysisSerivice;
import com.example.ch06.service.ImageGenerationSerivice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
public class ImageGenerationController {
    private final ImageGenerationSerivice service;

    @GetMapping("/ai/image-generation")
    public String imageGeneration(){
        return "/image-generation";
    }

    @ResponseBody
    @PostMapping("/ai/image-generate")
    public String imageGeneration(String description)  {
        String b64Json = service.generation(description);

        return b64Json;
    }

    @ResponseBody
    @PostMapping("/ai/image-edit")
    public String imageEdit(String description, MultipartFile originalImage, MultipartFile maskImage) throws IOException {
        String b64Json = service.edit(description, originalImage.getBytes(), maskImage.getBytes());

        return b64Json;
    }
}
