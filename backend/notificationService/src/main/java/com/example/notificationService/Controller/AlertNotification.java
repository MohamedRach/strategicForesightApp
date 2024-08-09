package com.example.notificationService.Controller;

import com.example.notificationService.Domain.NotificationResponse;
import com.example.notificationService.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AlertNotification {

    private final AlertService alertService;

    @Autowired
    public AlertNotification(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping(path = "/alert")
    public List<NotificationResponse> getAlerts() {
        return alertService.findbyCountIsNotNull();
    }

    @DeleteMapping(path = "/alert/{id}")
    public void deleteAlert(@PathVariable Long id) {
        alertService.deleteAlert(id);
    }

    @PostMapping(path = "/alert")
    public NotificationResponse addAlert(@RequestBody NotificationResponse notificationResponse) {
        return alertService.createAlert(notificationResponse);
    }

}
