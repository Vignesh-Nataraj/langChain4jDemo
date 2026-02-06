#!/bin/bash

# Quick Fix and Start Script
# Run this to apply all fixes and start the application

echo "╔═══════════════════════════════════════════════════════════════╗"
echo "║     LangChain4j Demo - Quick Fix and Start Script             ║"
echo "╚═══════════════════════════════════════════════════════════════╝"
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Check Java version
echo -e "${YELLOW}Checking Java version...${NC}"
if java -version 2>&1 | grep -q "version \"25"; then
    echo -e "${GREEN}✓ Java 25 detected${NC}"
else
    echo -e "${RED}✗ Java 25 not found${NC}"
    echo "Please install Java 25 and set JAVA_HOME"
    exit 1
fi
echo ""

# Check Ollama
echo -e "${YELLOW}Checking Ollama...${NC}"
if command -v ollama &> /dev/null; then
    echo -e "${GREEN}✓ Ollama is installed${NC}"

    # Check if Ollama is running
    if curl -s http://localhost:11434/api/tags > /dev/null 2>&1; then
        echo -e "${GREEN}✓ Ollama is running${NC}"
    else
        echo -e "${YELLOW}⚠ Ollama is not running${NC}"
        echo "Starting Ollama in background..."
        ollama serve > /tmp/ollama.log 2>&1 &
        echo "Waiting for Ollama to start..."
        sleep 5

        if curl -s http://localhost:11434/api/tags > /dev/null 2>&1; then
            echo -e "${GREEN}✓ Ollama started successfully${NC}"
        else
            echo -e "${RED}✗ Failed to start Ollama${NC}"
            echo "Start manually: ollama serve"
            exit 1
        fi
    fi

    # Check if llama2 is downloaded
    if ollama list | grep -q "llama2"; then
        echo -e "${GREEN}✓ llama2 model is downloaded${NC}"
    else
        echo -e "${YELLOW}⚠ llama2 model not found${NC}"
        echo "Downloading llama2 model (this may take a while)..."
        ollama pull llama2
        echo -e "${GREEN}✓ llama2 model downloaded${NC}"
    fi
else
    echo -e "${RED}✗ Ollama is not installed${NC}"
    echo "Install with: brew install ollama"
    exit 1
fi
echo ""

# Clean and rebuild
echo -e "${YELLOW}Rebuilding project...${NC}"
mvn clean install -DskipTests
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Build successful${NC}"
else
    echo -e "${RED}✗ Build failed${NC}"
    exit 1
fi
echo ""

# Show summary
echo "╔═══════════════════════════════════════════════════════════════╗"
echo "║                     READY TO START                            ║"
echo "╚═══════════════════════════════════════════════════════════════╝"
echo ""
echo -e "${GREEN}All checks passed! Ready to start the application.${NC}"
echo ""
echo "Starting Spring Boot application..."
echo ""
echo -e "${BLUE}Once started, test with:${NC}"
echo "curl -X POST http://localhost:8080/api/basic-chat/chat \\"
echo "  -H \"Content-Type: application/json\" \\"
echo "  -d '{\"message\": \"Hello!\"}'"
echo ""
echo -e "${YELLOW}Press Ctrl+C to stop the application${NC}"
echo ""
echo "═══════════════════════════════════════════════════════════════"
echo ""

# Start the application
mvn spring-boot:run
