# LangChain4j Demo with Ollama

A comprehensive Spring Boot application demonstrating various features of **LangChain4j** with **Ollama** as the offline LLM.

---

## ⚠️ FIRST TIME SETUP

**Ollama must be installed and running before using this application!**

### Quick Setup (3 steps):
```bash
# 1. Install Ollama
brew install ollama

# 2. Start Ollama (Terminal 1 - keep running)
ollama serve

# 3. Download a model (Terminal 2)
ollama pull llama2
```

**📖 For detailed setup instructions, see [OLLAMA_SETUP.md](OLLAMA_SETUP.md)**

Once Ollama is running, start the application:
```bash
./mvnw spring-boot:run
```

---

## 📋 Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Architecture](#architecture)
- [Features Overview](#features-overview)
- [API Endpoints](#api-endpoints)
- [Usage Examples](#usage-examples)
- [Configuration](#configuration)

## ✨ Features

This demo showcases the following LangChain4j capabilities:

1. **Basic Chat** - Simple question-answering interactions
2. **Conversational Chat** - Multi-turn conversations with memory
3. **Streaming Responses** - Token-by-token response streaming
4. **Embeddings & Vector Search** - Semantic search using embeddings
5. **RAG (Retrieval Augmented Generation)** - Context-aware Q&A
6. **AI Services** - Declarative AI interfaces with annotations
7. **Tools/Function Calling** - LLM calling Java methods

## 💻 Technology Stack

- **Java 25** (Latest)
- **Spring Boot 4.0.0-M1**
- **LangChain4j 0.35.0**
- **Ollama** (Local LLM runtime)
- **Maven** (Build tool)
- **Lombok 1.18.36** (Code generation)

## 🔧 Prerequisites

### 0. Java 25 or Higher
This project requires **Java 25** or higher.

Check your Java version:
```bash
java -version
```

If you need to install Java 25:
```bash
# macOS - Using Homebrew
brew install openjdk@25

# Or download from: https://jdk.java.net/25/
```

### 1. Install Ollama
```bash
# macOS
brew install ollama

# Start Ollama service
ollama serve
```

### 2. Pull a Model
```bash
# Pull Llama 2 (recommended for this demo)
ollama pull llama2

# Or use other models:
# ollama pull mistral
# ollama pull codellama
# ollama pull llama3
```

### 3. Verify Ollama is Running
```bash
curl http://localhost:11434/api/tags
```

## 🚀 Setup

### 1. Clone and Build
```bash
cd langChain4jDemo
./mvnw clean install
```

### 2. Run the Application
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Spring Boot App                       │
├─────────────────────────────────────────────────────────┤
│  Controllers                                             │
│  ├── BasicChatController                                │
│  ├── ConversationalChatController                       │
│  ├── RagController                                      │
│  ├── AiServicesController                               │
│  └── ToolController                                     │
├─────────────────────────────────────────────────────────┤
│  Services                                                │
│  ├── BasicChatService                                   │
│  ├── ConversationalChatService                          │
│  ├── StreamingChatService                               │
│  ├── EmbeddingService                                   │
│  ├── RagService                                         │
│  ├── AiServiceExampleService                            │
│  └── ToolService                                        │
├─────────────────────────────────────────────────────────┤
│  LangChain4j Components                                  │
│  ├── ChatLanguageModel (Ollama)                        │
│  ├── StreamingChatLanguageModel                         │
│  ├── EmbeddingModel                                     │
│  ├── EmbeddingStore (In-Memory)                         │
│  └── ChatMemory                                         │
└─────────────────────────────────────────────────────────┘
                         │
                         ▼
              ┌──────────────────┐
              │   Ollama Server   │
              │  localhost:11434  │
              └──────────────────┘
```

## 📚 Features Overview

### 1. Basic Chat
Simple one-off interactions without context retention.

**Use Cases:**
- Quick questions
- Code generation
- Content creation

### 2. Conversational Chat with Memory
Multi-turn conversations where the AI remembers previous exchanges.

**Use Cases:**
- Interactive dialogues
- Context-dependent follow-ups
- Customer support chatbots

### 3. Streaming Responses
Responses delivered token-by-token for better UX.

**Use Cases:**
- Long-form content generation
- Real-time chat interfaces
- Progressive content display

### 4. Embeddings & Vector Search
Convert text to vectors and perform semantic search.

**Use Cases:**
- Document similarity search
- Semantic search engines
- Content recommendation

### 5. RAG (Retrieval Augmented Generation)
Answer questions based on your custom knowledge base.

**Use Cases:**
- Knowledge base Q&A
- Document-based assistants
- Enterprise search

### 6. AI Services
Define AI behavior using Java interfaces and annotations.

**Use Cases:**
- Type-safe AI interactions
- Declarative AI programming
- Service-oriented architecture

### 7. Tools/Function Calling
Allow the LLM to call Java methods to perform actions.

**Use Cases:**
- Calculator functions
- API integrations
- Database operations

## 🔌 API Endpoints

### Basic Chat
```bash
# Simple chat
POST /api/basic-chat/chat
Body: {"message": "Hello, how are you?"}

# Ask a question
POST /api/basic-chat/question
Body: {"message": "What is the capital of France?"}

# Generate content
POST /api/basic-chat/generate-content
Body: {"message": "Write a short poem about spring"}

# Generate code
POST /api/basic-chat/generate-code
Body: {"message": "Create a Java function to calculate fibonacci"}
```

### Conversational Chat
```bash
# Chat with memory
POST /api/conversational-chat/chat
Body: {"message": "My name is John"}

# Follow-up (AI remembers your name)
POST /api/conversational-chat/chat
Body: {"message": "What is my name?"}

# Clear memory
POST /api/conversational-chat/clear

# Get conversation size
GET /api/conversational-chat/size
```

### RAG (Knowledge Base)
```bash
# Add a single document
POST /api/rag/add-document
Body: {
  "text": "LangChain4j is a Java library for building LLM applications",
  "id": "doc1"
}

# Add multiple documents
POST /api/rag/add-documents
Body: {
  "texts": [
    "Ollama is a tool for running LLMs locally",
    "Spring Boot is a Java framework",
    "Vector embeddings represent text as numbers"
  ]
}

# Add long document (auto-chunks)
POST /api/rag/add-long-document
Body: {"text": "Very long document content..."}

# Search similar content
POST /api/rag/search
Body: {"query": "What is LangChain?", "maxResults": 3}

# Ask question with RAG
POST /api/rag/ask
Body: {"question": "What is Ollama?", "maxResults": 3}

# Ask with sources
POST /api/rag/ask-with-sources
Body: {"question": "What is Spring Boot?", "maxResults": 3}
```

### AI Services
```bash
# Chat assistant
POST /api/ai-services/chat
Body: {"message": "Explain quantum computing"}

# Generate code
POST /api/ai-services/generate-code
Body: {
  "language": "Python",
  "description": "function to sort a list of numbers"
}

# Sentiment analysis
POST /api/ai-services/sentiment
Body: {"text": "I love this product! It's amazing!"}

# Summarize text
POST /api/ai-services/summarize
Body: {
  "text": "Long article text here...",
  "maxWords": 50
}

# Translate
POST /api/ai-services/translate
Body: {
  "text": "Hello, how are you?",
  "targetLanguage": "Spanish"
}
```

### Tools (Function Calling)
```bash
# Chat with calculator tools
POST /api/tools/chat
Body: {"message": "What is 25 * 4 + 10?"}

# Chat with weather tools
POST /api/tools/chat
Body: {"message": "What's the weather in New York?"}

# Chat with data store
POST /api/tools/chat
Body: {"message": "Store 'my_key' with value 'Hello World'"}

# Retrieve stored data
POST /api/tools/chat
Body: {"message": "What is stored in 'my_key'?"}
```

## 📝 Usage Examples

### Example 1: Simple Q&A
```bash
curl -X POST http://localhost:8080/api/basic-chat/question \
  -H "Content-Type: application/json" \
  -d '{"message": "What is artificial intelligence?"}'
```

### Example 2: Conversational Chat
```bash
# First message
curl -X POST http://localhost:8080/api/conversational-chat/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "I have a dog named Max"}'

# Follow-up (remembers context)
curl -X POST http://localhost:8080/api/conversational-chat/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "What is my pet'\''s name?"}'
```

### Example 3: RAG Knowledge Base
```bash
# Step 1: Add knowledge
curl -X POST http://localhost:8080/api/rag/add-documents \
  -H "Content-Type: application/json" \
  -d '{
    "texts": [
      "Our company was founded in 2020 in San Francisco.",
      "We specialize in AI and machine learning solutions.",
      "Our CEO is Jane Smith, who has 15 years of experience."
    ]
  }'

# Step 2: Ask questions
curl -X POST http://localhost:8080/api/rag/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "When was the company founded?", "maxResults": 3}'

curl -X POST http://localhost:8080/api/rag/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "Who is the CEO?", "maxResults": 3}'
```

### Example 4: Code Generation
```bash
curl -X POST http://localhost:8080/api/ai-services/generate-code \
  -H "Content-Type: application/json" \
  -d '{
    "language": "Java",
    "description": "bubble sort algorithm with comments"
  }'
```

### Example 5: Tool Usage (Calculator)
```bash
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Calculate the square root of 144 and multiply it by 5"}'
```

## ⚙️ Configuration

Edit `src/main/resources/application.properties`:

```properties
# Ollama Configuration
ollama.base.url=http://localhost:11434
ollama.chat.model=llama2  # Change to mistral, codellama, etc.
ollama.embedding.model=llama2
ollama.timeout=300s

# Server Configuration
server.port=8080

# Logging
logging.level.dev.langchain4j=DEBUG
logging.level.org.jarvis.langchain4jdemo=INFO
```

### Supported Models
- `llama2` - General purpose (recommended)
- `mistral` - Fast and efficient
- `codellama` - Best for code generation
- `llama3` - Latest version
- `phi` - Smaller, faster model

## 🎯 Key Concepts Explained

### 1. ChatLanguageModel vs StreamingChatLanguageModel
- **ChatLanguageModel**: Blocking, waits for complete response
- **StreamingChatLanguageModel**: Non-blocking, streams tokens as they're generated

### 2. Embeddings
- Convert text to numerical vectors (arrays of floats)
- Similar meanings = similar vectors
- Enables semantic search

### 3. Embedding Store
- Database for storing text embeddings
- Allows fast similarity search
- In-memory for demos, use persistent stores for production

### 4. Chat Memory
- Maintains conversation context
- MessageWindowChatMemory: Keeps last N messages
- TokenWindowChatMemory: Keeps last N tokens

### 5. AI Services
- Declarative way to define AI interactions
- Uses Java interfaces with annotations
- Type-safe and maintainable

### 6. Tools (Function Calling)
- LLM can decide to call Java methods
- Enables actions beyond text generation
- Requires model support (Llama 2 supports this)

## 🔍 Testing the Application

### 1. Verify Ollama Connection
```bash
curl http://localhost:11434/api/tags
```

### 2. Test Basic Endpoint
```bash
curl http://localhost:8080/api/basic-chat/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Say hello!"}'
```

### 3. Check Logs
The application logs show:
- LangChain4j debug information
- Request/response details
- Tool calls
- Embedding operations

## 🐛 Troubleshooting

### Issue: Connection refused to Ollama
**Solution**: Make sure Ollama is running: `ollama serve`

### Issue: Model not found
**Solution**: Pull the model: `ollama pull llama2`

### Issue: Slow responses
**Solution**: 
- Use a smaller model (phi, mistral)
- Increase timeout in application.properties
- Reduce message history in memory

### Issue: Out of memory
**Solution**:
- Close other applications
- Use a smaller model
- Reduce embedding store size

## 📖 Learn More

- [LangChain4j Documentation](https://docs.langchain4j.dev/)
- [Ollama Documentation](https://ollama.ai/docs)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)

## 🤝 Contributing

Feel free to experiment and extend this demo with:
- Different embedding stores (PostgreSQL, Redis)
- Document loaders (PDF, Word, HTML)
- Advanced RAG techniques
- Custom tools
- Web UI integration

## 📄 License

This is a demo project for learning purposes.

---

**Happy Learning! 🚀**
