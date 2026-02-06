package org.jarvis.langchain4jdemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jarvis.langchain4jdemo.service.AiServiceExampleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for AI Services examples
 */
@RestController
@RequestMapping("/api/ai-services")
@RequiredArgsConstructor
@Slf4j
public class AiServicesController {

    private final AiServiceExampleService aiServiceExampleService;

    @PostMapping("/chat")
    public ResponseEntity<Response> chat(@RequestBody ChatRequest request) {
        String response = aiServiceExampleService.chat(request.message());
        return ResponseEntity.ok(new Response(response));
    }

    @PostMapping("/generate-code")
    public ResponseEntity<Response> generateCode(@RequestBody CodeRequest request) {
        String code = aiServiceExampleService.generateCode(request.language(), request.description());
        return ResponseEntity.ok(new Response(code));
    }

    @PostMapping("/sentiment")
    public ResponseEntity<Response> analyzeSentiment(@RequestBody TextRequest request) {
        String sentiment = aiServiceExampleService.analyzeSentiment(request.text());
        return ResponseEntity.ok(new Response(sentiment));
    }

    @PostMapping("/summarize")
    public ResponseEntity<Response> summarize(@RequestBody SummarizeRequest request) {
        String summary = aiServiceExampleService.summarize(request.text(), request.maxWords());
        return ResponseEntity.ok(new Response(summary));
    }

    @PostMapping("/translate")
    public ResponseEntity<Response> translate(@RequestBody TranslateRequest request) {
        String translation = aiServiceExampleService.translate(request.text(), request.targetLanguage());
        return ResponseEntity.ok(new Response(translation));
    }

    public record ChatRequest(String message) {}
    public record CodeRequest(String language, String description) {}
    public record TextRequest(String text) {}
    public record SummarizeRequest(String text, int maxWords) {}
    public record TranslateRequest(String text, String targetLanguage) {}
    public record Response(String result) {}
}
