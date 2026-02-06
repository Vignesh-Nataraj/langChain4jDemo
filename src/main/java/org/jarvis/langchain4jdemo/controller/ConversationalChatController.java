package org.jarvis.langchain4jdemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jarvis.langchain4jdemo.service.ConversationalChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for conversational chat with memory
 */
@RestController
@RequestMapping("/api/conversational-chat")
@RequiredArgsConstructor
@Slf4j
public class ConversationalChatController {

    private final ConversationalChatService conversationalChatService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        String response = conversationalChatService.chatWithMemory(request.message());
        int conversationSize = conversationalChatService.getConversationSize();
        return ResponseEntity.ok(new ChatResponse(response, conversationSize));
    }

    @PostMapping("/clear")
    public ResponseEntity<String> clearMemory() {
        conversationalChatService.clearMemory();
        return ResponseEntity.ok("Conversation memory cleared");
    }

    @GetMapping("/size")
    public ResponseEntity<Integer> getConversationSize() {
        return ResponseEntity.ok(conversationalChatService.getConversationSize());
    }

    public record ChatRequest(String message) {}
    public record ChatResponse(String response, int conversationSize) {}
}
