package com.backend.services;

import java.util.List;

import com.backend.model.Factura;

public interface IFacturaService {
	public List<Factura> findAll();
	public Factura save(Factura e);
	public Factura findById(Long id);
}
