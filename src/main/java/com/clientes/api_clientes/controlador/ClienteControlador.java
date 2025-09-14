package com.clientes.api_clientes.controlador;

import com.clientes.api_clientes.constantes.TipoDispositivo;
import com.clientes.api_clientes.dto.ClienteListadoDTO;
import com.clientes.api_clientes.dto.ClienteRequestDTO;
import com.clientes.api_clientes.dto.ClienteResponseDTO;
import com.clientes.api_clientes.servicio.ClienteServicio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteControlador {
    @Autowired
    private ClienteServicio clienteServicio;

    @PostMapping
    public ClienteResponseDTO crearCliente (
            @RequestHeader("consumerId")
            @NotBlank(message = "consumerId es obligatorio")
            String consumerId,
            @RequestHeader("traceparent")
            @Pattern(
                    regexp = "^[0-9a-f]{2}-[0-9a-f]{32}-[0-9a-f]{16}-[0-9a-f]{2}$",
                    flags = Pattern.Flag.CASE_INSENSITIVE,
                    message = "error en el formato del traceparent, debe seguir el formato de W3C"
            )String traceparent,
            @RequestHeader("deviceType")
            @NotBlank(message = "tipoDispositivo es obligatorio")
            TipoDispositivo tipoDispositivo,
            @RequestHeader("deviceId")
            @NotBlank(message = "deviceId es obligatorio")
            String deviceId,
            @Valid
            @RequestBody ClienteRequestDTO cliente){

            return  clienteServicio.crearCliente(cliente);
    }

    @GetMapping
    public List<ClienteListadoDTO> listarclientes(
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
