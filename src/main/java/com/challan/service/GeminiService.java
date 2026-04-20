package com.challan.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class GeminiService {

    @Autowired
    private ChatLanguageModel geminiChatModel;

    private final ObjectMapper objectMapper =
            new ObjectMapper();

    public Map<String, Object> generateChallanData(
            String vehicleNumber,
            String vehicleType,
            String violationType,
            String location,
            String state) throws Exception {

        // System message — sets model behaviour
        String systemPrompt = """
                You are an official Indian traffic law
                enforcement system. You have deep knowledge
                of Motor Vehicles Act 1988 and its 2019
                Amendment. You always respond with precise,
                legally accurate JSON only.
                Never add markdown, backticks, or
                explanation text. Only output valid JSON.
                """;

        // User message — the actual request
        String userPrompt = """
                Generate an official traffic violation
                challan for India with these details:

                Vehicle Number : %s
                Vehicle Type   : %s
                Violation      : %s
                Location       : %s
                State          : %s

                Rules:
                1. Use ONLY real sections from Indian
                   Motor Vehicles Act 1988 / 2019 Amendment.
                2. fineAmount must be exact Indian Rupees
                   figure from 2019 Amendment Act.
                3. isCompoundable = true ONLY if officer
                   can legally accept spot payment.
                4. penaltyDetails must include imprisonment
                   clause if applicable under Indian law,
                   else write exactly:
                   "Fine only. No imprisonment."
                5. Use formal official Indian police
                   language for challanDescription.
                6. Think step by step before answering.

                Respond ONLY in this exact JSON format:
                {
                  "challanDescription": "...",
                  "motorVehiclesActSection": "Section X - Title",
                  "fineAmount": 1000,
                  "penaltyDetails": "...",
                  "isCompoundable": true,
                  "aiSummary": "one line official summary"
                }
                """.formatted(vehicleNumber, vehicleType,
                violationType, location, state);

        // Call Gemini with system + user message
        String rawResponse = geminiChatModel
                .generate(
                        SystemMessage.from(systemPrompt),
                        UserMessage.from(userPrompt))
                .content()
                .text();

        // Clean any accidental markdown
        String cleanJson = rawResponse
                .replaceAll("(?s)```json\\s*", "")
                .replaceAll("```", "")
                .trim();

        // If response has extra text before JSON,
        // extract just the JSON part
        int jsonStart = cleanJson.indexOf("{");
        int jsonEnd   = cleanJson.lastIndexOf("}");
        if (jsonStart != -1 && jsonEnd != -1) {
            cleanJson = cleanJson
                    .substring(jsonStart, jsonEnd + 1);
        }

        return objectMapper.readValue(cleanJson, Map.class);
    }
}