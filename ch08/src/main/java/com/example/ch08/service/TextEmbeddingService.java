package com.example.ch08.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.embedding.EmbeddingResponseMetadata;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class TextEmbeddingService {
    private final EmbeddingModel embeddingModel;

    public void embedding(String question){
        // 임베딩 처리
        EmbeddingResponse response = embeddingModel.embedForResponse(List.of(question));

        // 임베딩 모델 확인
        EmbeddingResponseMetadata metadata = response.getMetadata();

        log.info("모델명 : {}", metadata.getModel());
        log.info("벡터차원 : {}", embeddingModel.dimensions());

        // 임베딩 벡터 확인
        Embedding embedding = response.getResults().get(0);
        log.info("임베딩 벡터 차원: {}", embedding.getOutput().length);
        log.info("임베딩 벡터: {}", embedding.getOutput());
    }

}
