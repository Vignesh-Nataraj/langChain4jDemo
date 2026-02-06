package org.jarvis.langchain4jdemo.service;

import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.model.chat.ChatLanguageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service demonstrating AI Services (declarative AI interface)
 * Feature: Define AI behavior using annotations and interfaces
 */
@Service
@Slf4j
public class AiServiceExampleService {

    private final ChatAssistant chatAssistant;
    private final CodeGenerator codeGenerator;
    private final TextAnalyzer textAnalyzer;
    private final Translator translator;

    public AiServiceExampleService(ChatLanguageModel chatLanguageModel) {
        // Create AI service instances
        this.chatAssistant = AiServices.create(ChatAssistant.class, chatLanguageModel);
        this.codeGenerator = AiServices.create(CodeGenerator.class, chatLanguageModel);
        this.textAnalyzer = AiServices.create(TextAnalyzer.class, chatLanguageModel);
        this.translator = AiServices.create(Translator.class, chatLanguageModel);
    }

    /**
     * General chat assistant
     */
    public String chat(String message) {
        log.info("Chat assistant request: {}", message);
        return chatAssistant.chat(message);
    }

    /**
     * Generate code
     */
    public String generateCode(String language, String description) {
        log.info("Code generation: {} in {}", description, language);
        return codeGenerator.generateCode(language, description);
    }

    /**
     * Analyze sentiment
     */
    public String analyzeSentiment(String text) {
        log.info("Sentiment analysis for text of length: {}", text.length());
        return textAnalyzer.analyzeSentiment(text);
    }

    /**
     * Summarize text
     */
    public String summarize(String text, int maxWords) {
        log.info("Summarizing text to {} words", maxWords);
        return textAnalyzer.summarize(text, maxWords);
    }

    /**
     * Translate text
     */
    public String translate(String text, String targetLanguage) {
        log.info("Translating text to {}", targetLanguage);
        return translator.translate(text, targetLanguage);
    }

    /**
     * Chat Assistant Interface
     */
    interface ChatAssistant {
        @SystemMessage("You are a helpful assistant. Answer questions concisely and accurately.")
        String chat(String message);
    }

    /**
     * Code Generator Interface
     */
    interface CodeGenerator {
        @SystemMessage("You are an expert programmer. Generate clean, well-commented code.")
        @UserMessage("Generate {{language}} code for: {{description}}")
        String generateCode(@V("language") String language, @V("description") String description);
    }

    /**
     * Text Analyzer Interface
     */
    interface TextAnalyzer {
        @SystemMessage("You are a text analysis expert.")
        @UserMessage("Analyze the sentiment of this text and respond with POSITIVE, NEGATIVE, or NEUTRAL, followed by a brief explanation: {{text}}")
        String analyzeSentiment(@V("text") String text);

        @UserMessage("Summarize the following text in maximum {{maxWords}} words: {{text}}")
        String summarize(@V("text") String text, @V("maxWords") int maxWords);
    }

    /**
     * Translator Interface
     */
    interface Translator {
        @SystemMessage("You are a professional translator. Translate accurately while preserving meaning and tone.")
        @UserMessage("Translate the following text to {{language}}: {{text}}")
        String translate(@V("text") String text, @V("language") String language);
    }
}
