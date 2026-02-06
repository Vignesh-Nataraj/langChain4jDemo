package org.jarvis.langchain4jdemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class LangChain4jDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(LangChain4jDemoApplication.class, args);
    }

    @Bean
    CommandLineRunner welcome() {
        return args -> {
            log.info("=".repeat(80));
            log.info("🚀 LangChain4j Demo Application with Ollama Started Successfully!");
            log.info("=".repeat(80));
            log.info("");
            log.info("📚 Features Available:");
            log.info("   1. Basic Chat              - POST /api/basic-chat/chat");
            log.info("   2. Conversational Chat     - POST /api/conversational-chat/chat");
            log.info("   3. RAG (Knowledge Base)    - POST /api/rag/add-documents & /api/rag/ask");
            log.info("   4. AI Services             - POST /api/ai-services/*");
            log.info("   5. Tools (Function Call)   - POST /api/tools/chat");
            log.info("");
            log.info("🧪 Quick Test:");
            log.info("   curl -X POST http://localhost:8080/api/basic-chat/chat \\");
            log.info("     -H \"Content-Type: application/json\" \\");
            log.info("     -d '{\"message\": \"Hello!\"}'");
            log.info("");
            log.info("📖 Documentation: Check README.md and QUICKSTART.md");
            log.info("📦 Postman Collection: Import LangChain4j-Demo-API.postman_collection.json");
            log.info("🧪 Test Script: Run ./test-api.sh");
            log.info("");
            log.info("=".repeat(80));
        };
    }

}
