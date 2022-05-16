package com.backend.services;

import java.util.List;

import com.backend.model.Cliente;

public interface IClienteService {
	public List<Cliente> findAll();
	public Cliente save(Cliente e);
	public Cliente findById(Long id);
}
