package com.clientes.api_clientes.controlador;

import com.clientes.api_clientes.constantes.TipoDispositivo;
import com.clientes.api_clientes.dto.ClienteListadoDTO;
import com.clientes.api_clientes.dto.ClienteRequestDTO;
import com.clientes.api_clientes.dto.ClienteResponseDTO;
import com.clientes.api_clientes.servicio.AnaliticaServicio;
import com.clientes.api_clientes.servicio.ClienteServicio;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClienteControlador.class)
class ClienteControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteServicio clienteServicio;

    @MockBean
    private AnaliticaServicio analiticaServicio;

    private static final String TRACEPARENT = "00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01";

    @Test
    @DisplayName("POST /clientes -> 200 y respuesta esperada")
    void crearCliente_ok() throws Exception {
        ClienteRequestDTO req = new ClienteRequestDTO();
        req.setNombre("Kori");
        req.setApellidoPaterno("Peña");
        req.setApellidoMaterno("Ortega");

        ClienteResponseDTO resp = new ClienteResponseDTO();
        resp.setId("abc123");
        resp.setNombre(req.getNombre());
        resp.setApellidoPaterno(req.getApellidoPaterno());
        resp.setApellidoMaterno(req.getApellidoMaterno());
        resp.setFechaCreacion(LocalDateTime.now());
        resp.setActivo(true);

        when(clienteServicio.crearCliente(any(ClienteRequestDTO.class))).thenReturn(resp);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("consumerId", "app-1")
                        .header("traceparent", TRACEPARENT)
                        .header("deviceType", "IOS")
                        .header("deviceId", "dev-001")
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("abc123")))
                .andExpect(jsonPath("$.nombre", is("Kori")))
                .andExpect(jsonPath("$.apellidoPaterno", is("Peña")))
                .andExpect(jsonPath("$.apellidoMaterno", is("Ortega")))
                .andExpect(jsonPath("$.activo", is(true)));
    }

    @Test
    @DisplayName("GET /clientes -> 200 y lista de clientes")
    void listarClientes_ok() throws Exception {
        ClienteListadoDTO dto = new ClienteListadoDTO();
        dto.setId("1");
        dto.setNombreCompleto("Kori Peña Ortega");

        when(clienteServicio.listarClientes()).thenReturn(List.of(dto));
        doNothing().when(analiticaServicio).logListadoClienteAsync(any(), any(), any(TipoDispositivo.class), any(), any(), any());

        mockMvc.perform(get("/clientes")
                        .header("consumerId", "app-1")
                        .header("traceparent", TRACEPARENT)
                        .header("deviceType", "IOS")
                        .header("deviceId", "dev-001"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].nombreCompleto", is("Kori Peña Ortega")));
    }
}
