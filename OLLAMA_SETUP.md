# ⚠️ IMPORTANT: Ollama Setup Required

## Current Status: Ollama is NOT Running

Your LangChain4j demo application is ready, but **Ollama needs to be installed and started** before you can use it.

---

## 📋 Prerequisites

### Java 25 Required

This project requires **Java 25** or higher.

**Check your version:**
```bash
java -version
```

**Install Java 25 if needed:**
```bash
# macOS - Using Homebrew
brew install openjdk@25

# Or download from: https://jdk.java.net/25/
```

---

## 🔧 Install Ollama (Choose One Method)

### Method 1: Using Homebrew (Recommended for macOS)
```bash
brew install ollama
```

### Method 2: Manual Download
1. Visit: https://ollama.ai/download
2. Download the macOS installer
3. Install the application

## 🚀 Start Ollama

After installation, start Ollama with:

```bash
ollama serve
```

**Keep this terminal window open!** Ollama needs to run continuously.

## 📦 Download a Model

In a **new terminal window**, download a model:

```bash
# Recommended: Llama 2 (~4GB download)
ollama pull llama2

# OR use a smaller/faster model:
ollama pull phi          # Smaller, faster (~1.6GB)
ollama pull mistral      # Good balance (~4GB)
ollama pull codellama    # Best for code (~4GB)
```

## ✅ Verify Ollama is Working

```bash
# Check installed models
ollama list

# Test Ollama
curl http://localhost:11434/api/tags

# Quick chat test
ollama run llama2 "Say hello"
```

You should see a response from the model.

## 🎯 Start Your Spring Boot Application

Once Ollama is running and a model is downloaded:

```bash
cd /Users/vicky/Documents/GitHub/langChain4jDemo
./mvnw spring-boot:run
```

## 🧪 Test the Application

### Quick Test
```bash
curl -X POST http://localhost:8080/api/basic-chat/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello!"}'
```

### Run All Tests
```bash
./test-api.sh
```

### Use Postman
Import: `LangChain4j-Demo-API.postman_collection.json`

## 📋 Complete Setup Checklist

- [ ] Install Ollama (`brew install ollama`)
- [ ] Start Ollama (`ollama serve` - keep running)
- [ ] Download a model (`ollama pull llama2`)
- [ ] Verify model is available (`ollama list`)
- [ ] Start Spring Boot app (`./mvnw spring-boot:run`)
- [ ] Test an endpoint (use curl or Postman)

## 🔍 Troubleshooting

### "Connection refused" when starting app
**Problem:** Ollama is not running  
**Solution:** Run `ollama serve` in a separate terminal

### "Model not found"
**Problem:** No model downloaded  
**Solution:** Run `ollama pull llama2`

### Slow responses
**Problem:** Model is too large  
**Solution:** Use a smaller model like `phi`:
```bash
ollama pull phi
```
Then update `src/main/resources/application.properties`:
```properties
ollama.chat.model=phi
```

## 📚 What Each Component Does

### Ollama
- Runs LLMs locally on your Mac
- No internet needed after model download
- Provides API at `localhost:11434`

### Your Spring Boot App
- Connects to Ollama
- Provides REST APIs for various LangChain4j features
- Runs on `localhost:8080`

## 🎓 Next Steps After Setup

1. **Read README.md** - Complete documentation
2. **Read QUICKSTART.md** - Quick start guide
3. **Read FEATURES_EXPLAINED.md** - Detailed feature explanations
4. **Try the examples** - Start with basic chat
5. **Build your own features** - Extend the demo

## 💡 Quick Start Commands (Copy-Paste)

```bash
# Terminal 1: Start Ollama
ollama serve

# Terminal 2: Download model (first time only)
ollama pull llama2

# Terminal 3: Start Spring Boot app
cd /Users/vicky/Documents/GitHub/langChain4jDemo
./mvnw spring-boot:run

# Terminal 4: Test the app
curl -X POST http://localhost:8080/api/basic-chat/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Hello! Can you introduce yourself?"}'
```

## 📞 Need Help?

If you get stuck:
1. Check Ollama is running: `ps aux | grep ollama`
2. Check models: `ollama list`
3. Check logs in the Spring Boot terminal
4. Verify port 11434 is accessible: `lsof -i :11434`

---

**Remember:** You need **two running processes**:
1. **Ollama** (provides the LLM) - `ollama serve`
2. **Spring Boot** (provides the REST APIs) - `./mvnw spring-boot:run`

Once both are running, you're ready to explore LangChain4j features! 🚀
