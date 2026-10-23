package com.spring_ai_ollama.demo;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IndexHtmlIntegrationTest {

    @LocalServerPort
    private int port;

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        // Use headless mode so tests execute cleanly in CI pipeline runners
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
    }

    @AfterAll
    static void closeBrowser() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        if (context != null) context.close();
    }

    @Test
    void testEndToEndChatFlow() {
        String baseUrl = "http://localhost:" + port + "/index.html";
        
        // 1. Route/Intercept Network Request going to backend to provide static predictable mock return
        page.route("**/spring-ai/api-chat/generate?message=*", route -> {
            route.fulfill(new Route.FulfillOptions()
                    .setStatus(200)
                    .setContentType("text/plain")
                    .setBody("Mocked Llama 3.2 answer from integration testing pipeline."));
        });

        // 2. Navigate to your app landing UI
        page.navigate(baseUrl);
        assertEquals("Spring AI + Llama 3.2 Chat", page.title());

        // 3. Populate Input Field
        page.locator("#userInput").fill("Hello integration test runner tool");

        // 4. Hit Send Button Action trigger
        page.locator("#sendBtn").click();

        // 5. Verify the loading element triggers (Optional assertion if UI latency occurs)
        String outputBoxSelector = "#aiResponse";
        page.waitForSelector(outputBoxSelector);

        // 6. Assert that the server response replaces placeholder successfully inside browser window
        String displayedText = page.locator(outputBoxSelector).textContent();
        assertTrue(displayedText.contains("Mocked Llama 3.2 answer"), 
                "The UI must accurately render intercepted backend payload output.");
    }
}
