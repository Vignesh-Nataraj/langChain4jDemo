package org.jarvis.langchain4jdemo.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service demonstrating embeddings and vector search
 * Feature: Store and retrieve information using semantic similarity
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    /**
     * Add text to the embedding store
     */
    public void addText(String text, String id) {
        log.info("Adding text to embedding store with id: {}", id);

        TextSegment segment = TextSegment.from(text);
        Embedding embedding = embeddingModel.embed(segment).content();

        embeddingStore.add(embedding, segment);
        log.info("Text embedded and stored successfully");
    }

    /**
     * Add multiple documents to the embedding store
     */
    public void addDocuments(List<String> texts) {
        log.info("Adding {} documents to embedding store", texts.size());

        for (int i = 0; i < texts.size(); i++) {
            TextSegment segment = TextSegment.from(texts.get(i));
            Embedding embedding = embeddingModel.embed(segment).content();
            embeddingStore.add(embedding, segment);
        }

        log.info("All documents embedded and stored successfully");
    }

    /**
     * Search for similar text using semantic similarity
     */
    public List<String> searchSimilar(String query, int maxResults) {
        log.info("Searching for similar texts to: {}", query);

        Embedding queryEmbedding = embeddingModel.embed(query).content();
        List<EmbeddingMatch<TextSegment>> matches =
            embeddingStore.findRelevant(queryEmbedding, maxResults);

        log.info("Found {} relevant matches", matches.size());

        return matches.stream()
                .map(match -> {
                    String text = match.embedded().text();
                    double score = match.score();
                    log.info("Match score: {}, Text: {}", score, text.substring(0, Math.min(50, text.length())));
                    return text;
                })
                .collect(Collectors.toList());
    }

    /**
     * Split long document into chunks and add to store
     */
    public void addLongDocument(String document) {
        log.info("Processing long document of length: {}", document.length());

        Document doc = Document.from(document);
        DocumentSplitter splitter = DocumentSplitters.recursive(300, 50);

        List<TextSegment> segments = splitter.split(doc);
        log.info("Document split into {} segments", segments.size());

        for (TextSegment segment : segments) {
            Embedding embedding = embeddingModel.embed(segment).content();
            embeddingStore.add(embedding, segment);
        }

        log.info("Long document processed and stored successfully");
    }

    /**
     * Get embedding for a text (useful for debugging)
     */
    public float[] getEmbedding(String text) {
        Embedding embedding = embeddingModel.embed(text).content();
        return embedding.vector();
    }
}
