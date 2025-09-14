package com.clientes.api_clientes.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.clientes.api_clientes.entidad.Cliente;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepositorio extends MongoRepository<Cliente, String> {

    List<Cliente> findAll();
}
