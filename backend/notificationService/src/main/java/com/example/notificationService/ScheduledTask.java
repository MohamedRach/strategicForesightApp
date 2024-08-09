package com.example.notificationService;

import com.example.notificationService.Domain.NotificationEntity;
import com.example.notificationService.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduledTask {

    private final NotificationService notificationService;
    private final KafkaTemplate<String, NotificationEntity> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final  String topic;

    public ScheduledTask(NotificationService notificationService, KafkaTemplate<String, NotificationEntity> kafkaTemplate, ObjectMapper objectMapper, @Value("${sadek.kafka.topic}") String topic) {
        this.notificationService = notificationService;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topic = topic;
    }
    /*
    @Scheduled(fixedRate = 50000)
    public void scheduledTask() {
        List<NotificationEntity> notifications = notificationService.findAll();
        LocalDateTime now = LocalDateTime.now();
        for (NotificationEntity notification : notifications) {
            LocalDateTime updatedAt = notification.getUpdatedAt();
            Duration duration = Duration.between(updatedAt, now);
            if (duration.toDays() >= 1 && notification.getFrequency().equals("every day")) {
                System.out.println("day");
                try {
                    sendNotification(notification);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (duration.toDays() >= 7 && notification.getFrequency().equals("every week")) {
                System.out.println("week");
                try {
                    sendNotification(notification);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (duration.toDays() >= 30 && notification.getFrequency().equals("every month")) {
                System.out.println("month");
                try {
                    sendNotification(notification);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
    private void sendNotification(NotificationEntity notification) throws Exception {
        String payload = objectMapper.writeValueAsString(notification);
        kafkaTemplate.send(topic, notification);
    }
    */


}
