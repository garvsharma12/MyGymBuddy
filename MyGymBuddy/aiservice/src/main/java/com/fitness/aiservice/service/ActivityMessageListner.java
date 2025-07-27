package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Activity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ActivityMessageListner {

//    @Value("${rabbitmq.queue.name}")
//    private String queueName;
    private final ActivityAIService  aiService;

    @RabbitListener(queues = "activity.queue")
    public void processActivity(Activity activity) {
        log.info("Received activity for processing: {}", activity.getId());
        log.info("Generated Recommendation: {}", aiService.generateRecommendation(activity));
    }
}
