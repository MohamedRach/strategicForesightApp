package com.example.notificationService;

import com.example.notificationService.Domain.NotificationEntity;
import com.example.notificationService.Domain.NotificationResponse;
import com.example.notificationService.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private final AlertService alertService;

    @Autowired
    public NotificationConsumer(AlertService alertService) {
        this.alertService = alertService;
    }
    @KafkaListener(topics = "${sadek.kafka.topicc}", groupId = "my-group")
    public void listen(NotificationResponse message) {
        alertService.createAlert(message);
    }
}
