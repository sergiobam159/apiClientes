package com.clientes.repositorio;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.clientes.entidad.Cliente;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepositorio extends CosmosRepository<Cliente, String> {

    List<Cliente> findAll();
}
