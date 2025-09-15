package com.clientes.api_clientes.servicio;


import com.clientes.api_clientes.constantes.TipoDispositivo;
import com.clientes.api_clientes.dto.ClienteRequestDTO;
import com.clientes.api_clientes.dto.ClienteResponseDTO;
import com.clientes.api_clientes.integracion.TramaEventHub;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnaliticaServicio {

    private final ObjectMapper objectMapper;

    @Value("${app.analytics.region}")
    private String region;
   // @Value("${app.analytics.application-prefijo:application-}")
    //private String applicationPrefijo;
    // Código de transacción para "registro de cliente"
    @Value("${app.analytics.transaction.create}")
    private String transactionCodeCreate;


    @Async("analyticsExecutor")
    public void logCreacionClienteAsync(
            String consumerId,
            String traceparent,
            TipoDispositivo tipoDispositivo,
            String deviceId,
            String channelOperationNumber, // puede venir null
            ClienteRequestDTO request,
            ClienteResponseDTO response
    ){
             long timestamp = System.currentTimeMillis();
                String applicationId = consumerId; // asumiendo que son iguales
                String analyticsTraceSource = "application-" + consumerId;

                String currentDate = java.time.OffsetDateTime.now().toString();

                String customerId = response.getId();

                String statusCode = "0000"; // éxito

        String traceId = extraer(traceparent, 1);

        String inboundString = toJson(request);

        //PONERLE OPTIONAL?
        if (tipoDispositivo != null || deviceId != null) {
            inboundString = wrapWithDeviceInfo(inboundString, tipoDispositivo, deviceId);
        }

        String outboundString = toJson(response);

        //OPTIONAL?
        String chaOpeNumbString = (channelOperationNumber == null || channelOperationNumber.isBlank())
                ? String.valueOf(timestamp)
                : channelOperationNumber;


        TramaEventHub trama = TramaEventHub.builder()
                .analyticsTraceSource(analyticsTraceSource)
                .applicationId(applicationId)
                .channelOperationNumber(chaOpeNumbString)
                .consumerId(consumerId)
                .currentDate(currentDate)
                .customerId(response != null ? response.getId() : null)
                .region(region)
                .statusCode("0000")
                .timestamp(timestamp)
                .traceId(traceId)
                .inbound(inboundString)
                .outbound(outboundString)
                .transactionCode(transactionCodeCreate)
                .build();

    }

    private String extraer(String traceparent, int index) {
        try {
            String[] parts = traceparent.split("-");
                return parts[index];
        } catch (Exception ignored) {}
        return null;
    }

    // ESTO SSE PUEDE QUITAR=?? DEJAR SOLO objectMapper.writeValueAsString(obj)
    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "\"<no-serializable>\"";
        }
    }

    //ESTO QUE HACE?
    private String wrapWithDeviceInfo(String requestJson, TipoDispositivo tipoDispositivo, String deviceId) {
        // Inserta deviceType y deviceId como un objeto "inbound" similar al del ejemplo
        String type = tipoDispositivo != null ? tipoDispositivo.name() : null;
        String id = deviceId != null ? deviceId : null;
        // requestJson ya es un JSON; lo metemos como "document" con doble escape al serializarse como String
        return String.format("{\"deviceId\":\"%s\",\"deviceType\":\"%s\",\"document\":%s}",
                id, type, requestJson);
    }

}
