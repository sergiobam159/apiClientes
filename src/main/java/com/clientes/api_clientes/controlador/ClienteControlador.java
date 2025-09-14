package com.clientes.api_clientes.controlador;

import com.clientes.api_clientes.dto.ClienteListadoDTO;
import com.clientes.api_clientes.dto.ClienteRequestDTO;
import com.clientes.api_clientes.dto.ClienteResponseDTO;
import com.clientes.api_clientes.servicio.ClienteServicio;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteControlador {
    @Autowired
    private ClienteServicio clienteServicio;

    @PostMapping
    public ClienteResponseDTO crearCliente (@Valid @RequestBody ClienteRequestDTO cliente){
        return clienteServicio.crearCliente(cliente);
    }

    @GetMapping
    public List<ClienteListadoDTO> listarclientes(){
            return clienteServicio.listarClientes();
    }

}
