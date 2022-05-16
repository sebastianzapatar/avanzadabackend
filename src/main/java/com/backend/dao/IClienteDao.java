package com.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.model.Cliente;

public interface IClienteDao extends JpaRepository<Cliente, Long> {

}
