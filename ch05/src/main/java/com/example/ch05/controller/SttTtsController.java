package com.example.ch05.controller;

import com.example.ch05.service.SttTtsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Controller
public class SttTtsController {
    private final SttTtsService service;

    @GetMapping("/ai/stt-tts")
    public String sttTts(){
        return "/stt-tts";
    }

    @PostMapping("/ai/stt")
    @ResponseBody
    public String stt(@RequestParam("speech") MultipartFile speech)throws Exception{
        String fname = speech.getOriginalFilename();
        byte[] bytes = speech.getBytes();

        String text = service.stt(fname, bytes);

        return text;
    }

    @PostMapping("/ai/tts")
    @ResponseBody
    public byte[] tts(@RequestParam("text") String text)throws Exception{
        byte[] bytes = service.tts(text);

        return bytes;
    }
}
