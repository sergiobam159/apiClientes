package com.clientes.api_clientes.servicio;


import com.clientes.api_clientes.constantes.TipoDispositivo;
import com.clientes.api_clientes.dto.ClienteListadoDTO;
import com.clientes.api_clientes.dto.ClienteRequestDTO;
import com.clientes.api_clientes.dto.ClienteResponseDTO;
import com.clientes.api_clientes.integracion.TramaEventHub;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import java.time.Duration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnaliticaServicio {

    private final ObjectMapper objectMapper;
    private final EventHubServicio eventHubSender;

    @Value("${app.eventHub.region}")
    private String region;

    @Value("${app.eventHub.transaction.crearCliente}")
    private String transactionCodeCreate;

    @Value("${app.eventHub.transaction.listarClientes}")
    private String transactionCodeList;


    @Async("eventHubExecutor")
    public void logCreacionClienteAsync(
            String consumerId,
            String traceparent,
            TipoDispositivo tipoDispositivo,
            String deviceId,
            String channelOperationNumber, // puede venir null
            ClienteRequestDTO request,
            ClienteResponseDTO response
    ){
        try {
             long timestamp = System.currentTimeMillis();
                String applicationId = consumerId; // asumiendo que son iguales
                String eventHubTraceSource = "application-" + applicationId;
                String currentDate = java.time.OffsetDateTime.now().toString();
                String customerId = response.getId();
                String statusCode = "0000"; // éxito, es otro codigo para el list, podria ponerlo en el properties
                String  traceId = extraer(traceparent, 1);

                //NO SE DEFINE SI VIENE O NO, PERO EN EL EJEMPLO ES UN TIMESTAMP DE LA FECHA
            String chaOpeNumbString = (channelOperationNumber == null || channelOperationNumber.isBlank())
                    ? String.valueOf(timestamp)
                    : channelOperationNumber;

            String outboundString = toJson(response);
            String inboundString = toJson(request);
            inboundString = wrapWithDeviceInfo(inboundString, tipoDispositivo, deviceId);



            TramaEventHub trama = TramaEventHub.builder()
                    .analyticsTraceSource(eventHubTraceSource)
                    .applicationId(applicationId)
                    .channelOperationNumber(chaOpeNumbString)
                    .consumerId(consumerId)
                    .currentDate(currentDate)
                    .customerId(customerId)
                    .region(region)
                    .statusCode(statusCode)
                    .timestamp(timestamp)
                    .traceId(traceId)
                    .inbound(inboundString)
                    .outbound(outboundString)
                    .transactionCode(transactionCodeCreate)
                    .build();

            String jsonTrama = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(trama);

            log.info("ANALYTICS crearCliente {}", jsonTrama);
            // Envío a Event Hubs (compacto) usando traceId como partitionKey
            String jsonTramaCompact = objectMapper.writeValueAsString(trama);
            eventHubSender.enviarJson(jsonTramaCompact, traceId);

        } catch (Exception e) {
            log.error("ANALYTICS error al construir/loguear la trama", e);
        }
    }



    @Async("eventHubExecutor")
    public void logListadoClienteAsync(
            String consumerId,
            String traceparent,
            TipoDispositivo tipoDispositivo,
            String deviceId,
            String channelOperationNumber, // puede venir null
            List<ClienteListadoDTO> response
    ){
        try {
            long timestamp = System.currentTimeMillis();
            String applicationId = consumerId; // asumiendo que son iguales
            String eventHubTraceSource = "application-" + applicationId;
            String currentDate = java.time.OffsetDateTime.now().toString();
           // String customerId = response.getId();
            String statusCode = "0000"; // éxito
            String  traceId = extraer(traceparent, 1);

            //NO SE DEFINE SI VIENE O NO, PERO EN EL EJEMPLO ES UN TIMESTAMP DE LA FECHA
            String chaOpeNumbString = (channelOperationNumber == null || channelOperationNumber.isBlank())
                    ? String.valueOf(timestamp)
                    : channelOperationNumber;

            String outboundString = toJson(response);
            //inboundString = wrapWithDeviceInfo(inboundString, tipoDispositivo, deviceId);



            TramaEventHub trama = TramaEventHub.builder()
                    .analyticsTraceSource(eventHubTraceSource)
                    .applicationId(applicationId)
                    .channelOperationNumber(chaOpeNumbString)
                    .consumerId(consumerId)
                    .currentDate(currentDate)
                    //.customerId(customerId)
                    .region(region)
                    .statusCode(statusCode)
                    .timestamp(timestamp)
                    .traceId(traceId)
                    .outbound(outboundString)
                    .transactionCode(transactionCodeList)
                    .build();

            String jsonTrama = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(trama);

            log.info("ANALYTICS ListarCliente {}", jsonTrama);
            // Envío a Event Hubs (compacto) usando traceId como partitionKey
            String jsonTramaCompact = objectMapper.writeValueAsString(trama);
            eventHubSender.enviarJson(jsonTramaCompact, traceId);

        } catch (Exception e) {
            log.error("ANALYTICS error al construir/loguear la trama", e);
        }
    }


    private String extraer(String traceparent, int indice) {
        try {
            String[] parts = traceparent.split("-");
                return parts[indice];
        } catch (Exception ignored) {}
        return null;
    }

    // ESTO SSE PUEDE QUITAR=?? DEJAR SOLO objectMapper.writeValueAsString(obj)
    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "error a convertir a json: " + e.getMessage();
        }
    }

    //envuelve el objeto json seteando primero deviceid y devicetype
    private String wrapWithDeviceInfo(String requestJson, TipoDispositivo tipoDispositivo, String deviceId) {
        // Inserta deviceType y deviceId como un objeto "inbound" similar al del ejemplo
        String type = tipoDispositivo != null ? tipoDispositivo.name() : null;
        String id = deviceId != null ? deviceId : null;
        // requestJson ya es un JSON; lo metemos como "document" con doble escape al serializarse como String
        return String.format("{\"deviceId\":\"%s\",\"deviceType\":\"%s\",\"document\":%s}",
                id, type, requestJson);
    }



}
