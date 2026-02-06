package org.jarvis.langchain4jdemo.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service demonstrating RAG (Retrieval Augmented Generation)
 * Feature: Answer questions based on custom knowledge base
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RagService {

    private final ChatLanguageModel chatLanguageModel;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    /**
     * Answer a question using RAG
     * 1. Find relevant context from embedding store
     * 2. Create prompt with context
     * 3. Generate answer using LLM
     */
    public String answerWithContext(String question, int maxResults) {
        log.info("RAG query: {}", question);

        // Step 1: Find relevant context
        Embedding questionEmbedding = embeddingModel.embed(question).content();
        List<EmbeddingMatch<TextSegment>> relevantMatches =
            embeddingStore.findRelevant(questionEmbedding, maxResults);

        if (relevantMatches.isEmpty()) {
            log.warn("No relevant context found for question");
            return "I don't have enough information to answer this question. Please add relevant documents first.";
        }

        // Step 2: Build context from relevant segments
        String context = relevantMatches.stream()
                .map(match -> match.embedded().text())
                .collect(Collectors.joining("\n\n"));

        log.info("Found {} relevant segments", relevantMatches.size());
        log.debug("Context: {}", context.substring(0, Math.min(200, context.length())));

        // Step 3: Create prompt with context
        String prompt = String.format("""
                Based on the following context, answer the question.
                If the answer is not in the context, say "I don't know based on the provided information."

                Context:
                %s

                Question: %s

                Answer:
                """, context, question);

        // Step 4: Generate answer
        String answer = chatLanguageModel.generate(prompt);
        log.info("RAG answer generated successfully");

        return answer;
    }

    /**
     * Answer with context and show sources
     */
    public RagResponse answerWithSources(String question, int maxResults) {
        log.info("RAG query with sources: {}", question);

        // Find relevant context
        Embedding questionEmbedding = embeddingModel.embed(question).content();
        List<EmbeddingMatch<TextSegment>> relevantMatches =
            embeddingStore.findRelevant(questionEmbedding, maxResults);

        if (relevantMatches.isEmpty()) {
            return new RagResponse(
                "I don't have enough information to answer this question.",
                List.of()
            );
        }

        // Extract context and sources
        List<String> sources = relevantMatches.stream()
                .map(match -> match.embedded().text())
                .toList();

        String context = String.join("\n\n", sources);

        // Create prompt
        String prompt = String.format("""
                Based on the following context, answer the question.

                Context:
                %s

                Question: %s

                Answer:
                """, context, question);

        // Generate answer
        String answer = chatLanguageModel.generate(prompt);

        return new RagResponse(answer, sources);
    }

    /**
     * Response object containing answer and sources
     */
    public record RagResponse(String answer, List<String> sources) {}
}
