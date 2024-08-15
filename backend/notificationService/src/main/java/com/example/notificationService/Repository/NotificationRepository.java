package com.example.notificationService.Repository;

import com.example.notificationService.Domain.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE NotificationEntity n SET n.updatedAt = :now WHERE n.id = :id")
    int updateUpdatedAt(Long id, LocalDateTime now);
}
