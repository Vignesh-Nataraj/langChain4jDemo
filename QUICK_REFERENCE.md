# 🎯 LangChain4j Demo - Quick Reference Guide

## 🚀 Quick Start

```bash
# 1. Start Ollama
ollama serve

# 2. Verify models
ollama list  # Should show llama3.2

# 3. Start application
mvn spring-boot:run

# 4. Application runs on: http://localhost:8080
```

---

## 📋 Feature Summary

| # | Feature | LangChain4j Component | Endpoint Base |
|---|---------|----------------------|---------------|
| 1 | **Basic Chat** | `ChatLanguageModel` | `/api/basic-chat` |
| 2 | **Conversational Chat** | `MessageWindowChatMemory` | `/api/conversational-chat` |
| 3 | **RAG** | `EmbeddingModel` + `EmbeddingStore` | `/api/rag` |
| 4 | **AI Services** | `AiServices` + Annotations | `/api/ai-services` |
| 5 | **Tools/Functions** | `@Tool` annotation | `/api/tools` |

---

## 🎨 Features Matrix

### 1️⃣ Basic Chat - Stateless Conversations

**Use Case**: Simple Q&A, content generation, code generation

| Endpoint | Purpose | Example Input |
|----------|---------|---------------|
| `/chat` | General chat | `"Explain AI"` |
| `/question` | Ask questions | `"What is Java?"` |
| `/generate-content` | Creative writing | `"Write a poem about spring"` |
| `/generate-code` | Code generation | `"Python fibonacci function"` |

**LangChain4j Magic**:
```java
ChatLanguageModel model = OllamaChatModel.builder()...
String response = model.generate(prompt);
```

---

### 2️⃣ Conversational Chat - Multi-turn Conversations

**Use Case**: Chatbots, assistants, context-aware interactions

| Endpoint | Purpose |
|----------|---------|
| `/chat` | Chat with memory |
| `/clear` | Reset conversation |
| `/size` | Get history size |

**LangChain4j Magic**:
```java
MessageWindowChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);
memory.add(UserMessage.from(input));
String response = model.generate(memory.messages());
memory.add(AiMessage.from(response));
```

**Example Flow**:
```
You: "My name is Alice"
AI:  "Nice to meet you, Alice!"

You: "What's my name?"
AI:  "Your name is Alice."  ← Remembers!
```

---

### 3️⃣ RAG - Knowledge Base Q&A

**Use Case**: Custom knowledge bases, document search, accurate Q&A

| Endpoint | Purpose | Input |
|----------|---------|-------|
| `/add-document` | Add single doc | `{text, id}` |
| `/add-documents` | Batch add | `{texts: [...]}` |
| `/add-long-document` | Add with chunking | `{text}` |
| `/search` | Semantic search | `{query, maxResults}` |
| `/ask` | RAG Q&A | `{question, maxResults}` |
| `/ask-with-sources` | Q&A + sources | `{question, maxResults}` |

**LangChain4j Magic**:
```java
// 1. Embed & Store
Embedding emb = embeddingModel.embed(text).content();
embeddingStore.add(emb, segment);

// 2. Search
List<EmbeddingMatch> matches = embeddingStore.findRelevant(queryEmb, 3);

// 3. Generate with context
String context = matches.stream()...
String answer = chatModel.generate("Context: " + context + "\nQ: " + question);
```

**RAG Flow**:
```
Add Docs → Embed → Store → Query → Find Similar → Build Context → Generate Answer
```

---

### 4️⃣ AI Services - Declarative AI

**Use Case**: Type-safe AI, reusable AI components, clean code

| Endpoint | Purpose | Parameters |
|----------|---------|------------|
| `/chat` | General assistant | `{message}` |
| `/generate-code` | Code generator | `{language, description}` |
| `/sentiment` | Sentiment analysis | `{text}` |
| `/summarize` | Text summarization | `{text, maxWords}` |
| `/translate` | Translation | `{text, targetLanguage}` |

