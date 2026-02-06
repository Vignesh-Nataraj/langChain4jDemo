package org.jarvis.langchain4jdemo.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configuration class for LangChain4j with Ollama integration
 */
@Configuration
public class LangChain4jConfig {

    @Value("${ollama.base.url}")
    private String ollamaBaseUrl;

    @Value("${ollama.chat.model}")
    private String chatModel;

    @Value("${ollama.timeout}")
    private Duration timeout;

    /**
     * Bean for regular chat model (blocking/synchronous)
     */
    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return OllamaChatModel.builder()
                .baseUrl(ollamaBaseUrl)
                .modelName(chatModel)
                .timeout(timeout)
                .temperature(0.7)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    /**
     * Bean for streaming chat model (asynchronous/streaming responses)
     */
    @Bean
    public StreamingChatLanguageModel streamingChatLanguageModel() {
        return OllamaStreamingChatModel.builder()
                .baseUrl(ollamaBaseUrl)
                .modelName(chatModel)
                .timeout(timeout)
                .temperature(0.7)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    /**
     * Bean for embedding model (converts text to vectors)
     * Using local all-minilm-l6-v2 model for faster embeddings
     */
    @Bean
    public EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }

    /**
     * Bean for in-memory embedding store (stores document embeddings)
     */
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }

    /**
     * Bean for chat memory (maintains conversation context)
     * Keeps last 10 messages in memory
     */
    @Bean
    public MessageWindowChatMemory chatMemory() {
        return MessageWindowChatMemory.withMaxMessages(10);
    }
}
