package com.example.ch08.controller;

import com.example.ch08.service.DeleteDocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class DeleteDocumentController {
    private final DeleteDocumentService service;

    @GetMapping("/ai/delete-document")
    public String deleteDocument(){
        return "/delete-document";
    }

    @ResponseBody
    @PostMapping("/ai/delete-document")
    public String deleteDocument(@RequestParam("question") String question){
        service.deleteDocument();

        return "Document가 삭제되었습니다.";
    }
}
