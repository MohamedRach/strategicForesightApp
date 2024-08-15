package com.example.notificationService.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "alert")
@Entity
public class NotificationResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "keywords", joinColumns = @JoinColumn(name = "alert_id"))
    @Column(name = "keyword")
    private List<String> keywords ;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sources", joinColumns = @JoinColumn(name = "alert_id"))
    @Column(name = "source")
    private List<String> sources ;

    @Column(name = "count")
    private Integer count;

    @Column(name = "searchId")
    private Long searchId;
}
