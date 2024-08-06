package com.example.notificationService.config;

import com.example.notificationService.Domain.NotificationEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "sadek.kafka")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class kafkaConfigProps {

    private String topic;
    @Bean
    public ProducerFactory<String, NotificationEntity> producerFactory()
    {

        // Creating a Map
        Map<String, Object> config = new HashMap<>();

        // Adding Configuration

        // 127.0.0.1:9092 is the default port number for
        // kafka
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "127.0.0.1:9092");
        config.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        config.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    // Annotation
    @Bean
    // Method
    public KafkaTemplate kafkaTemplate()
    {

        return new KafkaTemplate<>(producerFactory());
    }
}
