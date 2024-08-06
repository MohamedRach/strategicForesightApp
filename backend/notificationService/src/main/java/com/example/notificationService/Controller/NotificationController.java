package com.example.notificationService.Controller;

import com.example.notificationService.Domain.NotificationEntity;
import com.example.notificationService.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(path = "/notification")
    public List<NotificationEntity> getnotifications() {
        return this.notificationService.findAll();
    }

    @PostMapping(path = "/notification")
    public NotificationEntity saveNotification(@RequestBody NotificationEntity notification) {
        return this.notificationService.save(notification);
    }

}
