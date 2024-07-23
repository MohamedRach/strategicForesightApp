package com.example.SearchService;

import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;

import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.example.SearchService.Repository")
public class ElasticSearchConfig extends ElasticsearchConfiguration {

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo("3fd97bdf9fa8:9200")
                .usingSsl(buildSSLContext())
                .withBasicAuth("elastic", "12345")
                .withSocketTimeout(Duration.ofSeconds(30))
                .withConnectTimeout(Duration.ofSeconds(30))// Disable hostname verification
                .build();
    }

    private static SSLContext buildSSLContext() {
        try {
            return SSLContextBuilder.create()
                    .loadTrustMaterial(null, (x509Certificates, s) -> true)
                    .build();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException("Failed to create SSLContext", e);
        }
    }
}