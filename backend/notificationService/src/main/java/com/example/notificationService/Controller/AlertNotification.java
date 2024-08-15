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

    @GetMapping(path = "/notification/alert")
    public List<NotificationResponse> getAlerts() {
        return alertService.findbyCountIsNotNull();
    }

    @DeleteMapping(path = "/notification/alert/{id}")
    public void deleteAlert(@PathVariable Long id) {
        alertService.deleteAlert(id);
    }

    @PostMapping(path = "/notification/alert")
    public NotificationResponse addAlert(@RequestBody NotificationResponse notificationResponse) {
        System.out.println("hhhhhhh");
        return alertService.createAlert(notificationResponse);
    }

}
