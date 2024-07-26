package com.example.SearchService;

import co.elastic.clients.elasticsearch._types.analysis.Analyzer;
import co.elastic.clients.json.DelegatingDeserializer;
import co.elastic.clients.json.ObjectDeserializer;
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
                .connectedTo("f7b075c45a8f:9200")
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