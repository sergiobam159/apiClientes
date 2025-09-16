package com.clientes.api_clientes.servicio;

import com.clientes.api_clientes.dto.ClienteListadoDTO;
import com.clientes.api_clientes.dto.ClienteRequestDTO;
import com.clientes.api_clientes.dto.ClienteResponseDTO;
import com.clientes.api_clientes.dto.mapper.ClienteMapper;
import com.clientes.api_clientes.entidad.Cliente;
import com.clientes.api_clientes.repositorio.ClienteRepositorio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;


    public ClienteResponseDTO crearCliente (ClienteRequestDTO clienteRequestDTO){

        Cliente cliente = ClienteMapper.toCliente(clienteRequestDTO);
        Cliente clienteGuardado = clienteRepositorio.save(cliente);
        log.info("Cliente creado correctamente");
    return ClienteMapper.toRespuestaDTO(clienteGuardado);
    }


    public List<ClienteListadoDTO> listarClientes() {
        return clienteRepositorio.findAll().stream()
                .map(cliente -> ClienteMapper.toListadoDTO(cliente, construirNombreCompleto(cliente)))
                .toList();
    }

    private String construirNombreCompleto(Cliente cliente) {
        String nombreCompleto = cliente.getNombre() + " " + cliente.getApellidoPaterno();

        nombreCompleto+= Optional.ofNullable(cliente.getApellidoMaterno()).
                filter(apellidoMaterno -> !apellidoMaterno.isEmpty())
                .map(apellido -> " " + apellido)
                .orElse("");

        return nombreCompleto;
    }

}
