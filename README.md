# Spring AI Ollama Playground

A minimalist Spring Boot application utilizing **Spring AI** to interface locally with **Ollama** models like **DeepSeek-R1** and **Llama 3.2**. This project provides a clean REST API, interactive API documentation via Swagger UI, and a simple frontend interface to submit prompts and see model answers in real-time. It is pre-configured with industry-standard code quality, mutation testing, and modern E2E verification engines.

---

## 🛠️ Features

* **Spring AI Chat Client:** Built using the recommended fluent, autoconfigured `ChatClient.Builder` API.
* **Local LLM Execution:** Configured out-of-the-box to hit a local Ollama instance running `deepseek-r1:1.5b` or `llama3.2`.
* **Built-in Open API / Swagger UI:** Interactive endpoint documentation accessible directly from your browser.
* **Responsive Frontend:** A modern, single-page web UI tailored for real-time model interaction.
* **Advanced Testing & Quality Suite:** 
  * **Mutation Testing:** Powered by Pitest to ensure the strength of test suites.
  * **E2E & Integration:** Browser automation via Microsoft Playwright and DOM assertions using jsoup.
  * **Coverage Tracking:** Code coverage processing configured via JaCoCo.

---

## 📋 Prerequisites

Before running the application, make sure you have installed:

1. **Java 21** (Configured via Gradle Toolchain).
2. **Ollama** (Download from [ollama.com](https://ollama.com)).
3. Pull the required models locally via your terminal:
   ```bash
   # Primary model configured in application.properties
   ollama run deepseek-r1:1.5b

   # Optional fallback model
   ollama run llama3.2
   ```

---

## ⚙️ Configuration (`application.properties`)

The application connects to Ollama on its default port and sets up Swagger custom paths:

```properties
spring.application.name=demo

# Ollama Connection Settings
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=deepseek-r1:1.5b

# Swagger Open API Documentation Slugs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs
```

---

## 🚀 Getting Started

### 1. Clone & Run the Backend
Navigate to the root directory of your project and run the Spring Boot application using the Gradle wrapper:

```bash
# Start the application locally
./gradlew bootRun
```

### 2. Run Verification & Testing Tasks
This project includes automated testing routines for metrics, mutations, and browser interaction:

```bash
# Run standard Unit/Integration tests + Generate JaCoCo reports
./gradlew clean test

# Run Mutation Testing (Pitest)
./gradlew pitest
```

### 3. Access the Application

Once the server has fully initialized (typically listening on port `8080`), you can access the following endpoints:

* **Web UI Playground:** `http://localhost:8080/` (Ensure the template file is placed inside `src/main/resources/static/index.html`)
* **Interactive Swagger UI:** `http://localhost:8080/swagger-ui.html`
* **Raw OpenAPI JSON Spec:** `http://localhost:8080/v3/api-docs`
* **Test Verification Outputs:**
  * JaCoCo Code Coverage: Check `build/reports/jacoco/test/html/index.html`
  * Pitest Mutation Report: Check `build/reports/pitest/index.html`

---

## 🔌 API Reference

### Generate AI Response

* **Endpoint:** `GET /spring-ai/api-chat/generate`
* **Query Parameters:**
  * `message` (String, Optional): The text prompt you want to submit to the model. Defaults to `"Hello! Tell me a fun fact."`
  * **Example Request:**
    ```bash
    curl -X GET "http://localhost:8080/spring-ai/api-chat/generate?message=Explain+quantum+physics+in+one+sentence"
    ```
  * **Sample Plaintext Response:**
    ```text
    Quantum physics is the study of how matter and light behave at the atomic and subatomic level, where particles can exist in multiple states at once.
    ```