**LangChain4j Magic**:
```java
interface Translator {
    @SystemMessage("You are a professional translator")
    @UserMessage("Translate to {{lang}}: {{text}}")
    String translate(@V("text") String text, @V("lang") String lang);
}

Translator t = AiServices.create(Translator.class, model);
String result = t.translate("Hello", "Spanish");
```

**Benefits**:
- ✅ Type-safe
- ✅ Reusable
- ✅ Testable
- ✅ Clean code

---

### 5️⃣ Tools - Function Calling

**Use Case**: LLM calls Java methods, extend AI with code

| Tool Category | Available Functions |
|--------------|-------------------|
| **Calculator** | `add`, `subtract`, `multiply`, `divide`, `sqrt` |
| **Weather** | `getCurrentWeather(city)`, `getCurrentDateTime()` |
| **Data Store** | `store(key, val)`, `retrieve(key)`, `listKeys()` |

**LangChain4j Magic**:
```java
class CalculatorTool {
    @Tool("Adds two numbers")
    public double add(double a, double b) {
        return a + b;
    }
}

Assistant ai = AiServices.builder(Assistant.class)
    .chatLanguageModel(model)
    .tools(new CalculatorTool())
    .build();

// LLM automatically calls add() when user asks math
ai.chat("What is 25 + 17?");  // → Calls add(25, 17) → Returns "42"
```

**Tool Flow**:
```
User Query → LLM Analyzes → Selects Tool → Extracts Params → Calls Method → Returns Result
```

---

## 🔧 LangChain4j Components Used

### Models
```java
// Chat Model - Synchronous responses
ChatLanguageModel chat = OllamaChatModel.builder()
    .baseUrl("http://localhost:11434")
    .modelName("llama3.2")
    .temperature(0.7)
    .build();

// Streaming Model - Asynchronous streaming
StreamingChatLanguageModel streaming = OllamaStreamingChatModel.builder()
    .baseUrl("http://localhost:11434")
    .modelName("llama3.2")
    .build();

// Embedding Model - Text to vectors
EmbeddingModel embeddings = new AllMiniLmL6V2EmbeddingModel();
```

### Memory
```java
// Keeps last 10 messages for context
MessageWindowChatMemory memory = MessageWindowChatMemory.withMaxMessages(10);
```

### Storage
```java
// In-memory vector database
EmbeddingStore<TextSegment> store = new InMemoryEmbeddingStore<>();
```

### Document Processing
```java
// Split documents into chunks
DocumentSplitter splitter = DocumentSplitters.recursive(300, 50);
List<TextSegment> segments = splitter.split(document);
```

---

## 📊 When to Use Each Feature

| Scenario | Use Feature | Why |
|----------|------------|-----|
| Simple Q&A | Basic Chat | No context needed |
| Chatbot | Conversational Chat | Needs conversation history |
| Knowledge Base | RAG | Custom domain knowledge |
| Specific AI Tasks | AI Services | Reusable, type-safe |
| Extend AI | Tools | Need code execution |

---

## 🎯 Real-World Examples

### Example 1: Customer Support Bot
```bash
# Add knowledge base
POST /api/rag/add-documents
{"texts": ["Product X costs $99", "Shipping takes 3-5 days"]}

# User asks question
POST /api/rag/ask
{"question": "How much does Product X cost?"}
→ Answer: "Product X costs $99"
```

### Example 2: Code Assistant
```bash
POST /api/ai-services/generate-code
{
  "language": "Python",
  "description": "REST API client with error handling"
}
→ Returns: Complete Python code
```

### Example 3: Multi-turn Conversation
```bash
# Turn 1
POST /api/conversational-chat/chat
{"message": "I need help with Java streams"}

# Turn 2
POST /api/conversational-chat/chat
{"message": "Show me an example"}
→ AI remembers you're learning Java streams
```

