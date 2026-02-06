# 🚀 LangChain4j Demo Application - Complete Feature Guide

## 📋 Table of Contents
- [Overview](#overview)
- [Technology Stack](#technology-stack)
- [LangChain4j Integration](#langchain4j-integration)
- [Features](#features)
- [API Reference](#api-reference)
- [Configuration](#configuration)
- [How to Run](#how-to-run)
- [Example Usage](#example-usage)

---

## 🎯 Overview

This is a comprehensive Spring Boot application that demonstrates all major features of **LangChain4j** - a powerful Java framework for building LLM-powered applications. The application uses **Ollama** for local LLM hosting, providing privacy-focused AI capabilities without external API dependencies.

### What is LangChain4j?

LangChain4j is a Java implementation of the LangChain framework that provides:
- 🤖 Simple integration with various LLM providers
- 💬 Conversation memory management
- 🔍 RAG (Retrieval Augmented Generation) capabilities
- 🛠️ Function/Tool calling for LLM-to-code interaction
- 📝 Document processing and embeddings
- 🎯 Declarative AI services with annotations

---

## 🛠️ Technology Stack

### Core Technologies
- **Java 25** - Latest Java features with preview support
- **Spring Boot 4.0.0-M1** - Modern Spring framework
- **Maven** - Dependency management and build tool

### LangChain4j Dependencies
- **langchain4j-core** (v0.36.2) - Core LangChain4j functionality
- **langchain4j-ollama** (v0.36.2) - Ollama integration for local LLMs
- **langchain4j-embeddings-all-minilm-l6-v2** (v0.36.2) - Fast local embeddings
- **langchain4j-document-parser-apache-tika** (v0.36.2) - Document parsing

### AI Infrastructure
- **Ollama** - Local LLM runtime
- **llama3.2** (3.2B parameters) - Primary chat model
- **nomic-embed-text** - Alternative embedding model
- **AllMiniLmL6V2** - Local embedding model (274MB)

---

## 🔗 LangChain4j Integration

### Key LangChain4j Features Implemented

#### 1. **Chat Models** (`ChatLanguageModel`)
- **Purpose**: Synchronous chat completions
- **Implementation**: `OllamaChatModel`
- **Configuration**: Base URL, model name, temperature, timeout
- **Used in**: BasicChatService, RagService, AI Services

#### 2. **Streaming Models** (`StreamingChatLanguageModel`)
- **Purpose**: Asynchronous streaming responses
- **Implementation**: `OllamaStreamingChatModel`
- **Benefits**: Real-time token generation, better UX for long responses
- **Configuration**: Same as chat models with streaming callbacks

#### 3. **Embedding Models** (`EmbeddingModel`)
- **Purpose**: Convert text to vector representations
- **Implementation**: `AllMiniLmL6V2EmbeddingModel` (local ONNX model)
- **Vector Dimension**: 384
- **Use Cases**: Semantic search, RAG, similarity matching

#### 4. **Embedding Store** (`EmbeddingStore<TextSegment>`)
- **Purpose**: Vector database for storing embeddings
- **Implementation**: `InMemoryEmbeddingStore`
- **Capabilities**: Add, search, retrieve by similarity
- **Used in**: RAG operations, document search

#### 5. **Chat Memory** (`MessageWindowChatMemory`)
- **Purpose**: Maintain conversation context
- **Configuration**: Window size of 10 messages
- **Behavior**: Automatically manages message history
- **Used in**: Conversational chat, tool-enabled assistants

#### 6. **AI Services** (Declarative Interfaces)
- **Purpose**: Type-safe AI interactions via annotations
- **Key Annotations**:
  - `@SystemMessage` - Define AI behavior/personality
  - `@UserMessage` - Template user prompts
  - `@V("variable")` - Template variable injection
- **Benefits**: Clean code, reusable, testable

#### 7. **Tools/Functions** (`@Tool` annotation)
- **Purpose**: Allow LLM to call Java methods
- **Mechanism**: Function calling / tool use
- **Implementation**: Automatic function discovery and execution
- **Use Cases**: Calculators, APIs, database operations

#### 8. **Document Processing**
- **Document Splitters**: Split long texts into chunks
  - `DocumentSplitters.recursive(300, 50)` - 300 chars per chunk, 50 char overlap
- **Text Segmentation**: `TextSegment` for managing text pieces
- **Use Case**: Processing long documents for RAG

---

## 🎨 Features

### 1. 💬 Basic Chat
**LangChain4j Feature**: `ChatLanguageModel`

Simple, stateless interactions with the LLM.

**Endpoints**:
- `POST /api/basic-chat/chat` - General chat
- `POST /api/basic-chat/question` - Ask questions
- `POST /api/basic-chat/generate-content` - Creative content generation
- `POST /api/basic-chat/generate-code` - Code generation

**How LangChain4j is Used**:
```java
ChatLanguageModel model = OllamaChatModel.builder()
    .baseUrl("http://localhost:11434")
    .modelName("llama3.2")
    .build();

String response = model.generate(userMessage);
```

**Example Request**:
```bash
curl -X POST http://localhost:8080/api/basic-chat/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Explain quantum computing in simple terms"}'
```

---

### 2. 🧠 Conversational Chat with Memory
**LangChain4j Feature**: `MessageWindowChatMemory`

Maintains conversation context across multiple turns.

**Endpoints**:
- `POST /api/conversational-chat/chat` - Chat with context
- `POST /api/conversational-chat/clear` - Clear conversation history
- `GET /api/conversational-chat/size` - Get conversation size

**How LangChain4j is Used**:
```java
MessageWindowChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);

// Add messages to memory
memory.add(UserMessage.from(userMessage));
var messages = memory.messages();

// Generate with context
String response = chatModel.generate(messages).content().text();
memory.add(AiMessage.from(response));
```

**Example Conversation**:
```bash
# First message
curl -X POST http://localhost:8080/api/conversational-chat/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "My name is Alice"}'

# Follow-up - AI remembers your name!
curl -X POST http://localhost:8080/api/conversational-chat/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "What is my name?"}'
```

---

### 3. 🔍 RAG (Retrieval Augmented Generation)
**LangChain4j Features**: `EmbeddingModel`, `EmbeddingStore`, `DocumentSplitters`

Answer questions based on custom knowledge base.

**Endpoints**:
- `POST /api/rag/add-document` - Add single document
- `POST /api/rag/add-documents` - Batch add documents
- `POST /api/rag/add-long-document` - Add with automatic chunking
- `POST /api/rag/search` - Semantic search
- `POST /api/rag/ask` - RAG-based Q&A
- `POST /api/rag/ask-with-sources` - Q&A with source attribution

**How LangChain4j is Used**:
```java
// 1. Embed and store documents
Embedding embedding = embeddingModel.embed(textSegment).content();
embeddingStore.add(embedding, textSegment);

// 2. Find relevant context
Embedding queryEmbedding = embeddingModel.embed(question).content();
List<EmbeddingMatch<TextSegment>> matches = 
    embeddingStore.findRelevant(queryEmbedding, 3);

// 3. Build context and generate answer
String context = matches.stream()
    .map(match -> match.embedded().text())
    .collect(Collectors.joining("\n\n"));
    
String prompt = "Context: " + context + "\n\nQuestion: " + question;
String answer = chatModel.generate(prompt);
```

**Example Usage**:
```bash
# 1. Add knowledge
curl -X POST http://localhost:8080/api/rag/add-documents \
  -H "Content-Type: application/json" \
  -d '{
    "texts": [
      "The Eiffel Tower is located in Paris, France. It was built in 1889.",
      "Paris is the capital city of France with a population of 2.2 million.",
      "The tower is 330 meters tall and made of iron."
    ]
  }'

# 2. Ask questions
curl -X POST http://localhost:8080/api/rag/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "How tall is the Eiffel Tower?", "maxResults": 3}'

# 3. Search for similar content
curl -X POST http://localhost:8080/api/rag/search \
  -H "Content-Type: application/json" \
  -d '{"query": "Paris landmarks", "maxResults": 3}'
```

**Document Splitting Example**:
```bash
# Long documents are automatically chunked
curl -X POST http://localhost:8080/api/rag/add-long-document \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Very long document content... (multiple pages)"
  }'
```

---

### 4. 🎯 AI Services (Declarative APIs)
**LangChain4j Feature**: `AiServices` with annotations

Define AI behavior through annotated interfaces.

**Endpoints**:
- `POST /api/ai-services/chat` - General assistant
- `POST /api/ai-services/generate-code` - Code generation
- `POST /api/ai-services/sentiment` - Sentiment analysis
- `POST /api/ai-services/summarize` - Text summarization
- `POST /api/ai-services/translate` - Translation

**How LangChain4j is Used**:
```java
interface Translator {
    @SystemMessage("You are a professional translator.")
    @UserMessage("Translate to {{language}}: {{text}}")
    String translate(@V("text") String text, @V("language") String language);
}

Translator translator = AiServices.create(Translator.class, chatModel);
String result = translator.translate("Hello World", "Spanish");
```

**Example Requests**:

**Code Generation**:
```bash
curl -X POST http://localhost:8080/api/ai-services/generate-code \
  -H "Content-Type: application/json" \
  -d '{
    "language": "Python",
    "description": "Binary search algorithm"
  }'
```

**Sentiment Analysis**:
```bash
curl -X POST http://localhost:8080/api/ai-services/sentiment \
  -H "Content-Type: application/json" \
  -d '{
    "text": "I absolutely love this product! Best purchase ever!"
  }'
```

**Summarization**:
```bash
curl -X POST http://localhost:8080/api/ai-services/summarize \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Long article text here...",
    "maxWords": 50
  }'
```

**Translation**:
```bash
curl -X POST http://localhost:8080/api/ai-services/translate \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Hello, how are you?",
    "targetLanguage": "French"
  }'
```

---

### 5. 🛠️ Tools / Function Calling
**LangChain4j Feature**: `@Tool` annotation with `AiServices`

Let the LLM call Java methods to perform actions.

**Endpoint**:
- `POST /api/tools/chat` - Chat with tool-enabled assistant

**Available Tools**:

#### Calculator Tools:
- `add(a, b)` - Addition
- `subtract(a, b)` - Subtraction
- `multiply(a, b)` - Multiplication
- `divide(a, b)` - Division
- `sqrt(number)` - Square root

#### Weather Tools:
- `getCurrentWeather(city)` - Get weather info
- `getCurrentDateTime()` - Get current date/time

#### Data Store Tools:
- `store(key, value)` - Store data
- `retrieve(key)` - Retrieve data
- `listKeys()` - List all keys

**How LangChain4j is Used**:
```java
public class CalculatorTool {
    @Tool("Adds two numbers")
    public double add(double a, double b) {
        return a + b;
    }
}

Assistant assistant = AiServices.builder(Assistant.class)
    .chatLanguageModel(chatModel)
    .tools(new CalculatorTool())
    .build();

// LLM automatically calls add() when needed
String response = assistant.chat("What is 25 + 17?");
```

**Example Requests**:

**Math Operations**:
```bash
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Calculate the square root of 144"}'
```

**Weather Query**:
```bash
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "What is the weather in Paris?"}'
```

**Data Operations**:
```bash
# Store data
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Store the value OpenAI with key company"}'

# Retrieve data
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "What is stored under the key company?"}'
```

**Complex Query**:
```bash
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Calculate 25 * 4, then add 100, then divide by 5"}'
```

---

## 📚 API Reference

### Complete Endpoint List

| Category | Method | Endpoint | Description |
|----------|--------|----------|-------------|
| **Basic Chat** | POST | `/api/basic-chat/chat` | Simple chat |
| | POST | `/api/basic-chat/question` | Ask question |
| | POST | `/api/basic-chat/generate-content` | Generate content |
| | POST | `/api/basic-chat/generate-code` | Generate code |
| **Conversational** | POST | `/api/conversational-chat/chat` | Chat with memory |
| | POST | `/api/conversational-chat/clear` | Clear memory |
| | GET | `/api/conversational-chat/size` | Get history size |
| **RAG** | POST | `/api/rag/add-document` | Add document |
| | POST | `/api/rag/add-documents` | Batch add documents |
| | POST | `/api/rag/add-long-document` | Add long document |
| | POST | `/api/rag/search` | Semantic search |
| | POST | `/api/rag/ask` | Ask question |
| | POST | `/api/rag/ask-with-sources` | Ask with sources |
| **AI Services** | POST | `/api/ai-services/chat` | General chat |
| | POST | `/api/ai-services/generate-code` | Code generation |
| | POST | `/api/ai-services/sentiment` | Sentiment analysis |
| | POST | `/api/ai-services/summarize` | Summarization |
| | POST | `/api/ai-services/translate` | Translation |
| **Tools** | POST | `/api/tools/chat` | Tool-enabled chat |

---

## ⚙️ Configuration

### Application Properties
Location: `src/main/resources/application.properties`

```properties
# Spring Boot Configuration
spring.application.name=langChain4jDemo
server.port=8080

# Ollama Configuration
ollama.base.url=http://localhost:11434
ollama.chat.model=llama3.2
ollama.embedding.model=llama3.2
ollama.timeout=300s

# Logging
logging.level.dev.langchain4j=DEBUG
logging.level.org.jarvis.langchain4jdemo=INFO
```

### LangChain4j Bean Configuration

**Chat Model Configuration**:
```java
@Bean
public ChatLanguageModel chatLanguageModel() {
    return OllamaChatModel.builder()
            .baseUrl(ollamaBaseUrl)        // Ollama server URL
            .modelName(chatModel)           // Model name (llama3.2)
            .timeout(timeout)               // Request timeout
            .temperature(0.7)               // Creativity (0.0-1.0)
            .logRequests(true)              // Log requests
            .logResponses(true)             // Log responses
            .build();
}
```

**Embedding Model Configuration**:
```java
@Bean
public EmbeddingModel embeddingModel() {
    return new AllMiniLmL6V2EmbeddingModel(); // Local ONNX model
}
```

**Chat Memory Configuration**:
```java
@Bean
public MessageWindowChatMemory chatMemory() {
    return MessageWindowChatMemory.withMaxMessages(10); // Keep last 10 messages
}
```

---

## 🚀 How to Run

### Prerequisites

1. **Java 25** installed
2. **Maven 3.8+** installed
3. **Ollama** installed and running
4. **Models downloaded**:
   ```bash
   ollama pull llama3.2
   ollama pull nomic-embed-text  # Optional
   ```

### Start Ollama
```bash
# Start Ollama server
ollama serve

# Verify models
ollama list
```

### Build and Run Application

**Option 1: Maven Spring Boot Plugin**
```bash
cd /path/to/langChain4jDemo
mvn clean install
mvn spring-boot:run
```

**Option 2: JAR File**
```bash
mvn clean package
java --enable-preview -jar target/langChain4jDemo-0.0.1-SNAPSHOT.jar
```

### Verify Application Started
Check logs for:
```
🚀 LangChain4j Demo Application with Ollama Started Successfully!

📚 Features Available:
   1. Basic Chat              - POST /api/basic-chat/chat
   2. Conversational Chat     - POST /api/conversational-chat/chat
   3. RAG (Knowledge Base)    - POST /api/rag/add-documents & /api/rag/ask
   4. AI Services             - POST /api/ai-services/*
   5. Tools (Function Call)   - POST /api/tools/chat
```

Application runs on: **http://localhost:8080**

---

## 💡 Example Usage Scenarios

### Scenario 1: Build a Knowledge Base Assistant

```bash
# Step 1: Add company knowledge
curl -X POST http://localhost:8080/api/rag/add-documents \
  -H "Content-Type: application/json" \
  -d '{
    "texts": [
      "Our company was founded in 2010 by John Doe.",
      "We have 500 employees across 10 offices worldwide.",
      "Our main product is CloudSync - a data synchronization platform.",
      "Customer support is available 24/7 via email at support@company.com"
    ]
  }'

# Step 2: Ask questions
curl -X POST http://localhost:8080/api/rag/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "How many employees does the company have?"}'

curl -X POST http://localhost:8080/api/rag/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "What is the main product?"}'
```

### Scenario 2: Create a Code Assistant

```bash
# Generate Python code
curl -X POST http://localhost:8080/api/ai-services/generate-code \
  -H "Content-Type: application/json" \
  -d '{
    "language": "Python",
    "description": "Function to find prime numbers up to N"
  }'

# Generate Java code
curl -X POST http://localhost:8080/api/ai-services/generate-code \
  -H "Content-Type: application/json" \
  -d '{
    "language": "Java",
    "description": "REST controller for user management"
  }'
```

### Scenario 3: Multi-turn Conversation

```bash
# Turn 1
curl -X POST http://localhost:8080/api/conversational-chat/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "I am planning a trip to Japan"}'

# Turn 2 - AI remembers context
curl -X POST http://localhost:8080/api/conversational-chat/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "What cities should I visit?"}'

# Turn 3 - Still remembers the trip
curl -X POST http://localhost:8080/api/conversational-chat/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "How many days would you recommend?"}'
```

### Scenario 4: Tool-Enhanced Assistant

```bash
# Complex calculation
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "I have $1000. If I multiply it by 1.05 for 5 years compound interest, how much will I have?"}'

# Store and retrieve
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Remember that my favorite color is blue"}'

curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "What is my favorite color?"}'
```

---

## 🎓 LangChain4j Concepts Explained

### 1. **Embeddings & Vector Search**
- **What**: Convert text to numerical vectors (embeddings)
- **Why**: Enable semantic similarity search beyond keyword matching
- **Model Used**: AllMiniLmL6V2 (384-dimensional vectors)
- **Use Case**: Find documents that are conceptually similar, not just keyword matches

### 2. **RAG (Retrieval Augmented Generation)**
- **What**: Enhance LLM with external knowledge
- **Process**: 
  1. Embed and store documents
  2. Retrieve relevant context for queries
  3. Generate answers with retrieved context
- **Benefits**: Accurate, up-to-date, source-attributed answers

### 3. **Chat Memory**
- **What**: Maintain conversation history
- **Implementation**: MessageWindowChatMemory (sliding window)
- **Configuration**: Last 10 messages retained
- **Use Case**: Multi-turn conversations with context

### 4. **AI Services**
- **What**: Declarative AI interfaces using annotations
- **Benefits**: 
  - Type-safe
  - Reusable
  - Testable
  - Clean separation of concerns
- **Annotations**: @SystemMessage, @UserMessage, @V()

### 5. **Tools/Functions**
- **What**: Let LLM call Java methods
- **How**: LLM analyzes user request, decides which tool to call, extracts parameters
- **Benefits**: Bridge AI and traditional code, extend LLM capabilities

### 6. **Document Processing**
- **Splitting**: Break long documents into chunks
- **Chunk Size**: 300 characters
- **Overlap**: 50 characters (maintains context)
- **Why**: LLMs have context limits; chunking enables processing large documents

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     REST Controllers                         │
│  BasicChat │ Conversational │ RAG │ AiServices │ Tools     │
└────────────┬────────────────┬─────┬─────────────┬───────────┘
             │                │     │             │
             ▼                ▼     ▼             ▼
┌─────────────────────────────────────────────────────────────┐
│                         Services                             │
│   BasicChatService │ ConversationalChatService              │
│   RagService │ EmbeddingService │ AiServiceExampleService   │
│   ToolService                                                │
└────────────┬─────────────────────────────────┬──────────────┘
             │                                  │
             ▼                                  ▼
┌─────────────────────────────────────────────────────────────┐
│                   LangChain4j Beans                         │
│  ChatLanguageModel │ EmbeddingModel │ EmbeddingStore        │
│  ChatMemory │ StreamingChatModel                            │
└────────────┬─────────────────────────────────┬──────────────┘
             │                                  │
             ▼                                  ▼
┌─────────────────────────┐    ┌───────────────────────────┐
│    Ollama (llama3.2)    │    │  AllMiniLmL6V2 (Local)   │
│  Chat & Text Generation │    │    Embeddings Model       │
└─────────────────────────┘    └───────────────────────────┘
```

---

## 🔧 Troubleshooting

### Ollama Not Running
```bash
# Check if Ollama is running
curl http://localhost:11434/api/tags

# Start Ollama
ollama serve
```

### Model Not Found
```bash
# Download required model
ollama pull llama3.2

# List downloaded models
ollama list
```

### Application Won't Start
```bash
# Check Java version
java --version  # Should be 25

# Clean and rebuild
mvn clean install

# Check port 8080 is free
lsof -i :8080
```

### Slow Responses
- Use smaller model: `ollama pull llama3.2` (3.2B is faster than 7B/13B)
- Increase timeout in `application.properties`
- Check system resources (RAM, CPU)

---

## 📊 Performance Tips

1. **Model Selection**:
   - `llama3.2` (3.2B) - Fast, good quality
   - `llama2` (7B) - Balanced
   - `mistral` - Fast and efficient

2. **Embedding Model**:
   - AllMiniLmL6V2 - Fast, local, no API calls
   - Good balance of speed and quality

3. **Memory Management**:
   - MessageWindowChatMemory with size 10 - prevents memory bloat
   - InMemoryEmbeddingStore - fast but limited by RAM

4. **Timeout Configuration**:
   - Default: 300s
   - Adjust based on model and hardware

---

## 🌟 Key Benefits of This Architecture

1. **Privacy**: All processing happens locally with Ollama
2. **No API Costs**: No charges for OpenAI, Claude, etc.
3. **Offline Capable**: Works without internet connection
4. **Type-Safe**: Java's strong typing with LangChain4j
5. **Spring Integration**: Leverages Spring Boot ecosystem
6. **Modular**: Easy to add/remove features
7. **Production Ready**: Logging, error handling, REST APIs

---

## 📖 Learn More

### LangChain4j Resources
- [Official Documentation](https://docs.langchain4j.dev/)
- [GitHub Repository](https://github.com/langchain4j/langchain4j)
- [Examples](https://github.com/langchain4j/langchain4j-examples)

### Ollama Resources
- [Official Website](https://ollama.ai/)
- [Model Library](https://ollama.ai/library)
- [Documentation](https://github.com/ollama/ollama)

### Concepts
- [RAG Explained](https://docs.langchain4j.dev/tutorials/rag)
- [Embeddings Guide](https://docs.langchain4j.dev/tutorials/embeddings)
- [AI Services](https://docs.langchain4j.dev/tutorials/ai-services)

---

## 🤝 Contributing

To extend this application:

1. Add new controller in `controller/` package
2. Implement service in `service/` package
3. Use existing LangChain4j beans from `LangChain4jConfig`
4. Add endpoint documentation here

---

## 📝 License

This is a demo application for learning LangChain4j.

---

## 👨‍💻 Author

Built as a comprehensive demonstration of LangChain4j capabilities.

**Last Updated**: February 6, 2026  
**LangChain4j Version**: 0.36.2  
**Spring Boot Version**: 4.0.0-M1  
**Java Version**: 25

---

## 🎉 Summary

This application demonstrates:
- ✅ 5 Major LangChain4j Features
- ✅ 20+ REST API Endpoints
- ✅ Local LLM with Ollama
- ✅ RAG Implementation
- ✅ Function Calling
- ✅ Conversation Memory
- ✅ AI Services Pattern
- ✅ Document Processing
- ✅ Semantic Search
- ✅ Production-Ready Code

**Perfect for**: Learning LangChain4j, building AI applications in Java, understanding RAG, exploring LLM capabilities locally.
