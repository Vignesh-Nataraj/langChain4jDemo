package org.jarvis.langchain4jdemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jarvis.langchain4jdemo.service.BasicChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for basic chat operations
 */
@RestController
@RequestMapping("/api/basic-chat")
@RequiredArgsConstructor
@Slf4j
public class BasicChatController {

    private final BasicChatService basicChatService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String response = basicChatService.chat(request.message());
        return ResponseEntity.ok(new ChatResponse(response));
    }

    @PostMapping("/question")
    public ResponseEntity<ChatResponse> askQuestion(@RequestBody ChatRequest request) {
        String answer = basicChatService.askQuestion(request.message());
        return ResponseEntity.ok(new ChatResponse(answer));
    }

    @PostMapping("/generate-content")
    public ResponseEntity<ChatResponse> generateContent(@RequestBody ChatRequest request) {
        String content = basicChatService.generateContent(request.message());
        return ResponseEntity.ok(new ChatResponse(content));
    }

    @PostMapping("/generate-code")
    public ResponseEntity<ChatResponse> generateCode(@RequestBody ChatRequest request) {
        String code = basicChatService.generateCode(request.message());
        return ResponseEntity.ok(new ChatResponse(code));
    }

    public record ChatRequest(String message) {}
    public record ChatResponse(String response) {}
}