### Example 4: Calculator Agent
```bash
POST /api/tools/chat
{"message": "Calculate (25 + 15) * 3 / 2"}
→ LLM calls: add(25,15)=40, multiply(40,3)=120, divide(120,2)=60
→ Returns: "The answer is 60"
```

---

## 🏗️ Architecture Overview

```
REST API Layer
    ↓
Service Layer (Your Code)
    ↓
LangChain4j Layer
    ↓
Ollama (Local LLM)
```

**Key Points**:
- 🔒 **Privacy**: Everything runs locally
- 💰 **No Costs**: No API fees
- 🚀 **Fast**: Local processing
- 🔧 **Flexible**: Easy to customize

---

## 📈 Complexity Levels

| Level | Feature | Description |
|-------|---------|-------------|
| 🟢 **Beginner** | Basic Chat | Just send/receive |
| 🟡 **Intermediate** | Conversational Chat | Memory management |
| 🟡 **Intermediate** | AI Services | Annotation-based |
| 🟠 **Advanced** | RAG | Embeddings + retrieval |
| 🔴 **Expert** | Tools | Function calling |

---

## 💡 Pro Tips

### Optimize RAG
- **Chunk Size**: 300 chars (balance context vs precision)
- **Overlap**: 50 chars (maintains continuity)
- **Max Results**: 3-5 (best balance)

### Improve Chat Quality
- **Temperature**: 
  - 0.3 = Focused, deterministic
  - 0.7 = Balanced (default)
  - 1.0 = Creative, varied
  
### Memory Management
- **Window Size**: 10 messages (prevents bloat)
- **Clear Often**: Reset for new topics

### Tool Design
- **Descriptive Names**: LLM decides based on description
- **Clear Annotations**: `@Tool("Clear description")`
- **Error Handling**: Return user-friendly messages

---

## 🐛 Common Issues

### Issue: "Connection refused"
**Fix**: Start Ollama first: `ollama serve`

### Issue: "Model not found"
**Fix**: Download model: `ollama pull llama3.2`

### Issue: "No relevant context found"
**Fix**: Add documents first using `/api/rag/add-documents`

### Issue: Slow responses
**Fix**: Use smaller model (llama3.2 is fast)

---

## 📚 API Testing Commands

### Test Basic Chat
```bash
curl -X POST http://localhost:8080/api/basic-chat/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello, AI!"}'
```

### Test RAG
```bash
# Add knowledge
curl -X POST http://localhost:8080/api/rag/add-documents \
  -H "Content-Type: application/json" \
  -d '{"texts": ["Sky is blue", "Grass is green"]}'

# Ask question
curl -X POST http://localhost:8080/api/rag/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "What color is the sky?"}'
```

### Test Tools
```bash
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "What is 123 * 456?"}'
```

---

## 🎓 Learning Path

1. **Start**: Basic Chat - Understand LLM interaction
2. **Next**: Conversational Chat - Learn memory management
3. **Then**: AI Services - Master declarative APIs
4. **Advanced**: RAG - Implement knowledge bases
5. **Expert**: Tools - Enable function calling

---

## 📖 Further Reading

- **Full Documentation**: `COMPREHENSIVE_FEATURES_README.md`
- **LangChain4j Docs**: https://docs.langchain4j.dev/
- **Ollama Docs**: https://ollama.ai/

---

## ✅ Checklist for Production

- [ ] Configure proper timeout values
- [ ] Add authentication/authorization
- [ ] Implement rate limiting
- [ ] Add monitoring/metrics
- [ ] Use persistent embedding store (not in-memory)
- [ ] Add input validation
- [ ] Implement error handling
- [ ] Add API documentation (Swagger)
- [ ] Configure CORS if needed
- [ ] Add health check endpoints

---

**🎉 You now have a complete LangChain4j demo with all major features!**

**Features Count**:
- ✅ 5 Major Features
- ✅ 20+ API Endpoints  
- ✅ Local LLM (Ollama)
- ✅ Production-Ready Code
- ✅ Comprehensive Documentation

**Ready to build AI applications in Java!** 🚀
