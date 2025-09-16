package com.clientes.api_clientes.integracion;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TramaEventHub {

    private String analyticsTraceSource;   // cabecera + consumerid ->  "application-SMP"
    private String applicationId;          // applicationId -> "SMP"
    private String channelOperationNumber; // en el log pero no lo define, otro header? autogenerado?
    private String consumerId;             // consumerId lo mismo que applicationId -> "SMP"
    private String currentDate;            // fecha formato 2024-10-17T11:32:22.782-05
    private String customerId;             // id del cliente
    private String region;                 // region seteada en properties imagino
    private String statusCode;             // "0000" éxito
    //si la petición es con un codigo
    //que no existe el cliente se define en la BD
    //coloco codigo 9999 ¿?
    private long timestamp;                // epoch ms? 2024-10-17T11:32:22.782-05 -> 1737073942782
    private String traceId;                // extraído de traceparent [2]
    private String inbound;                // request como JSON string
    private String outbound;               // response como JSON string
    private String transactionCode;        // 002 consulta y 102 creación
//Teniendo en cuenta
//que este codigos puede estar en un config
//map y la configuracion sea desde los
//microservicios.  ¿?
}

