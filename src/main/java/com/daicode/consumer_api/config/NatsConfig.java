package com.daicode.consumer_api.config;

import com.daicode.consumer_api.exception.NatsConnectionException;
import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Duration;

@Configuration
@Slf4j
public class NatsConfig {

    @Value("${nats.url}")
    private String natsURL;
    @Value("${nats.maxReconnects}")
    private Integer maxReconnects;
    @Value("${nats.secondsReconnectWait}")
    private Integer secondsReconnectWait;
    private Connection connection;

    @Bean
    public Connection natsConnection() {

        var options = new Options.Builder()
                .server(natsURL)
                .maxReconnects(maxReconnects)
                .reconnectWait(Duration.ofSeconds(secondsReconnectWait))
                .build();
        try {
            connection = Nats.connect(options);
            log.info("Connected to NATS at {}", natsURL);
            return connection;
        } catch (IOException | InterruptedException e) {
            log.error("Failed to connect to Nats at {}::{}", natsURL, e.getMessage(), e);
            Thread.currentThread().interrupt();
            throw new NatsConnectionException("Failed to connect to NATS at ".concat(natsURL));
        }
    }

    @PreDestroy
    public void closeConnection() {
        if (this.connection != null && this.connection.getStatus() == Connection.Status.CONNECTED) {
            try {
                this.connection.close();
            } catch (InterruptedException e) {
                log.error("Interrupted while closing NATS connection: {}", e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
