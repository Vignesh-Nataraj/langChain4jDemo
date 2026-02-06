package org.jarvis.langchain4jdemo.service;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service demonstrating conversational AI with memory
 * Feature: Maintains context across multiple exchanges
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ConversationalChatService {

    private final ChatLanguageModel chatLanguageModel;
    private final MessageWindowChatMemory chatMemory;

    /**
     * Chat with memory - maintains conversation context
     */
    public String chatWithMemory(String userMessage) {
        log.info("Conversational chat - User: {}", userMessage);

        // Add user message to memory
        chatMemory.add(UserMessage.from(userMessage));

        // Get all messages from memory for context
        var messages = chatMemory.messages();

        // Generate response with full context
        String response = chatLanguageModel.generate(messages).content().text();

        // Add AI response to memory
        chatMemory.add(AiMessage.from(response));

        log.info("Conversational chat - AI: {}", response);
        return response;
    }

    /**
     * Clear conversation memory
     */
    public void clearMemory() {
        log.info("Clearing conversation memory");
        chatMemory.clear();
    }

    /**
     * Get current conversation history size
     */
    public int getConversationSize() {
        return chatMemory.messages().size();
    }
}
