package org.jarvis.langchain4jdemo.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.output.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Service demonstrating streaming responses from LLM
 * Feature: Get responses token-by-token for better UX
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StreamingChatService {

    private final StreamingChatLanguageModel streamingChatModel;

    /**
     * Stream response - useful for long responses
     * Returns a CompletableFuture that completes when streaming is done
     */
    public CompletableFuture<String> streamChat(String message, StreamingResponseCallback handler) {
        log.info("Streaming chat request: {}", message);

        CompletableFuture<String> future = new CompletableFuture<>();
        StringBuilder fullResponse = new StringBuilder();

        streamingChatModel.generate(message, new StreamingResponseHandler<AiMessage>() {
            @Override
            public void onNext(String token) {
                fullResponse.append(token);
                handler.onToken(token);
            }

            @Override
            public void onComplete(Response<AiMessage> response) {
                log.info("Streaming completed. Total length: {}", fullResponse.length());
                String completeText = fullResponse.toString();
                handler.onComplete(completeText);
                future.complete(completeText);
            }

            @Override
            public void onError(Throwable error) {
                log.error("Streaming error: {}", error.getMessage(), error);
                handler.onError(error);
                future.completeExceptionally(error);
            }
        });

        return future;
    }

    /**
     * Functional interface for handling streaming responses
     */
    public interface StreamingResponseCallback {
        void onToken(String token);
        void onComplete(String fullResponse);
        void onError(Throwable error);
    }
}
