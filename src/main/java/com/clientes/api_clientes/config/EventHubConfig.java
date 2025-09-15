package com.clientes.api_clientes.config;

import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventHubConfig {

    @Value("${app.analytics.eventhub.connection-string}")
    private String connectionString;

    @Value("${app.analytics.eventhub.name}")
    private String eventHubName;

    @Bean
    public EventHubProducerClient eventHubProducerClient() {
        return new EventHubClientBuilder()
                .connectionString(connectionString, eventHubName)
                .buildProducerClient();
    }
}
