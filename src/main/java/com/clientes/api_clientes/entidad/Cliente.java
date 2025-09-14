package com.clientes.api_clientes.entidad;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "clientes")
public class Cliente {

    @Id
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
