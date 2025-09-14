package com.clientes.controlador;

import com.azure.core.annotation.Post;
import com.clientes.dto.ClienteListadoDTO;
import com.clientes.dto.ClienteRequestDTO;
import com.clientes.dto.ClienteResponseDTO;
import com.clientes.servicio.ClienteServicio;
import jakarta.validation.Valid;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
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
