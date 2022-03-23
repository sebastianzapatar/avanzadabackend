package com.backend.services;

import java.util.List;

import com.backend.model.Producto;

public interface IProductoService {
	public List<Producto> findAll();
	public Producto save(Producto producto);
	public void delete(Long id);
	public Producto findById(Long id);
}
