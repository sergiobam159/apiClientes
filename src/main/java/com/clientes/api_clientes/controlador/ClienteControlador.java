package com.clientes.api_clientes.controlador;

import com.clientes.api_clientes.constantes.TipoDispositivo;
import com.clientes.api_clientes.dto.ClienteListadoDTO;
import com.clientes.api_clientes.dto.ClienteRequestDTO;
import com.clientes.api_clientes.dto.ClienteResponseDTO;
import com.clientes.api_clientes.servicio.AnaliticaServicio;
import com.clientes.api_clientes.servicio.ClienteServicio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/clientes")
@Slf4j
public class ClienteControlador {
    @Autowired
    private ClienteServicio clienteServicio;

    @Autowired
    private AnaliticaServicio analiticaServicio;

    @PostMapping
    public ClienteResponseDTO crearCliente (
            @RequestHeader("consumerId") //OK
            @NotBlank(message = "consumerId es obligatorio")
            String consumerId,
            @RequestHeader("traceparent")
            @Pattern( //formato de traceparen ejemplo 00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01
                    regexp = "^[0-9a-f]{2}-[0-9a-f]{32}-[0-9a-f]{16}-[0-9a-f]{2}$",
                    flags = Pattern.Flag.CASE_INSENSITIVE,
                    message = "error en el formato del traceparent, debe seguir el formato de W3C"
            )String traceparent, //OK
            @RequestHeader("deviceType")
            @NotNull(message = "tipoDispositivo es obligatorio")
            TipoDispositivo tipoDispositivo, //OK
            @RequestHeader("deviceId")
            @NotBlank(message = "deviceId es obligatorio")
            String deviceId, //OK
            @RequestHeader(value = "channelOperationNumber", required = false) // no sé ah, pero bueno, no entiendo bien esto la docu
            String channelOperationNumber,
            @Valid
            @RequestBody ClienteRequestDTO clienteRequest){

       log.info("Recibida solicitud para crear cliente");
        ClienteResponseDTO clienteRespuesta = clienteServicio.crearCliente(clienteRequest);
        log.info("Cliente creado correctamente ");

        analiticaServicio.logCreacionClienteAsync(
                consumerId, traceparent, tipoDispositivo, deviceId, channelOperationNumber, clienteRequest, clienteRespuesta
        );

        return clienteRespuesta;
    }

    @GetMapping
    public List<ClienteListadoDTO> listarClientes(
            @RequestHeader("consumerId") @NotBlank String consumerId,
            @RequestHeader("traceparent")
            @NotBlank
            @Pattern(
                    regexp = "^[0-9a-f]{2}-[0-9a-f]{32}-[0-9a-f]{16}-[0-9a-f]{2}$",
                    flags = Pattern.Flag.CASE_INSENSITIVE
            )
            String traceparent,
            @RequestHeader("deviceType") TipoDispositivo tipoDispositivo,
            @RequestHeader("deviceId") @NotBlank String deviceId
    ){

        return clienteServicio.listarClientes();

    }



}
