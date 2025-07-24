package com.example.activityservice.dto;

import lombok.Data;
import model.ActivityType;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityResponse {
    private String id;
    private String userId;
    private Integer duration;
    private ActivityType type;
    private Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String, Object> additionalMetrices;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
