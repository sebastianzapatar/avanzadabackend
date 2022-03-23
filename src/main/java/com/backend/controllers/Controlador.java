package com.backend.controllers;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backend.model.Producto;
import com.backend.services.IProductoService;


@RestController
@RequestMapping("/api")
public class Controlador {
	@Autowired
	private IProductoService productoService;
	@GetMapping("/productos")
	public List<Producto> productos(){
		return productoService.findAll();
	}
	@GetMapping("/productos/{id}")
	public Producto buscar(@PathVariable Long id) {
		return productoService.findById(id);
	}
	@PostMapping("/productos")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto create(@RequestBody Producto producto) {
		return productoService.save(producto);
	}
	@DeleteMapping("/productos/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		productoService.delete(id);
	}
	@PutMapping("/productos/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto update(@PathVariable Long id,
			@RequestBody Producto producto) {
		Producto cambiar=productoService.findById(id);
		cambiar.setImagen(producto.getImagen());
		cambiar.setNombre(producto.getNombre());
		cambiar.setProovedor(producto.getProovedor());
		return productoService.save(cambiar);
	}
	
}
