package com.example.ch08.service;

import com.openai.models.vectorstores.VectorStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ImageEmbeddingService {
    private WebClient webClient;
    private JdbcTemplate jdbcTemplate;

    public ImageEmbeddingService(WebClient.Builder webClientBuild, JdbcTemplate jdbcTemplate){
        this.webClient = webClientBuild.build();
        this.jdbcTemplate = jdbcTemplate;
    }

    public float[] getFaceVector(MultipartFile mfile) throws IOException {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", mfile.getBytes())
                .filename(mfile.getOriginalFilename())
                .contentType(MediaType.valueOf(mfile.getContentType()));

        MultiValueMap<String, HttpEntity<?>> multipartForm = builder.build();

        FaceEmbeddingResponse response = webClient
                                                .post()
                                                .uri("http://localhost:50001/get-face-vector")
                                                .body(BodyInserters.fromMultipartData(multipartForm))
                                                .retrieve()
                                                .bodyToMono(FaceEmbeddingResponse.class)
                                                .block();

        return response.vector;
    }

    public void addFace(String personName, MultipartFile mfile) throws IOException {
        // 이미지 파일 임베딩 요청
        float[] vector = getFaceVector(mfile);

        // 벡터 저장소에 저장
        String strVector = Arrays.toString(vector).replace("", "");

        String sql = "INSERT INTO face_vector_store (content, embedding) VALUES (?, ?::vector)";
        jdbcTemplate.update(sql, personName, strVector);
    }

    public record FaceEmbeddingResponse(float[] vector) {

    }

    public String findFace(MultipartFile mfile) throws IOException {
        float[] vector = getFaceVector(mfile);
        String strVector = Arrays.toString(vector).replace(" ", "");

        String sql = """
                SELECT content, (embedding <=> ?::vector) AS similarity
                FROM face_vector_store
                ORDER BY embedding <=> ?::vector
                LIMIT 3
                """;

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, strVector, strVector);

        for(Map<String, Object> map : list) {
            String personName = (String) map.get("content");
            Double similarity = (Double) map.get("similarity");

            log.info("{} 코사인 거리: {}", personName, similarity);
        }

        // 검색 결과에서 거리가 가장 짧은 벡터의 유사도가 0.3 이상일 경우
        double similarity = (double) list.get(0).get("similarity");
        String personName = (String) list.get(0).get("content");

        if(similarity > 0.5) {
            return "등록된 사람이 아닙니다.";
        }

        return personName;
    }
}
