package com.spring_ai_ollama.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class IndexHtmlUnitTest {

    private static Document doc;

    @BeforeAll
    static void loadHtml() throws IOException {
        // Path to your static index.html file
        File htmlFile = new File("src/main/resources/static/index.html");
        assertTrue(htmlFile.exists(), "index.html file must exist in static resources");
        doc = Jsoup.parse(htmlFile, StandardCharsets.UTF_8.name());
    }

    @Test
    void testCriticalDOMElementsExist() {
        // Verify Title
        assertEquals("Spring AI + Llama 3.2 Chat", doc.title());

        // Verify Input field
        Element userInput = doc.getElementById("userInput");
        assertNotNull(userInput, "Textarea with id 'userInput' should exist");
        assertEquals("textarea", userInput.tagName());

        // Verify Send button
        Element sendBtn = doc.getElementById("sendBtn");
        assertNotNull(sendBtn, "Button with id 'sendBtn' should exist");
        assertEquals("askAI()", sendBtn.attr("onclick"));

        // Verify Response Containers
        assertNotNull(doc.getElementById("responseContainer"), "Container element should exist");
        assertNotNull(doc.getElementById("aiResponse"), "AI Response display element should exist");
    }

    @Test
    void testJavaScriptFunctionSignatureIsPresent() {
        // Ensure that the askAI logic script block is hardcoded or present
        String htmlContent = doc.html();
        assertTrue(htmlContent.contains("async function askAI()"), "The JavaScript file must declare askAI()");
        assertTrue(htmlContent.contains("fetch(url)"), "The JavaScript must utilize fetch API mechanisms");
    }
}
