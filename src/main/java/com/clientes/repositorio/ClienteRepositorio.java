package com.clientes.repositorio;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.clientes.entidad.Cliente;

public interface ClienteRepositorio extends CosmosRepository<Cliente, String> {

}
