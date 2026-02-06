package org.jarvis.langchain4jdemo.service;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Service demonstrating Tools/Function Calling
 * Feature: LLM can call Java methods to perform actions
 */
@Service
@Slf4j
public class ToolService {

    private final Assistant assistant;
    private final Map<String, String> dataStore = new HashMap<>();

    public ToolService(ChatLanguageModel chatLanguageModel) {
        this.assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .tools(new CalculatorTool(), new WeatherTool(), new DataStoreTool(dataStore))
                .build();
    }

    /**
     * Chat with assistant that has access to tools
     */
    public String chatWithTools(String message) {
        log.info("Tool-enabled chat: {}", message);
        return assistant.chat(message);
    }

    /**
     * Assistant interface with tools
     */
    interface Assistant {
        String chat(String message);
    }

    /**
     * Calculator Tool - performs mathematical calculations
     */
    public static class CalculatorTool {

        @Tool("Adds two numbers")
        public double add(double a, double b) {
            log.info("Tool called: add({}, {})", a, b);
            return a + b;
        }

        @Tool("Subtracts second number from first")
        public double subtract(double a, double b) {
            log.info("Tool called: subtract({}, {})", a, b);
            return a - b;
        }

        @Tool("Multiplies two numbers")
        public double multiply(double a, double b) {
            log.info("Tool called: multiply({}, {})", a, b);
            return a * b;
        }

        @Tool("Divides first number by second")
        public double divide(double a, double b) {
            log.info("Tool called: divide({}, {})", a, b);
            if (b == 0) {
                throw new IllegalArgumentException("Cannot divide by zero");
            }
            return a / b;
        }

        @Tool("Calculates the square root of a number")
        public double sqrt(double number) {
            log.info("Tool called: sqrt({})", number);
            return Math.sqrt(number);
        }
    }

    /**
     * Weather Tool - simulates weather API
     */
    public static class WeatherTool {

        @Tool("Gets the current weather for a city")
        public String getCurrentWeather(String city) {
            log.info("Tool called: getCurrentWeather({})", city);
            // Simulated weather data
            return String.format("Weather in %s: Sunny, 22°C, Humidity 65%%", city);
        }

        @Tool("Gets the current date and time")
        public String getCurrentDateTime() {
            log.info("Tool called: getCurrentDateTime()");
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    /**
     * Data Store Tool - stores and retrieves data
     */
    public static class DataStoreTool {

        private final Map<String, String> store;

        public DataStoreTool(Map<String, String> store) {
            this.store = store;
        }

        @Tool("Stores a value with a key")
        public String store(String key, String value) {
            log.info("Tool called: store({}, {})", key, value);
            store.put(key, value);
            return "Stored successfully: " + key;
        }

        @Tool("Retrieves a value by key")
        public String retrieve(String key) {
            log.info("Tool called: retrieve({})", key);
            String value = store.get(key);
            return value != null ? value : "Key not found: " + key;
        }

        @Tool("Lists all stored keys")
        public String listKeys() {
            log.info("Tool called: listKeys()");
            return "Stored keys: " + String.join(", ", store.keySet());
        }
    }
}
