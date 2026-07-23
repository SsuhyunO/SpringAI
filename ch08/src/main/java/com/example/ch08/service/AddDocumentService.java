package com.example.ch08.service;

import org.springframework.ai.vectorstore.VectorStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class AddDocumentService {
    private final VectorStore vectorStore;

    public void addDocument() {
        List<Document> documentList = List.of(
                new Document("대통령 선거는 5년 마다 있습니다.", Map.of("source", "헌법", "year", 1987)),
                new Document("대통령 임기는 4년입니다.", Map.of("source", "헌법", "year", 1980)),
                new Document("자동차를 운전하려면 등록해야 합니다.", Map.of("source", "자동차관리법")),
                new Document("대통령은 행정부의 수반입니다..", Map.of("source", "헌법", "year", 1987)),
                new Document("승용차는 정기적인 점검을 받아야 합니다.", Map.of("source", "자동차관리법"))
        );

        // 벡터 저장소 저장
        vectorStore.add(documentList);
    }

}
