package com.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.model.Factura;

public interface IFacturaDao extends JpaRepository<Factura, Long> {

}
