package com.clientes.api_clientes.servicio;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.azure.messaging.eventhubs.models.CreateBatchOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
//ANALIZAR!
public class EventHubServicio {
    private final EventHubProducerClient producerClient;

    public void enviarJson(String json, String partitionKey) {
        try {
            var options = new CreateBatchOptions();
            if (partitionKey != null && !partitionKey.isBlank()) {
                options.setPartitionKey(partitionKey);
            }
            var batch = producerClient.createBatch(options);

            producerClient.send(batch);
            log.debug("Evento enviado a Event Hubs ({} bytes).", json.length());
        } catch (Exception e) {
            log.error("Error enviando evento a Event Hubs", e);
        }
    }
}
