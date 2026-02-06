#!/bin/bash

# LangChain4j Demo - Complete Setup Script
# This script will guide you through the setup process

echo "=================================="
echo "LangChain4j Demo - Setup Guide"
echo "=================================="
echo ""

# Colors
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Step 1: Check Ollama
echo -e "${BLUE}Step 1: Checking Ollama installation...${NC}"
if command -v ollama &> /dev/null; then
    echo -e "${GREEN}✓ Ollama is installed${NC}"
    ollama --version
else
    echo -e "${YELLOW}⚠ Ollama is not installed${NC}"
    echo ""
    echo "To install Ollama:"
    echo "  brew install ollama"
    echo ""
    echo "Or download from: https://ollama.ai/"
    echo ""
    exit 1
fi

echo ""

# Step 2: Check if Ollama is running
echo -e "${BLUE}Step 2: Checking if Ollama is running...${NC}"
if curl -s http://localhost:11434/api/tags > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Ollama is running${NC}"
else
    echo -e "${YELLOW}⚠ Ollama is not running${NC}"
    echo ""
    echo "To start Ollama, run in a separate terminal:"
    echo "  ollama serve"
    echo ""
    echo "Then press Enter to continue..."
    read
fi

echo ""

# Step 3: Check for models
echo -e "${BLUE}Step 3: Checking for installed models...${NC}"
MODELS=$(ollama list 2>/dev/null | tail -n +2)
if [ -z "$MODELS" ]; then
    echo -e "${YELLOW}⚠ No models installed${NC}"
    echo ""
    echo "Would you like to download llama2? (Recommended, ~4GB download)"
    echo "Type 'yes' to download or 'no' to skip:"
    read DOWNLOAD_MODEL

    if [ "$DOWNLOAD_MODEL" = "yes" ]; then
        echo ""
        echo -e "${BLUE}Downloading llama2... This may take a few minutes.${NC}"
        ollama pull llama2
        echo -e "${GREEN}✓ llama2 downloaded${NC}"
    else
        echo ""
        echo "You can download models later using:"
        echo "  ollama pull llama2"
        echo "  ollama pull mistral"
        echo "  ollama pull phi"
    fi
else
    echo -e "${GREEN}✓ Installed models:${NC}"
    echo "$MODELS"
fi

echo ""

# Step 4: Check Java
echo -e "${BLUE}Step 4: Checking Java installation...${NC}"
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1)
    echo -e "${GREEN}✓ Java is installed: $JAVA_VERSION${NC}"

    # Extract major version
    JAVA_MAJOR=$(java -version 2>&1 | head -n 1 | sed 's/.*version "\([0-9]*\).*/\1/')
    if [ "$JAVA_MAJOR" -lt 25 ]; then
        echo -e "${YELLOW}⚠ Warning: Java 25 is recommended for this project${NC}"
        echo "Current version appears to be older than Java 25"
        echo "Please install Java 25:"
        echo "  brew install openjdk@25"
        echo ""
        echo "Continue anyway? (yes/no)"
        read CONTINUE
        if [ "$CONTINUE" != "yes" ]; then
            exit 1
        fi
    fi
else
    echo -e "${RED}✗ Java is not installed${NC}"
    echo ""
    echo "Please install Java 25 or higher:"
    echo "  brew install openjdk@25"
    echo ""
    echo "Or download from: https://jdk.java.net/25/"
    exit 1
fi

echo ""

# Step 5: Build the project
echo -e "${BLUE}Step 5: Building the project...${NC}"
./mvnw clean package -DskipTests

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Build successful${NC}"
else
    echo -e "${RED}✗ Build failed${NC}"
    exit 1
fi

echo ""
echo "=================================="
echo -e "${GREEN}✓ Setup Complete!${NC}"
echo "=================================="
echo ""
echo -e "${BLUE}Next Steps:${NC}"
echo ""
echo "1. Make sure Ollama is running:"
echo "   ollama serve"
echo ""
echo "2. Start the application:"
echo "   ./mvnw spring-boot:run"
echo ""
echo "3. Test in another terminal:"
echo "   ./test-api.sh"
echo ""
echo "4. Or use Postman:"
echo "   Import: LangChain4j-Demo-API.postman_collection.json"
echo ""
echo -e "${GREEN}Happy coding! 🚀${NC}"
echo ""
