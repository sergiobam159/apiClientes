package com.clientes.dto.mapper;

import com.clientes.dto.ClienteRequestDTO;
import com.clientes.entidad.Cliente;

public class ClienteMapper {

    public static Cliente toEntity(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellidoPaterno(dto.getApellidoPaterno());
        cliente.setApellidoMaterno(dto.getApellidoMaterno());
        cliente.setActivo(true); // Por defecto
        cliente.setFechaCreacion(java.time.LocalDateTime.now());
        return cliente;
    }
}
