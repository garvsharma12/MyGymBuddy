package com.fitness.aiservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "recommendation")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recommendation {

    @Id
    private String id;
    private String activityId;
    private String userId;
    private String recommendation;
    private String activityType; // ✅ Add this field
    private List<String> safety;
    private List<String> improvements;
    private List<String> suggestions;

    @CreatedDate
    private LocalDateTime createAt;
}
