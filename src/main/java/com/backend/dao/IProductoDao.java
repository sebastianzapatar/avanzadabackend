package com.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.model.Producto;

public interface IProductoDao extends JpaRepository<Producto, Long> {

}
