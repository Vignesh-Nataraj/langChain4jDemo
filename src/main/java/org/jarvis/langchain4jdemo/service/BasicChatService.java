package org.jarvis.langchain4jdemo.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service demonstrating basic chat interactions with LLM
 * Feature: Simple question-answering without context
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BasicChatService {

    private final ChatLanguageModel chatLanguageModel;

    /**
     * Simple chat - sends a message and gets a response
     */
    public String chat(String message) {
        log.info("Basic chat request: {}", message);
        String response = chatLanguageModel.generate(message);
        log.info("Basic chat response: {}", response);
        return response;
    }

    /**
     * Ask a specific question
     */
    public String askQuestion(String question) {
        log.info("Question: {}", question);
        String answer = chatLanguageModel.generate(question);
        log.info("Answer: {}", answer);
        return answer;
    }

    /**
     * Generate creative content
     */
    public String generateContent(String prompt) {
        log.info("Content generation prompt: {}", prompt);
        String content = chatLanguageModel.generate(prompt);
        log.info("Generated content length: {} characters", content.length());
        return content;
    }

    /**
     * Code generation example
     */
    public String generateCode(String instruction) {
        String prompt = "Generate code based on this instruction: " + instruction +
                       "\nProvide only the code with comments.";
        log.info("Code generation request: {}", instruction);
        return chatLanguageModel.generate(prompt);
    }
}
