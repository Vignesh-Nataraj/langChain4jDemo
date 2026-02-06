#!/bin/bash

# LangChain4j Demo - API Test Script
# Make sure the application is running on http://localhost:8080

BASE_URL="http://localhost:8080"

echo "=================================="
echo "LangChain4j Demo - API Tests"
echo "=================================="

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Test 1: Basic Chat
echo -e "\n${BLUE}Test 1: Basic Chat${NC}"
curl -s -X POST "$BASE_URL/api/basic-chat/chat" \
  -H "Content-Type: application/json" \
  -d '{"message": "Say hello in one sentence!"}' | jq

# Test 2: Conversational Chat
echo -e "\n${BLUE}Test 2: Conversational Chat${NC}"
echo -e "${GREEN}Message 1: Setting context${NC}"
curl -s -X POST "$BASE_URL/api/conversational-chat/chat" \
  -H "Content-Type: application/json" \
  -d '{"message": "My name is Alice"}' | jq

echo -e "\n${GREEN}Message 2: Testing memory${NC}"
curl -s -X POST "$BASE_URL/api/conversational-chat/chat" \
  -H "Content-Type: application/json" \
  -d '{"message": "What is my name?"}' | jq

# Test 3: Add Documents for RAG
echo -e "\n${BLUE}Test 3: Adding Documents to Knowledge Base${NC}"
curl -s -X POST "$BASE_URL/api/rag/add-documents" \
  -H "Content-Type: application/json" \
  -d '{
    "texts": [
      "LangChain4j is a Java library for building LLM applications",
      "Ollama runs large language models locally on your computer",
      "RAG stands for Retrieval Augmented Generation",
      "Embeddings convert text into numerical vectors"
    ]
  }' | jq

# Test 4: RAG Query
echo -e "\n${BLUE}Test 4: RAG Query${NC}"
curl -s -X POST "$BASE_URL/api/rag/ask" \
  -H "Content-Type: application/json" \
  -d '{"question": "What is LangChain4j?", "maxResults": 2}' | jq

# Test 5: AI Services - Sentiment Analysis
echo -e "\n${BLUE}Test 5: Sentiment Analysis${NC}"
curl -s -X POST "$BASE_URL/api/ai-services/sentiment" \
  -H "Content-Type: application/json" \
  -d '{"text": "This is an amazing product! I absolutely love it!"}' | jq

# Test 6: Code Generation
echo -e "\n${BLUE}Test 6: Code Generation${NC}"
curl -s -X POST "$BASE_URL/api/ai-services/generate-code" \
  -H "Content-Type: application/json" \
  -d '{
    "language": "Python",
    "description": "function to reverse a string"
  }' | jq

# Test 7: Tools - Calculator
echo -e "\n${BLUE}Test 7: Tool Usage (Calculator)${NC}"
curl -s -X POST "$BASE_URL/api/tools/chat" \
  -H "Content-Type: application/json" \
  -d '{"message": "What is 15 multiplied by 8?"}' | jq

echo -e "\n${GREEN}=================================="
echo "All tests completed!"
echo "==================================${NC}"
