package com.clientes.api_clientes.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClienteResponseDTO {

    private String id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private LocalDateTime fechaCreacion;
    private boolean activo;
}
