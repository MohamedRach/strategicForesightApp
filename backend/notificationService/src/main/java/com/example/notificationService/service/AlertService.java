package com.example.notificationService.service;

import com.example.notificationService.Domain.NotificationResponse;
import com.example.notificationService.Repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    @Autowired
    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public NotificationResponse createAlert(NotificationResponse alert) {
        return alertRepository.save(alert);
    }

    public List<NotificationResponse> getAllAlerts() {
        return alertRepository.findAll();
    }

    public void deleteAlert(long id) {
        alertRepository.deleteById(id);
    }

    public List<NotificationResponse> findbyCountIsNotNull() {
        return alertRepository.findByCountIsNotNull();
    }

}
