package com.backend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.dao.IFacturaDao;
import com.backend.model.Factura;

@Service
public class FacturaService implements IFacturaService {

	@Autowired
	private IFacturaDao facturaDao;
	@Override
	public List<Factura> findAll() {
		// TODO Auto-generated method stub
		return facturaDao.findAll();
	}

	@Override
	public Factura save(Factura e) {
		// TODO Auto-generated method stub
		return facturaDao.save(e);
	}

	@Override
	public Factura findById(Long id) {
		// TODO Auto-generated method stub
		return facturaDao.getById(id);
	}

}
