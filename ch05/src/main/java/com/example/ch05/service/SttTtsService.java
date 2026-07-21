package com.example.ch05.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SttTtsService {
    private ChatClient chatClient;
    private OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;
    private OpenAiAudioSpeechModel openAiAudioSpeechModel;

    public SttTtsService(
            ChatClient.Builder chatClientBuilder,
            OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel,
            OpenAiAudioSpeechModel openAiAudioSpeechModel){
        this.chatClient = chatClientBuilder.build();
        this.openAiAudioTranscriptionModel = openAiAudioTranscriptionModel;
        this.openAiAudioSpeechModel = openAiAudioSpeechModel;
    }

    public String stt(String fname, byte[] bytes){
        // 파일명 정의
        Resource resource = new ByteArrayResource(bytes) {
            public String getFilename(){
                return fname;
            }
        };

        // 모델 옵션 설정
        OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
                .model("whisper-1")
                .language("ko")
                .build();

        // 프롬트 생성
        AudioTranscriptionPrompt prompt = new AudioTranscriptionPrompt(resource, options);

        // 모델 요청 및 응답
        AudioTranscriptionResponse response = openAiAudioTranscriptionModel.call(prompt);
        String text = response.getResult().getOutput();

        return text;
    }

    public byte[] tts(String text){
        // 모델 옵션 설정
        OpenAiAudioSpeechOptions options = OpenAiAudioSpeechOptions.builder()
                .model("gpt-4o-mini-tts")
                .voice(OpenAiAudioSpeechOptions.Voice.ALLOY)
                .responseFormat(OpenAiAudioSpeechOptions.AudioResponseFormat.MP3)
                .speed(1.0)
                .build();

        // 프롬프트 생성
        TextToSpeechPrompt prompt = new TextToSpeechPrompt(text, options);

        // 모델 요청 및 응답
        TextToSpeechResponse response = openAiAudioSpeechModel.call(prompt);
        byte[] bytes = response.getResult().getOutput();

        return bytes;
    }
}
