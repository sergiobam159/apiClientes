package com.clientes.servicio;

import com.clientes.dto.ClienteListadoDTO;
import com.clientes.dto.ClienteRequestDTO;
import com.clientes.dto.ClienteResponseDTO;
import com.clientes.dto.mapper.ClienteMapper;
import com.clientes.entidad.Cliente;
import com.clientes.repositorio.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;


    public ClienteResponseDTO crearCliente (ClienteRequestDTO clienteRequestDTO){
        Cliente  cliente= clienteRepositorio.save(ClienteMapper.toCliente(clienteRequestDTO));
    return ClienteMapper.toRespuestaDTO(cliente);
    }


    public List<ClienteListadoDTO> listarClientes() {
        return clienteRepositorio.findAll().stream()
                .map(cliente -> ClienteMapper.toListadoDTO(cliente, construirNombreCompleto(cliente)))
                .toList();
    }

    private String construirNombreCompleto(Cliente cliente) {
        String nombreCompleto = cliente.getNombre() + " " + cliente.getApellidoPaterno();

        nombreCompleto+= Optional.ofNullable(cliente.getApellidoMaterno()).
                filter(apellidoMaterno -> !apellidoMaterno.isEmpty()).
                orElse("");

        if (cliente.getApellidoMaterno() != null && !cliente.getApellidoMaterno().isEmpty()) {
            nombreCompleto += " " + cliente.getApellidoMaterno();
        }
        return nombreCompleto;
    }

}
