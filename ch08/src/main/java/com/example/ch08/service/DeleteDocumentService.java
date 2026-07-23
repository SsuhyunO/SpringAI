package com.example.ch08.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class DeleteDocumentService {
    private final VectorStore vectorStore;

    public void deleteDocument() {
        vectorStore.delete("source == '헌법' && year < 1987");
    }

}
