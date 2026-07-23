package com.example.ch08.controller;

import com.example.ch08.service.SearchDocument1Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class SearchDocument1Controller {
    private final SearchDocument1Service service;

    @GetMapping("/ai/search-document-1")
    public String searchDocument1(){
        return "/search-document-1";
    }

    @ResponseBody
    @PostMapping("/ai/search-document-1")
    public String searchDocument1(@RequestParam("question") String question){
        List<Document> documentList = service.searchDocument(question);

        String text = "";

        for(Document doc:documentList){
            text += "<div>";
            text += "<span>유사도 점수 : %f,</span>".formatted(doc.getScore());
            text += "<span>%s(%s)</span>".formatted(doc.getText(), doc.getMetadata().get("year"));
            text += "</div>";
        }

        return text;
    }
}
