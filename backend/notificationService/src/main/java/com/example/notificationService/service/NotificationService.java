package com.example.notificationService.service;

import com.example.notificationService.Domain.NotificationEntity;
import com.example.notificationService.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {this.notificationRepository = notificationRepository;}

    public NotificationEntity save(NotificationEntity notification) {
        return notificationRepository.save(notification);
    }

    public List<NotificationEntity> findAll() {
        return notificationRepository.findAll();
    }

    public NotificationEntity findById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }

}
