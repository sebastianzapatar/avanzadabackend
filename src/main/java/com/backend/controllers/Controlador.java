	package com.backend.controllers;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.backend.model.Cliente;
import com.backend.model.Factura;
import com.backend.model.Producto;
import com.backend.services.IClienteService;
import com.backend.services.IFacturaService;
import com.backend.services.IProductoService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin(origins = "*", 
methods= {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,
		RequestMethod.DELETE})
@RestController
@RequestMapping("/api")
public class Controlador {
	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private IClienteService clienteService;
	
	@Autowired
	private IFacturaService facturaService;
	
	@GetMapping("/facturas")
	public List<Factura> facturas(){
		return facturaService.findAll();
	}
	
	@PostMapping("/facturas")
	@ResponseStatus(HttpStatus.CREATED)
	public Factura createFactura(@RequestBody Factura factura) {
		return facturaService.save(factura);
	}
	
	@GetMapping("/facturas/{id}")
	public Factura buscarfactura(@PathVariable Long id) {
		return facturaService.findById(id);
	}
	@GetMapping("/clientes")
	public List<Cliente> clientes(){
		return clienteService.findAll();
	}
	@PostMapping("/clientes")
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente createCliente(@RequestBody Cliente cliente) {
		return clienteService.save(cliente);
	}
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
	@PostMapping("productos/upload")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto upload(@RequestParam("archivo") MultipartFile archivo,
			@RequestParam("id") Long id) {
		Producto actual=productoService.findById(id);
		if(!archivo.isEmpty()) {
			
			String nombre=UUID.randomUUID()+" "+archivo.getOriginalFilename().
					replace(" ", "");
			Path rutaArchivo=
					Paths.get("uploads").resolve(nombre).toAbsolutePath();
			try {
				Files.copy(archivo.getInputStream(), rutaArchivo);
			}
			catch(IOException e) {	
				return new Producto();
			}
			String fotoAnterior=actual.getImagen();
			if(fotoAnterior!=null && fotoAnterior.length()>0) {
				java.nio.file.Path rutaAnterior=
						Paths.get("uploads").resolve(fotoAnterior).toAbsolutePath();
				File archivoAnterior=rutaAnterior.toFile();
				if(archivoAnterior.canRead() && archivoAnterior.exists()) {
					archivoAnterior.delete();
				}
			}
			actual.setImagen(nombre);
			productoService.save(actual);
		}
		return actual;
	}
	@GetMapping("productos/upload/img/{nombrefoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String nombrefoto){
		Resource recurso=null;
		java.nio.file.Path rutaFoto=
				Paths.get("uploads").resolve(nombrefoto).toAbsolutePath();
		try {
			recurso=new UrlResource(rutaFoto.toUri());
		}
		catch(Exception e) {
			
		}
		HttpHeaders cabecera=new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=\""+
		recurso.getFilename());
		return  new ResponseEntity<Resource>(recurso,cabecera,HttpStatus.OK);
	}
	
}

