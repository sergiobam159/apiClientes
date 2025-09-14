package com.clientes.entidad;

import com.azure.spring.data.cosmos.core.mapping.Container;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Container(containerName = "clientes")
public class Cliente {

    @Id
    @GeneratedValue
    private String id;

    @NotNull
    @NotBlank
    private String nombre;

    @NotNull
    @NotBlank
    private String apellidoPaterno;

    @NotNull
    private String apellidoMaterno;


    private LocalDateTime fechaCreacion;


    private boolean activo;


}
