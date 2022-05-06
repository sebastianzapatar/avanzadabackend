package com.backend.controllers;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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

import com.backend.model.Producto;
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
	@PostMapping("/productos/upload")
	public ResponseEntity<?> subir(@RequestParam("archivo")
	MultipartFile archivo, @RequestParam("id") Long id){
		Producto actual=productoService.findById(id);
		Map<String, Object> response=new HashMap<>();
		
		if(!archivo.isEmpty()) {
			String nombre=UUID.randomUUID()+"_"+
		archivo.getOriginalFilename().replace(" ","");
			java.nio.file.Path rutaArchivo=Paths.get("uploads")
					.resolve(nombre).toAbsolutePath();
			System.out.println(nombre);
			try {
				Files.copy(archivo.getInputStream(), rutaArchivo);
			}
			catch(IOException e) {
				response.put("Mensaje", "Error al subir la imagen");
				return new ResponseEntity<Map<String,Object>>
				(response,HttpStatus.INTERNAL_SERVER_ERROR);
			};
			String imagenAnterior=actual.getImagen();
			if(imagenAnterior!=null && imagenAnterior.length()>0) {
				java.nio.file.Path rutaImagen=Paths.get("uploads")
						.resolve(imagenAnterior).toAbsolutePath();
				File archivoAnterior=rutaImagen.toFile();
				archivoAnterior.delete();
			}
			actual.setImagen(nombre);
			productoService.save(actual);
			response.put("Producto", actual);
			response.put("Mensaje", "Se subio la imagen");
		}
		return new ResponseEntity<Map<String,Object>>
		(response,HttpStatus.CREATED);
	}
	@GetMapping("/productos/upload/img/{nombrefoto:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String
			nombrefoto){
		System.out.println(nombrefoto);
		java.nio.file.Path rutaImagen=Paths.get("uploads")
				.resolve(nombrefoto).toAbsolutePath();
		Resource recurso=null;
		try {
			recurso=new UrlResource(rutaImagen.toUri());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		HttpHeaders cabecera=new HttpHeaders();
		cabecera.add(HttpHeaders.CONTENT_DISPOSITION,
				"attachment;filename\""+recurso.getFilename());
		return new ResponseEntity<Resource>(recurso,cabecera,
				HttpStatus.OK);
	}
}
