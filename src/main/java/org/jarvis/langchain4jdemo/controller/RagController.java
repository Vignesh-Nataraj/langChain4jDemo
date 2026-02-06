package org.jarvis.langchain4jdemo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jarvis.langchain4jdemo.service.EmbeddingService;
import org.jarvis.langchain4jdemo.service.RagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for RAG (Retrieval Augmented Generation)
 */
@RestController
@RequestMapping("/api/rag")
@RequiredArgsConstructor
@Slf4j
public class RagController {

    private final RagService ragService;
    private final EmbeddingService embeddingService;

    @PostMapping("/add-document")
    public ResponseEntity<String> addDocument(@RequestBody DocumentRequest request) {
        embeddingService.addText(request.text(), request.id());
        return ResponseEntity.ok("Document added successfully");
    }

    @PostMapping("/add-documents")
    public ResponseEntity<String> addDocuments(@RequestBody MultiDocumentRequest request) {
        embeddingService.addDocuments(request.texts());
        return ResponseEntity.ok(request.texts().size() + " documents added successfully");
    }

    @PostMapping("/add-long-document")
    public ResponseEntity<String> addLongDocument(@RequestBody LongDocumentRequest request) {
        embeddingService.addLongDocument(request.text());
        return ResponseEntity.ok("Long document processed and added successfully");
    }

    @PostMapping("/search")
    public ResponseEntity<List<String>> search(@RequestBody SearchRequest request) {
        List<String> results = embeddingService.searchSimilar(
            request.query(),
            request.maxResults() != null ? request.maxResults() : 3
        );
        return ResponseEntity.ok(results);
    }

    @PostMapping("/ask")
    public ResponseEntity<RagResponse> ask(@RequestBody AskRequest request) {
        String answer = ragService.answerWithContext(
            request.question(),
            request.maxResults() != null ? request.maxResults() : 3
        );
        return ResponseEntity.ok(new RagResponse(answer));
    }

    @PostMapping("/ask-with-sources")
    public ResponseEntity<RagService.RagResponse> askWithSources(@RequestBody AskRequest request) {
        RagService.RagResponse response = ragService.answerWithSources(
            request.question(),
            request.maxResults() != null ? request.maxResults() : 3
        );
        return ResponseEntity.ok(response);
    }

    public record DocumentRequest(String text, String id) {}
    public record MultiDocumentRequest(List<String> texts) {}
    public record LongDocumentRequest(String text) {}
    public record SearchRequest(String query, Integer maxResults) {}
    public record AskRequest(String question, Integer maxResults) {}
    public record RagResponse(String answer) {}
}
