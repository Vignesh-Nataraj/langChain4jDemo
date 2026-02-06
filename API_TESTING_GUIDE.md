# 🧪 API Testing Guide - LangChain4j Demo

Complete guide for testing all features with curl commands and expected responses.

---

## 🚀 Prerequisites

1. Application running on `http://localhost:8080`
2. Ollama running with llama3.2 model
3. Terminal or REST client ready

---

## 📋 Table of Contents

1. [Basic Chat APIs](#1-basic-chat-apis)
2. [Conversational Chat APIs](#2-conversational-chat-apis)
3. [RAG APIs](#3-rag-apis)
4. [AI Services APIs](#4-ai-services-apis)
5. [Tools APIs](#5-tools-apis)

---

## 1. 💬 Basic Chat APIs

### 1.1 Simple Chat

**Endpoint**: `POST /api/basic-chat/chat`

**Request**:
```bash
curl -X POST http://localhost:8080/api/basic-chat/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Explain artificial intelligence in 2 sentences"
  }'
```

**Expected Response**:
```json
{
  "response": "Artificial intelligence (AI) is the simulation of human intelligence by machines, enabling them to perform tasks like learning, reasoning, and problem-solving. It powers applications from virtual assistants to autonomous vehicles."
}
```

---

### 1.2 Ask Question

**Endpoint**: `POST /api/basic-chat/question`

**Request**:
```bash
curl -X POST http://localhost:8080/api/basic-chat/question \
  -H "Content-Type: application/json" \
  -d '{
    "message": "What is the capital of France?"
  }'
```

**Expected Response**:
```json
{
  "response": "The capital of France is Paris."
}
```

---

### 1.3 Generate Content

**Endpoint**: `POST /api/basic-chat/generate-content`

**Request**:
```bash
curl -X POST http://localhost:8080/api/basic-chat/generate-content \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Write a haiku about programming"
  }'
```

**Expected Response**:
```json
{
  "response": "Code flows like water,\nBugs dance in morning sunlight,\nDebug brings the peace."
}
```

---

### 1.4 Generate Code

**Endpoint**: `POST /api/basic-chat/generate-code`

**Request**:
```bash
curl -X POST http://localhost:8080/api/basic-chat/generate-code \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Create a Java method to reverse a string"
  }'
```

**Expected Response**:
```json
{
  "response": "public String reverseString(String input) {\n    if (input == null) return null;\n    return new StringBuilder(input).reverse().toString();\n}"
}
```

---

## 2. 🧠 Conversational Chat APIs

### 2.1 Start Conversation

**Endpoint**: `POST /api/conversational-chat/chat`

**Request 1**:
```bash
curl -X POST http://localhost:8080/api/conversational-chat/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Hello! My name is Alice and I love programming."
  }'
```

**Expected Response**:
```json
{
  "response": "Hello Alice! It's great to meet you. Programming is a wonderful passion. What programming languages do you enjoy working with?",
  "conversationSize": 2
}
```

---

### 2.2 Continue Conversation (Context-Aware)

**Request 2**:
```bash
curl -X POST http://localhost:8080/api/conversational-chat/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "What is my name?"
  }'
```

**Expected Response**:
```json
{
  "response": "Your name is Alice.",
  "conversationSize": 4
}
```

**Note**: The AI remembers from the previous message!

---

### 2.3 Get Conversation Size

**Endpoint**: `GET /api/conversational-chat/size`

**Request**:
```bash
curl -X GET http://localhost:8080/api/conversational-chat/size
```

**Expected Response**:
```json
4
```

---

### 2.4 Clear Memory

**Endpoint**: `POST /api/conversational-chat/clear`

**Request**:
```bash
curl -X POST http://localhost:8080/api/conversational-chat/clear
```

**Expected Response**:
```json
"Conversation memory cleared"
```

---

## 3. 🔍 RAG APIs

### 3.1 Add Single Document

**Endpoint**: `POST /api/rag/add-document`

**Request**:
```bash
curl -X POST http://localhost:8080/api/rag/add-document \
  -H "Content-Type: application/json" \
  -d '{
    "text": "The Eiffel Tower is a wrought-iron lattice tower in Paris, France. It was completed in 1889 and stands 330 meters tall.",
    "id": "eiffel-tower-1"
  }'
```

**Expected Response**:
```json
"Document added successfully"
```

---

### 3.2 Add Multiple Documents (Batch)

**Endpoint**: `POST /api/rag/add-documents`

**Request**:
```bash
curl -X POST http://localhost:8080/api/rag/add-documents \
  -H "Content-Type: application/json" \
  -d '{
    "texts": [
      "Paris is the capital and largest city of France, with a population of over 2 million people.",
      "The Louvre Museum in Paris is the world largest art museum and houses the Mona Lisa.",
      "Notre-Dame Cathedral is a medieval Catholic cathedral located on the Île de la Cité in Paris.",
      "The Seine River flows through Paris and divides the city into the Left Bank and Right Bank."
    ]
  }'
```

**Expected Response**:
```json
"4 documents added successfully"
```

---

### 3.3 Add Long Document (Auto-Chunking)

**Endpoint**: `POST /api/rag/add-long-document`

**Request**:
```bash
curl -X POST http://localhost:8080/api/rag/add-long-document \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Machine learning is a subset of artificial intelligence that focuses on the development of algorithms and statistical models. These models enable computers to improve their performance on a specific task through experience, without being explicitly programmed. The process involves feeding data to algorithms, which then learn patterns and make predictions or decisions. Common applications include image recognition, natural language processing, recommendation systems, and autonomous vehicles. Deep learning, a subset of machine learning, uses neural networks with multiple layers to process complex patterns in large amounts of data."
  }'
```

**Expected Response**:
```json
"Long document processed and added successfully"
```

**Note**: The document is automatically split into chunks of ~300 characters.

---

### 3.4 Semantic Search

**Endpoint**: `POST /api/rag/search`

**Request**:
```bash
curl -X POST http://localhost:8080/api/rag/search \
  -H "Content-Type: application/json" \
  -d '{
    "query": "famous landmarks in Paris",
    "maxResults": 3
  }'
```

**Expected Response**:
```json
[
  "The Eiffel Tower is a wrought-iron lattice tower in Paris, France. It was completed in 1889 and stands 330 meters tall.",
  "The Louvre Museum in Paris is the world's largest art museum and houses the Mona Lisa.",
  "Notre-Dame Cathedral is a medieval Catholic cathedral located on the Île de la Cité in Paris."
]
```

---

### 3.5 Ask Question (RAG)

**Endpoint**: `POST /api/rag/ask`

**Request**:
```bash
curl -X POST http://localhost:8080/api/rag/ask \
  -H "Content-Type: application/json" \
  -d '{
    "question": "How tall is the Eiffel Tower?",
    "maxResults": 3
  }'
```

**Expected Response**:
```json
{
  "answer": "The Eiffel Tower stands 330 meters tall."
}
```

**Another Example**:
```bash
curl -X POST http://localhost:8080/api/rag/ask \
  -H "Content-Type: application/json" \
  -d '{
    "question": "What museum houses the Mona Lisa?",
    "maxResults": 3
  }'
```

**Expected Response**:
```json
{
  "answer": "The Louvre Museum in Paris houses the Mona Lisa."
}
```

---

### 3.6 Ask with Sources

**Endpoint**: `POST /api/rag/ask-with-sources`

**Request**:
```bash
curl -X POST http://localhost:8080/api/rag/ask-with-sources \
  -H "Content-Type: application/json" \
  -d '{
    "question": "Tell me about Paris landmarks",
    "maxResults": 3
  }'
```

**Expected Response**:
```json
{
  "answer": "Paris is home to several iconic landmarks. The Eiffel Tower is a 330-meter tall wrought-iron structure completed in 1889. The Louvre Museum is the world's largest art museum and houses the famous Mona Lisa painting. Notre-Dame Cathedral is a medieval Catholic cathedral located on the Île de la Cité.",
  "sources": [
    "The Eiffel Tower is a wrought-iron lattice tower in Paris, France. It was completed in 1889 and stands 330 meters tall.",
    "The Louvre Museum in Paris is the world's largest art museum and houses the Mona Lisa.",
    "Notre-Dame Cathedral is a medieval Catholic cathedral located on the Île de la Cité in Paris."
  ]
}
```

---

## 4. 🎯 AI Services APIs

### 4.1 General Chat (AI Services)

**Endpoint**: `POST /api/ai-services/chat`

**Request**:
```bash
curl -X POST http://localhost:8080/api/ai-services/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Explain the concept of recursion"
  }'
```

**Expected Response**:
```json
{
  "result": "Recursion is a programming technique where a function calls itself to solve a problem by breaking it down into smaller, similar sub-problems. Each recursive call works on a smaller version of the problem until reaching a base case that stops the recursion."
}
```

---

### 4.2 Generate Code

**Endpoint**: `POST /api/ai-services/generate-code`

**Request**:
```bash
curl -X POST http://localhost:8080/api/ai-services/generate-code \
  -H "Content-Type: application/json" \
  -d '{
    "language": "Python",
    "description": "Function to calculate factorial using recursion"
  }'
```

**Expected Response**:
```json
{
  "result": "def factorial(n):\n    # Base case: factorial of 0 or 1 is 1\n    if n <= 1:\n        return 1\n    # Recursive case: n! = n * (n-1)!\n    return n * factorial(n - 1)\n\n# Example usage:\n# print(factorial(5))  # Output: 120"
}
```

**Another Example - Java**:
```bash
curl -X POST http://localhost:8080/api/ai-services/generate-code \
  -H "Content-Type: application/json" \
  -d '{
    "language": "Java",
    "description": "REST controller with GET and POST endpoints for User entity"
  }'
```

---

### 4.3 Sentiment Analysis

**Endpoint**: `POST /api/ai-services/sentiment`

**Request - Positive**:
```bash
curl -X POST http://localhost:8080/api/ai-services/sentiment \
  -H "Content-Type: application/json" \
  -d '{
    "text": "I absolutely love this product! Best purchase I have ever made. Highly recommend!"
  }'
```

**Expected Response**:
```json
{
  "result": "POSITIVE - The text expresses strong enthusiasm and satisfaction with superlative language like 'absolutely love' and 'best purchase ever'."
}
```

**Request - Negative**:
```bash
curl -X POST http://localhost:8080/api/ai-services/sentiment \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Terrible experience. The service was slow and the staff was rude. Would not recommend."
  }'
```

**Expected Response**:
```json
{
  "result": "NEGATIVE - The text contains negative descriptors like 'terrible', 'slow', and 'rude', expressing dissatisfaction."
}
```

**Request - Neutral**:
```bash
curl -X POST http://localhost:8080/api/ai-services/sentiment \
  -H "Content-Type: application/json" \
  -d '{
    "text": "The product arrived on time. It has a blue color and measures 10 inches."
  }'
```

**Expected Response**:
```json
{
  "result": "NEUTRAL - The text provides factual information without expressing positive or negative emotions."
}
```

---

### 4.4 Summarization

**Endpoint**: `POST /api/ai-services/summarize`

**Request**:
```bash
curl -X POST http://localhost:8080/api/ai-services/summarize \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Artificial intelligence has made remarkable progress in recent years, transforming industries from healthcare to finance. Machine learning algorithms can now diagnose diseases with accuracy comparable to human doctors, predict financial market trends, and power autonomous vehicles. Natural language processing has enabled chatbots and virtual assistants to understand and respond to human queries with increasing sophistication. Computer vision systems can identify objects, faces, and even emotions in images and videos. Despite these advances, challenges remain in areas such as explainability, bias, and ethical considerations. Researchers continue to work on making AI systems more transparent, fair, and aligned with human values.",
    "maxWords": 50
  }'
```

**Expected Response**:
```json
{
  "result": "AI has transformed industries like healthcare and finance through machine learning and natural language processing. While achieving impressive capabilities in diagnosis, prediction, and automation, challenges persist in explainability, bias, and ethics. Researchers focus on making AI more transparent and aligned with human values."
}
```

---

### 4.5 Translation

**Endpoint**: `POST /api/ai-services/translate`

**Request - English to Spanish**:
```bash
curl -X POST http://localhost:8080/api/ai-services/translate \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Hello, how are you? I hope you are having a wonderful day.",
    "targetLanguage": "Spanish"
  }'
```

**Expected Response**:
```json
{
  "result": "Hola, ¿cómo estás? Espero que estés teniendo un día maravilloso."
}
```

**Request - English to French**:
```bash
curl -X POST http://localhost:8080/api/ai-services/translate \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Good morning! Would you like some coffee?",
    "targetLanguage": "French"
  }'
```

**Expected Response**:
```json
{
  "result": "Bonjour! Voulez-vous du café?"
}
```

**Request - English to Japanese**:
```bash
curl -X POST http://localhost:8080/api/ai-services/translate \
  -H "Content-Type: application/json" \
  -d '{
    "text": "Thank you very much for your help.",
    "targetLanguage": "Japanese"
  }'
```

**Expected Response**:
```json
{
  "result": "ご協力ありがとうございます。"
}
```

---

## 5. 🛠️ Tools APIs

### 5.1 Calculator Tool - Addition

**Endpoint**: `POST /api/tools/chat`

**Request**:
```bash
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "What is 127 plus 395?"
  }'
```

**Expected Response**:
```json
{
  "response": "127 plus 395 equals 522."
}
```

**Console Output**:
```
Tool called: add(127.0, 395.0)
```

---

### 5.2 Calculator Tool - Complex Math

**Request**:
```bash
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Calculate (25 * 4) + (100 / 5) - 15"
  }'
```

**Expected Response**:
```json
{
  "response": "The result is 105. Here's how: (25 * 4) = 100, (100 / 5) = 20, then 100 + 20 - 15 = 105."
}
```

**Console Output**:
```
Tool called: multiply(25.0, 4.0)
Tool called: divide(100.0, 5.0)
Tool called: add(100.0, 20.0)
Tool called: subtract(120.0, 15.0)
```

---

### 5.3 Calculator Tool - Square Root

**Request**:
```bash
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "What is the square root of 256?"
  }'
```

**Expected Response**:
```json
{
  "response": "The square root of 256 is 16."
}
```

---

### 5.4 Weather Tool

**Request**:
```bash
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "What is the weather in Tokyo?"
  }'
```

**Expected Response**:
```json
{
  "response": "Weather in Tokyo: Sunny, 22°C, Humidity 65%"
}
```

**Console Output**:
```
Tool called: getCurrentWeather(Tokyo)
```

---

### 5.5 Date/Time Tool

**Request**:
```bash
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "What is the current date and time?"
  }'
```

**Expected Response**:
```json
{
  "response": "The current date and time is 2026-02-06 07:30:45."
}
```

---

### 5.6 Data Store Tool - Store

**Request**:
```bash
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Remember that my favorite color is blue"
  }'
```

**Expected Response**:
```json
{
  "response": "I've stored that your favorite color is blue."
}
```

**Console Output**:
```
Tool called: store(favorite_color, blue)
```

---

### 5.7 Data Store Tool - Retrieve

**Request**:
```bash
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "What is my favorite color?"
  }'
```

**Expected Response**:
```json
{
  "response": "Your favorite color is blue."
}
```

**Console Output**:
```
Tool called: retrieve(favorite_color)
```

---

### 5.8 Data Store Tool - List Keys

**Request**:
```bash
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "What information have you stored?"
  }'
```

**Expected Response**:
```json
{
  "response": "I have stored the following: favorite_color, user_name, preference"
}
```

---

### 5.9 Multi-Tool Scenario

**Request**:
```bash
curl -X POST http://localhost:8080/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Store my age as 25, then calculate what my age will be in 10 years"
  }'
```

**Expected Response**:
```json
{
  "response": "I've stored your age as 25. In 10 years, you will be 35 years old."
}
```

**Console Output**:
```
Tool called: store(age, 25)
Tool called: add(25.0, 10.0)
```

---

## 📊 Complete Test Suite Script

Save this as `test-all-apis.sh`:

```bash
#!/bin/bash

BASE_URL="http://localhost:8080"

echo "🧪 Testing LangChain4j Demo APIs"
echo "================================="

# Basic Chat
echo -e "\n1️⃣ Testing Basic Chat..."
curl -s -X POST $BASE_URL/api/basic-chat/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello AI"}' | jq

# Conversational Chat
echo -e "\n2️⃣ Testing Conversational Chat..."
curl -s -X POST $BASE_URL/api/conversational-chat/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "My name is Alice"}' | jq

# RAG - Add Documents
echo -e "\n3️⃣ Testing RAG - Adding Documents..."
curl -s -X POST $BASE_URL/api/rag/add-documents \
  -H "Content-Type: application/json" \
  -d '{"texts": ["Paris is in France", "Tokyo is in Japan"]}' | jq

# RAG - Ask Question
echo -e "\n4️⃣ Testing RAG - Asking Question..."
curl -s -X POST $BASE_URL/api/rag/ask \
  -H "Content-Type: application/json" \
  -d '{"question": "Where is Paris?", "maxResults": 3}' | jq

# AI Services - Code Generation
echo -e "\n5️⃣ Testing AI Services - Code Generation..."
curl -s -X POST $BASE_URL/api/ai-services/generate-code \
  -H "Content-Type: application/json" \
  -d '{"language": "Python", "description": "hello world"}' | jq

# Tools - Calculator
echo -e "\n6️⃣ Testing Tools - Calculator..."
curl -s -X POST $BASE_URL/api/tools/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "What is 100 plus 200?"}' | jq

echo -e "\n✅ All tests completed!"
```

Make it executable and run:
```bash
chmod +x test-all-apis.sh
./test-all-apis.sh
```

---

## 🎯 Postman Collection

Import the file `LangChain4j-Demo-API.postman_collection.json` included in the project for GUI-based testing.

---

## 📝 Response Time Expectations

| Feature | Expected Time | Notes |
|---------|--------------|-------|
| Basic Chat | 2-5 seconds | Depends on response length |
| Conversational | 2-5 seconds | Slightly slower with memory |
| RAG Search | <1 second | Fast embedding search |
| RAG Answer | 3-7 seconds | Search + generation |
| AI Services | 2-5 seconds | Similar to basic chat |
| Tools | 3-8 seconds | LLM decides + executes |

**Factors affecting speed**:
- Model size (llama3.2 3.2B is fast)
- Hardware (CPU/GPU)
- Response length
- Number of tool calls

---

## 🐛 Troubleshooting Test Failures

### Error: Connection Refused
**Solution**: Start application with `mvn spring-boot:run`

### Error: 404 Not Found
**Solution**: Verify endpoint URL and HTTP method

### Error: 500 Internal Server Error
**Check**: 
- Ollama is running: `ollama serve`
- Model is available: `ollama list`
- Application logs for details

### Empty RAG Results
**Solution**: Add documents first using `/api/rag/add-documents`

---

## ✅ Success Criteria

All features working correctly if:
- ✅ Basic chat responds coherently
- ✅ Conversational chat remembers context
- ✅ RAG finds and uses document context
- ✅ AI Services perform specific tasks correctly
- ✅ Tools execute and return results
- ✅ No error messages in console
- ✅ Response times reasonable (<10s)

---

**Happy Testing! 🎉**

For more details, see:
- `COMPREHENSIVE_FEATURES_README.md` - Full documentation
- `QUICK_REFERENCE.md` - Quick feature guide
