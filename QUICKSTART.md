# Quick Start Guide

## Prerequisites Check

### 0. Java 25 or Higher Required

This project uses **Java 25**. Verify your installation:

```bash
java -version
```

You should see version 25 or higher. If not:

**Install Java 25:**
```bash
# macOS - Using Homebrew
brew install openjdk@25

# Or download from: https://jdk.java.net/25/
```

**Set JAVA_HOME (if needed):**
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 25)
```

### 1. Verify Ollama Installation
```bash
ollama --version
```

### 2. Start Ollama (if not running)
```bash
ollama serve
```
Keep this terminal open.

### 3. Pull Llama2 Model (in a new terminal)
```bash
ollama pull llama2
```
This will download ~4GB. Wait for completion.

### 4. Verify Model is Available
```bash
ollama list
```
You should see `llama2` in the list.

## Running the Application

### Option 1: Using Maven Wrapper (Recommended)
```bash
cd /Users/vicky/Documents/GitHub/langChain4jDemo
./mvnw clean install
./mvnw spring-boot:run
```

### Option 2: Using Installed Maven
```bash
cd /Users/vicky/Documents/GitHub/langChain4jDemo
mvn clean install
mvn spring-boot:run
```

Wait for the message: "Started LangChain4jDemoApplication"

## Testing the Application

### Quick Test (from another terminal)
```bash
curl -X POST http://localhost:8080/api/basic-chat/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Say hello!"}'
```

### Run All Tests
```bash
cd /Users/vicky/Documents/GitHub/langChain4jDemo
./test-api.sh
```

### Import Postman Collection
1. Open Postman
2. Import `LangChain4j-Demo-API.postman_collection.json`
3. Start testing all endpoints!

## Feature Examples

### 1. Basic Chat
```bash
curl -X POST http://localhost:8080/api/basic-chat/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "What is Java?"}'
```

### 2. Conversational Chat (with memory)
```bash
# First message
curl -X POST http://localhost:8080/api/conversational-chat/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "My name is Alice"}'

# Second message (AI remembers your name!)
curl -X POST http://localhost:8080/api/conversational-chat/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "What is my name?"}'
```

### 3. RAG - Add Knowledge & Ask Questions
```bash
# Add documents
curl -X POST http://localhost:8080/api/rag/add-documents \
  -H "Content-Type: application/json" \
  -d '{
    "texts": [
      "Our company sells electric cars",
      "We have 3 models: Basic, Pro, and Ultra",
      "Basic costs $30,000, Pro costs $45,000, Ultra costs $60,000"
    ]
  }'

# Ask question based on the documents
curl -X POST http://localhost:8080/api/rag/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "How much does the Pro model cost?", "maxResults": 3}'
```

### 4. AI Services - Code Generation
```bash
curl -X POST http://localhost:8080/api/ai-services/generate-code \
  -H "Content-Type: application/json" \
  -d '{
    "language": "Python",
    "description": "function to check if a string is palindrome"
  }'
```

### 5. Tools - Calculator
```bash
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "What is the square root of 144 multiplied by 5?"}'
```

## Troubleshooting

### Issue: "Connection refused" error
**Solution:** Ollama is not running. Start it:
```bash
ollama serve
```

### Issue: "Model not found"
**Solution:** Pull the model:
```bash
ollama pull llama2
```

### Issue: Slow responses
**Solutions:**
- Use a smaller model: `ollama pull phi`
- Update `application.properties`: `ollama.chat.model=phi`
- Restart the application

### Issue: Application won't start
**Solution:** Check if port 8080 is available:
```bash
lsof -i :8080
# Kill the process if needed
kill -9 <PID>
```

## Next Steps

1. **Read the full README.md** for detailed documentation
2. **Explore the code** in `src/main/java/org/jarvis/langchain4jdemo/`
3. **Try different models** by changing `ollama.chat.model` in `application.properties`
4. **Customize the examples** to your needs
5. **Build your own features** using the examples as templates

## Project Structure

```
src/main/java/org/jarvis/langchain4jdemo/
├── config/
│   └── LangChain4jConfig.java          # Spring configuration
├── service/
│   ├── BasicChatService.java           # Simple chat
│   ├── ConversationalChatService.java  # Chat with memory
│   ├── StreamingChatService.java       # Streaming responses
│   ├── EmbeddingService.java           # Vector embeddings
│   ├── RagService.java                 # RAG implementation
│   ├── AiServiceExampleService.java    # Declarative AI
│   └── ToolService.java                # Function calling
├── controller/
│   ├── BasicChatController.java        # REST endpoints
│   ├── ConversationalChatController.java
│   ├── RagController.java
│   ├── AiServicesController.java
│   └── ToolController.java
└── runner/
    └── LangChain4jDemoRunner.java      # Demo runner (optional)
```

## Available Endpoints

- **Basic Chat:** `POST /api/basic-chat/*`
- **Conversational:** `POST /api/conversational-chat/*`
- **RAG:** `POST /api/rag/*`
- **AI Services:** `POST /api/ai-services/*`
- **Tools:** `POST /api/tools/*`

See README.md for complete API documentation.

## Support

For issues or questions:
1. Check the logs in the console
2. Review the README.md
3. Verify Ollama is running: `curl http://localhost:11434/api/tags`

Happy coding! 🚀
