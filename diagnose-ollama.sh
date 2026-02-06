#!/bin/bash

# Ollama Diagnostic Script
# Run this to diagnose Ollama connectivity issues

echo "╔════════════════════════════════════════════════════════════════╗"
echo "║             OLLAMA DIAGNOSTIC TOOL                             ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 1. Check Ollama Version
echo -e "${YELLOW}1. Checking Ollama Version...${NC}"
if command -v ollama &> /dev/null; then
    ollama --version
    echo -e "${GREEN}✓ Ollama is installed${NC}"
else
    echo -e "${RED}✗ Ollama is not installed${NC}"
    echo "Install with: brew install ollama"
fi
echo ""

# 2. Check if Ollama is running
echo -e "${YELLOW}2. Checking if Ollama is Running...${NC}"
if ps aux | grep -q "[o]llama serve"; then
    echo -e "${GREEN}✓ Ollama process is running${NC}"
    ps aux | grep "[o]llama serve"
else
    echo -e "${RED}✗ Ollama is not running${NC}"
    echo "Start with: ollama serve"
fi
echo ""

# 3. Check Port 11434
echo -e "${YELLOW}3. Checking Port 11434...${NC}"
if lsof -i :11434 &> /dev/null; then
    echo -e "${GREEN}✓ Port 11434 is listening${NC}"
    lsof -i :11434
else
    echo -e "${RED}✗ Port 11434 is not listening${NC}"
fi
echo ""

# 4. Check Downloaded Models
echo -e "${YELLOW}4. Checking Downloaded Models...${NC}"
if ollama list 2>&1 | grep -q "NAME"; then
    ollama list
    if ollama list | grep -q "llama2"; then
        echo -e "${GREEN}✓ llama2 model is downloaded${NC}"
    else
        echo -e "${RED}✗ llama2 model is NOT downloaded${NC}"
        echo "Download with: ollama pull llama2"
    fi
else
    echo -e "${RED}✗ Cannot list models (Ollama might not be running)${NC}"
fi
echo ""

# 5. Test API Tags Endpoint
echo -e "${YELLOW}5. Testing API Tags Endpoint...${NC}"
TAGS_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" http://localhost:11434/api/tags)
HTTP_CODE=$(echo "$TAGS_RESPONSE" | grep "HTTP_CODE" | cut -d: -f2)
if [ "$HTTP_CODE" = "200" ]; then
    echo -e "${GREEN}✓ API tags endpoint is working (HTTP 200)${NC}"
    echo "$TAGS_RESPONSE" | grep -v "HTTP_CODE" | jq . 2>/dev/null || echo "$TAGS_RESPONSE" | grep -v "HTTP_CODE"
else
    echo -e "${RED}✗ API tags endpoint failed (HTTP $HTTP_CODE)${NC}"
fi
echo ""

# 6. Test Generate API (Older endpoint)
echo -e "${YELLOW}6. Testing /api/generate Endpoint...${NC}"
GENERATE_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST http://localhost:11434/api/generate \
  -H "Content-Type: application/json" \
  -d '{"model":"llama2","prompt":"Hi","stream":false}' 2>&1)
HTTP_CODE=$(echo "$GENERATE_RESPONSE" | grep "HTTP_CODE" | cut -d: -f2)
if [ "$HTTP_CODE" = "200" ]; then
    echo -e "${GREEN}✓ Generate API is working (HTTP 200)${NC}"
else
    echo -e "${RED}✗ Generate API failed (HTTP $HTTP_CODE)${NC}"
    echo "Response: $GENERATE_RESPONSE"
fi
echo ""

# 7. Test Chat API (Newer endpoint)
echo -e "${YELLOW}7. Testing /api/chat Endpoint...${NC}"
CHAT_RESPONSE=$(curl -s -w "\nHTTP_CODE:%{http_code}" -X POST http://localhost:11434/api/chat \
  -H "Content-Type: application/json" \
  -d '{"model":"llama2","messages":[{"role":"user","content":"Hi"}],"stream":false}' 2>&1)
HTTP_CODE=$(echo "$CHAT_RESPONSE" | grep "HTTP_CODE" | cut -d: -f2)
if [ "$HTTP_CODE" = "200" ]; then
    echo -e "${GREEN}✓ Chat API is working (HTTP 200)${NC}"
else
    echo -e "${RED}✗ Chat API failed (HTTP $HTTP_CODE) - THIS IS YOUR PROBLEM!${NC}"
    echo "Response: $CHAT_RESPONSE"
fi
echo ""

# 8. Check Java Version
echo -e "${YELLOW}8. Checking Java Version...${NC}"
if java -version 2>&1 | grep -q "version \"25"; then
    echo -e "${GREEN}✓ Java 25 is installed${NC}"
    java -version 2>&1 | head -1
else
    echo -e "${YELLOW}⚠ Java version is not 25${NC}"
    java -version 2>&1 | head -1
fi
echo ""

# Summary and Recommendations
echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                    DIAGNOSTIC SUMMARY                          ║"
echo "╚════════════════════════════════════════════════════════════════╝"
echo ""

if [ "$HTTP_CODE" = "404" ]; then
    echo -e "${RED}PROBLEM IDENTIFIED: Ollama /api/chat returns 404${NC}"
    echo ""
    echo "RECOMMENDED FIXES:"
    echo "1. Update Ollama: brew upgrade ollama"
    echo "2. Ensure llama2 is downloaded: ollama pull llama2"
    echo "3. Restart Ollama: pkill ollama && ollama serve"
    echo "4. Update LangChain4j in pom.xml to version 0.37.0 or newer"
    echo "5. Rebuild: mvn clean install"
    echo ""
    echo "See OLLAMA_404_FIX.md for detailed solutions"
elif ps aux | grep -q "[o]llama serve" && [ "$HTTP_CODE" = "200" ]; then
    echo -e "${GREEN}✓ Everything looks good! Ollama is working correctly.${NC}"
    echo ""
    echo "If your app still has issues:"
    echo "1. Check Spring Boot logs for detailed error messages"
    echo "2. Ensure application.properties has correct settings"
    echo "3. Rebuild with: mvn clean install"
else
    echo -e "${YELLOW}Some issues detected. Follow the recommendations above.${NC}"
fi

echo ""
echo "╔════════════════════════════════════════════════════════════════╗"
echo "║                    END OF DIAGNOSTIC                           ║"
echo "╚════════════════════════════════════════════════════════════════╝"
