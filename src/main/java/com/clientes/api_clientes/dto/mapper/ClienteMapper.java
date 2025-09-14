package com.clientes.api_clientes.dto.mapper;

import com.clientes.api_clientes.dto.ClienteListadoDTO;
import com.clientes.api_clientes.dto.ClienteRequestDTO;
import com.clientes.api_clientes.dto.ClienteResponseDTO;
import com.clientes.api_clientes.entidad.Cliente;

//para no tener en la capa servicio transformadores para los dto
public class ClienteMapper {

    //Clientedto a cliente
    public static Cliente toCliente(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellidoPaterno(dto.getApellidoPaterno());
        cliente.setApellidoMaterno(dto.getApellidoMaterno());
        cliente.setActivo(true); //seteo true por defecto
        cliente.setFechaCreacion(java.time.LocalDateTime.now()); //seteo la fecha de creacion enel momento en el que recibo el post cliente
        return cliente;
    }

    //cliente a respuesta dto
    public static ClienteResponseDTO toRespuestaDTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setApellidoPaterno(cliente.getApellidoPaterno());
        dto.setApellidoMaterno(cliente.getApellidoMaterno());
        dto.setFechaCreacion(cliente.getFechaCreacion());
        dto.setActivo(cliente.isActivo());
        return dto;
    }
    //cliente a respuesta lista de clientes
    public static ClienteListadoDTO toListadoDTO(Cliente cliente, String nombreCompleto) {
        ClienteListadoDTO dto = new ClienteListadoDTO();
        dto.setId(cliente.getId());
        dto.setNombreCompleto(nombreCompleto);
        return dto;
    }


}
