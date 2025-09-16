package com.clientes.api_clientes.servicio;

import com.clientes.api_clientes.dto.ClienteListadoDTO;
import com.clientes.api_clientes.dto.ClienteRequestDTO;
import com.clientes.api_clientes.dto.ClienteResponseDTO;
import com.clientes.api_clientes.entidad.Cliente;
import com.clientes.api_clientes.repositorio.ClienteRepositorio;
import com.clientes.api_clientes.servicio.ClienteServicio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServicioTest {

    @Mock
    private ClienteRepositorio clienteRepositorio;

    @InjectMocks
    private ClienteServicio clienteServicio;

    @Test
    void testCrearCliente() {

        ClienteRequestDTO request = new ClienteRequestDTO();
        request.setNombre("Juan");
        request.setApellidoPaterno("Perez");
        request.setApellidoMaterno("Lopez");

        Cliente saved = new Cliente();
        saved.setId("123");
        saved.setNombre("Juan");
        saved.setApellidoPaterno("Perez");
        saved.setApellidoMaterno("Lopez");
        LocalDateTime now = LocalDateTime.now();
        saved.setFechaCreacion(now);
        saved.setActivo(true);

        when(clienteRepositorio.save(any(Cliente.class))).thenReturn(saved);


        ClienteResponseDTO response = clienteServicio.crearCliente(request);


        assertNotNull(response);
        assertEquals("123", response.getId());
        assertEquals("Juan", response.getNombre());
        assertEquals("Perez", response.getApellidoPaterno());
        assertEquals("Lopez", response.getApellidoMaterno());
        assertTrue(response.isActivo());
        assertNotNull(response.getFechaCreacion());

       // verify(clienteRepositorio, times(1)).save(any(Cliente.class));
    }

    @Test
    void testListarClientes() {

        Cliente c = new Cliente();
        Cliente c2 = new Cliente();
        c.setId("1");
        c.setNombre("Ana");
        c.setApellidoPaterno("Gomez");
        c.setApellidoMaterno("Diaz");
        c2.setId("2");
        c2.setNombre("Pancho");
        c2.setApellidoPaterno("Villa");
        List<Cliente> lista = List.of(c, c2);
        when(clienteRepositorio.findAll()).thenReturn(lista);


        List<ClienteListadoDTO> result = clienteServicio.listarClientes();


        assertEquals(1, result.size());
        ClienteListadoDTO dto = result.get(0);
        assertEquals("1", dto.getId());
        assertEquals("Ana Gomez Diaz" , dto.getNombreCompleto());
      //  verify(clienteRepositorio, times(1)).findAll();
    }


}

