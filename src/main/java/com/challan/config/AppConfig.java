package com.challan.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Bean
    public ChatLanguageModel geminiChatModel() {
        return GoogleAiGeminiChatModel.builder()
                .apiKey(geminiApiKey)
                // ✅ Latest free-tier model
                .modelName("gemini-3.1-flash-lite-preview")
                // Low temp = precise legal output
                .temperature(0.1) // Should use 1.0 as mentioned in website
                // Enough for full JSON + reasoning
                .maxOutputTokens(4096)
                .build();
    }
}