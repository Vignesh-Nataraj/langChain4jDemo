package org.jarvis.langchain4jdemo.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jarvis.langchain4jdemo.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Demonstration runner that showcases all LangChain4j features
 * Uncomment @Component to run on startup
 */
// @Component
@RequiredArgsConstructor
@Slf4j
public class LangChain4jDemoRunner implements CommandLineRunner {

    private final BasicChatService basicChatService;
    private final ConversationalChatService conversationalChatService;
    private final EmbeddingService embeddingService;
    private final RagService ragService;
    private final AiServiceExampleService aiServiceExampleService;
    private final ToolService toolService;

    @Override
    public void run(String... args) throws Exception {
        log.info("=".repeat(80));
        log.info("Starting LangChain4j Demo");
        log.info("=".repeat(80));

        // Demo 1: Basic Chat
        demo1BasicChat();

        // Demo 2: Conversational Chat with Memory
        demo2ConversationalChat();

        // Demo 3: Embeddings and Search
        demo3Embeddings();

        // Demo 4: RAG (Retrieval Augmented Generation)
        demo4Rag();

        // Demo 5: AI Services
        demo5AiServices();

        // Demo 6: Tools/Function Calling
        demo6Tools();

        log.info("=".repeat(80));
        log.info("Demo Complete! Check the logs above for results.");
        log.info("=".repeat(80));
    }

    private void demo1BasicChat() {
        log.info("\n" + "=".repeat(80));
        log.info("DEMO 1: Basic Chat");
        log.info("=".repeat(80));

        String response1 = basicChatService.chat("What is Java?");
        log.info("Q: What is Java?");
        log.info("A: {}", response1);

        String response2 = basicChatService.generateCode("Create a Java method to check if a number is prime");
        log.info("\nCode Generation Result:");
        log.info("{}", response2);
    }

    private void demo2ConversationalChat() {
        log.info("\n" + "=".repeat(80));
        log.info("DEMO 2: Conversational Chat with Memory");
        log.info("=".repeat(80));

        conversationalChatService.clearMemory();

        String response1 = conversationalChatService.chatWithMemory("My favorite color is blue");
        log.info("User: My favorite color is blue");
        log.info("AI: {}", response1);

        String response2 = conversationalChatService.chatWithMemory("What is my favorite color?");
        log.info("\nUser: What is my favorite color?");
        log.info("AI: {}", response2);

        log.info("\nConversation size: {} messages", conversationalChatService.getConversationSize());
    }

    private void demo3Embeddings() {
        log.info("\n" + "=".repeat(80));
        log.info("DEMO 3: Embeddings and Vector Search");
        log.info("=".repeat(80));

        List<String> documents = List.of(
            "Java is a popular programming language",
            "Python is great for data science",
            "JavaScript runs in web browsers",
            "The weather is sunny today",
            "Machine learning is a subset of AI"
        );

        embeddingService.addDocuments(documents);
        log.info("Added {} documents to embedding store", documents.size());

        String query = "programming languages";
        List<String> results = embeddingService.searchSimilar(query, 3);

        log.info("\nQuery: {}", query);
        log.info("Top {} similar documents:", results.size());
        for (int i = 0; i < results.size(); i++) {
            log.info("{}. {}", i + 1, results.get(i));
        }
    }

    private void demo4Rag() {
        log.info("\n" + "=".repeat(80));
        log.info("DEMO 4: RAG (Retrieval Augmented Generation)");
        log.info("=".repeat(80));

        // Add knowledge base
        List<String> knowledge = List.of(
            "LangChain4j is a Java library for building applications with Large Language Models.",
            "Ollama allows you to run LLMs locally on your machine without internet connection.",
            "RAG (Retrieval Augmented Generation) combines information retrieval with text generation.",
            "Embeddings are vector representations of text that capture semantic meaning.",
            "Spring Boot is a framework that makes it easy to create production-ready applications."
        );

        embeddingService.addDocuments(knowledge);
        log.info("Added {} knowledge base entries", knowledge.size());

        String question = "What is LangChain4j?";
        String answer = ragService.answerWithContext(question, 2);

        log.info("\nQuestion: {}", question);
        log.info("Answer: {}", answer);

        String question2 = "How can I run LLMs locally?";
        RagService.RagResponse response = ragService.answerWithSources(question2, 2);

        log.info("\nQuestion: {}", question2);
        log.info("Answer: {}", response.answer());
        log.info("Sources used: {}", response.sources().size());
    }

    private void demo5AiServices() {
        log.info("\n" + "=".repeat(80));
        log.info("DEMO 5: AI Services (Declarative AI)");
        log.info("=".repeat(80));

        String sentiment = aiServiceExampleService.analyzeSentiment("This product is amazing! I love it!");
        log.info("Sentiment Analysis: {}", sentiment);

        String summary = aiServiceExampleService.summarize(
            "Artificial intelligence is transforming the world. " +
            "It helps in healthcare, finance, education, and many other fields. " +
            "Machine learning, a subset of AI, enables computers to learn from data.",
            20
        );
        log.info("\nSummary: {}", summary);

        String translation = aiServiceExampleService.translate("Hello, how are you?", "Spanish");
        log.info("\nTranslation to Spanish: {}", translation);
    }

    private void demo6Tools() {
        log.info("\n" + "=".repeat(80));
        log.info("DEMO 6: Tools/Function Calling");
        log.info("=".repeat(80));

        String calc1 = toolService.chatWithTools("What is 25 multiplied by 4?");
        log.info("Q: What is 25 multiplied by 4?");
        log.info("A: {}", calc1);

        String calc2 = toolService.chatWithTools("Calculate the square root of 144");
        log.info("\nQ: Calculate the square root of 144");
        log.info("A: {}", calc2);

        String weather = toolService.chatWithTools("What's the weather in San Francisco?");
        log.info("\nQ: What's the weather in San Francisco?");
        log.info("A: {}", weather);
    }
}
