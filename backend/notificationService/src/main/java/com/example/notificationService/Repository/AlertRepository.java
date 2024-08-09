package com.example.notificationService.Repository;

import com.example.notificationService.Domain.NotificationResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<NotificationResponse, Long> {
    List<NotificationResponse> findByCountIsNotNull();
}
