package org.jarvis.langchain4jdemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jarvis.langchain4jdemo.service.ToolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for Tool/Function Calling examples
 */
@RestController
@RequestMapping("/api/tools")
@RequiredArgsConstructor
@Slf4j
public class ToolController {

    private final ToolService toolService;

    @PostMapping("/chat")
    public ResponseEntity<ToolResponse> chatWithTools(@RequestBody ToolRequest request) {
        String response = toolService.chatWithTools(request.message());
        return ResponseEntity.ok(new ToolResponse(response));
    }

    public record ToolRequest(String message) {}
    public record ToolResponse(String response) {}
}
