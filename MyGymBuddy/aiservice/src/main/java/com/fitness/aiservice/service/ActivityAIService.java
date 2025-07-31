package com.fitness.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;

    public Recommendation generateRecommendation(Activity activity) {
        try {
            String prompt = createPromptForActivity(activity);
            String aiResponse = geminiService.getAnswer(prompt);
            log.info("RAW AI RESPONSE: {}", aiResponse);

            String jsonText = extractJsonFromGeminiResponse(aiResponse);
            log.info("CLEAN JSON CONTENT: {}", jsonText);

            return processAiRecommendation(activity, jsonText);
        } catch (Exception e) {
            log.error("Error during AI response processing", e);
            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation("Failed to process AI response.")
                    .improvements(Collections.emptyList())
                    .suggestions(Collections.emptyList())
                    .safety(Collections.emptyList())
                    .createAt(LocalDateTime.now())
                    .build();
        }
    }

    private String extractJsonFromGeminiResponse(String rawResponse) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(rawResponse);

        JsonNode textNode = root.path("candidates")
                .get(0)
                .path("content")
                .path("parts")
                .get(0)
                .path("text");

        return textNode.asText()
                .replaceAll("(?i)```json\\s*", "")
                .replaceAll("```", "")
                .trim();
    }

    private Recommendation processAiRecommendation(Activity activity, String cleanedJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode analysisJson = objectMapper.readTree(cleanedJson);

            JsonNode analysisNode = analysisJson.path("analysis");
            StringBuilder fullAnalysis = new StringBuilder();

            addAnalysisSection(fullAnalysis, analysisNode, "overall", "Overall:");
            addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace:");
            addAnalysisSection(fullAnalysis, analysisNode, "heartRate", "Heart Rate:");
            addAnalysisSection(fullAnalysis, analysisNode, "caloriesBurned", "Calories:");

            List<String> improvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
            List<String> safety = extractSafetyGuidelines(analysisJson.path("safety"));

            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation(fullAnalysis.toString().trim())
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .createAt(LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return createDefaultRecommendation(activity);
        }
    }

    private Recommendation createDefaultRecommendation(Activity activity) {
        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityId(activity.getType())
                .recommendation("Unable to generate detailed analysis")
                .improvements(Collections.singletonList("Continue with your routinr"))
                .suggestions(Collections.singletonList("Consider consulting an expert"))
                .safety(Arrays.asList(
                        "Always warm up before exercise",
                        "Stay hydrated",
                        "Listen to your body requirements"
                ))
                .createAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractSafetyGuidelines(JsonNode safetyNode) {
        List<String> safetyGuidelines = new ArrayList<>();
        if (safetyNode != null && safetyNode.isArray()) {
            for (JsonNode guideline : safetyNode) {
                safetyGuidelines.add(guideline.asText());
            }
        }
        return safetyGuidelines.isEmpty()
                ? Collections.singletonList("No specific safety guidelines provided.")
                : safetyGuidelines;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestions = new ArrayList<>();
        if (suggestionsNode != null && suggestionsNode.isArray()) {
            for (JsonNode suggestion : suggestionsNode) {
                String workout = suggestion.path("workout").asText();
                String description = suggestion.path("description").asText();
                suggestions.add(String.format("%s: %s", workout, description));
            }
        }
        return suggestions.isEmpty()
                ? Collections.singletonList("No specific suggestions provided.")
                : suggestions;
    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements = new ArrayList<>();
        if (improvementsNode != null && improvementsNode.isArray()) {
            for (JsonNode improvement : improvementsNode) {
                String area = improvement.path("area").asText();
                String detail = improvement.path("recommendation").asText();
                improvements.add(String.format("%s: %s", area, detail));
            }
        }
        return improvements.isEmpty()
                ? Collections.singletonList("No specific improvements provided.")
                : improvements;
    }

    private void addAnalysisSection(StringBuilder sb, JsonNode node, String key, String label) {
        if (node.hasNonNull(key)) {
            sb.append(label).append(" ")
                    .append(node.get(key).asText())
                    .append("\n\n");
        }
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
    Analyze this fitness activity and provide detailed recommendations in the json format
    {
        "analysis":{
            "overall": "Overall analysis here",
            "pace": "Pace analysis here",
            "heartRate": "Heartrate analysis here",
            "caloriesBurned": "Calorie analysis here"
        },
        "improvements": [
        {
            "area": "Area name",
            "recommendation": "Detailed recommendation"
        }
        ],
        "suggestions":[
        {
            "workout": "Workout name",
            "description": "Detailed workout description"
        }
        ],
        "safety":[
            "Safety point 1",
            "Safety point 2"
        ]
    }
    Analyze this activity:
    Activity Type: %s
    Duration %d minutes
    Calories Burned: %d
    Additional Metrics: %s
    
    Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
    Ensure the response follows the EXACT JSON format shown above.
    """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()
        );

    }
}
