package com.example.ch06.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.image.ImageMessage;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MimeType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ImageGenerationSerivice {
    private ChatClient chatClient;
    private ImageModel imageModel;

    @Value("${spring.ai.openai.api-key}")
    private String OPENAI_KEY;

    public ImageGenerationSerivice(ChatClient.Builder chatClientBuilder, ImageModel imageModel){
        this.chatClient = chatClientBuilder.build();
        this.imageModel = imageModel;
    }

    public String generation(String description){
        // 이미지 메세지 작성
        ImageMessage imageMessage = new ImageMessage(description);

        // 이미지 옵션 설정
        OpenAiImageOptions imageOptions = OpenAiImageOptions.builder()
                                                .model("gpt-image-1")
                                                .quality("low")
                                                .width(1024)
                                                .height(1024)
                                                .n(1)   // 생성할 이미지 갯수
                                                .build();

        // 프롬프트 생성
        List<ImageMessage> imageMessageList = List.of(imageMessage);
        ImagePrompt imagePrompt = new ImagePrompt(imageMessage, imageOptions);

        // 모델 요청 및 응답
        ImageResponse imageResponse = imageModel.call(imagePrompt);

        // base64로 인코딩된 이미지 문자열 반환
        String b64Json = imageResponse.getResult().getOutput().getB64Json();

        return b64Json;
    }

    // 이미지 수정
    public String edit(String description, byte[] originalImage, byte[] maskImage){
        // 원본 이미지를 ByteArrayResource 생성
        ByteArrayResource originalResource = new ByteArrayResource(originalImage){
            @Override
            public @Nullable String getFilename(){
                return "image.png";
            }
        };

        // 마스크 이미지를 ByteArrayResource 생성
        ByteArrayResource maskResource = new ByteArrayResource(maskImage){
            @Override
            public @Nullable String getFilename(){
                return "mask.png";
            }
        };

        // 이미지 모델 전송 데이터 설정
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("model", "gpt-image-1");
        form.add("image", originalResource);
        form.add("mask", maskResource);
        form.add("prompt", description);
        form.add("n", "1");
        form.add("size", "1024x1024");
        form.add("quality", "low");

        // REST API 요청을 위한 WebClient 생성
        WebClient webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/images/edits")
                .defaultHeader("Authorization", "Bearer " + OPENAI_KEY)
                .exchangeStrategies(
                        ExchangeStrategies.builder()
                                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(1024 * 1024 * 10))
                                .build()
                )
                .build();

        // 이미지 수정 요청, Mono는 비동기 단일값 스트림
        Mono<OpenAIImageEditResponse> mono = webClient.post()
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(form))
                .retrieve()
                .bodyToMono(OpenAIImageEditResponse.class);

        // Mono가 완료될 때까지 현재 스레드를 블로킹
        OpenAIImageEditResponse response = mono.block();

        // base64 인코딩 이미지 문자열 가져오기
        String b64Json = response.data().get(0).b64_Json();

        return b64Json;
    }

    public record OpenAIImageEditResponse(List<Image> data){
        public record Image(String url,
                            @JsonProperty("b64_json")
                            String b64_Json) {}
    }
}
