package com.restaurante.gestionProductos.controller;

import com.restaurante.gestionProductos.model.Producto;
import com.restaurante.gestionProductos.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Producto> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/disponibles")
    public List<Producto> listarDisponibles() {
        return service.listarDisponibles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> crear(@RequestBody Producto p) {
        Producto creado = service.crear(p);
        return ResponseEntity.ok(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id,
                                               @RequestBody Producto datos) {
        return service.actualizar(id, datos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Patrones de Diseño - Nuevos endpoints

    // Facade + Builder - Mostrar menú completo
    @GetMapping("/menu")
    public ResponseEntity<String> mostrarMenu() {
        service.mostrarMenu();
        return ResponseEntity.ok("Menú mostrado (ver logs del servidor)");
    }

    // Abstract Factory - Crear bebida caliente
    @PostMapping("/factory/bebida-caliente")
    public ResponseEntity<Producto> crearBebidaCaliente() {
        return ResponseEntity.ok(service.crearBebidaCaliente());
    }

    // Abstract Factory - Crear bebida fría
    @PostMapping("/factory/bebida-fria")
    public ResponseEntity<Producto> crearBebidaFria() {
        return ResponseEntity.ok(service.crearBebidaFria());
    }

    // Abstract Factory - Crear plato caliente
    @PostMapping("/factory/plato-caliente")
    public ResponseEntity<Producto> crearPlatoCaliente() {
        return ResponseEntity.ok(service.crearPlatoCaliente());
    }

    // Abstract Factory - Crear plato frío
    @PostMapping("/factory/plato-frio")
    public ResponseEntity<Producto> crearPlatoFrio() {
        return ResponseEntity.ok(service.crearPlatoFrio());
    }

    // Decorator - Aplicar descuento
    @PostMapping("/{id}/descuento")
    public ResponseEntity<Producto> aplicarDescuento(@PathVariable Long id,
                                                      @RequestBody java.util.Map<String, Double> body) {
        double porcentaje = body.getOrDefault("porcentaje", 0.0);
        return ResponseEntity.ok(service.aplicarDescuento(id, porcentaje));
    }

    // Decorator - Agregar extra
    @PostMapping("/{id}/extra")
    public ResponseEntity<Producto> agregarExtra(@PathVariable Long id,
                                                  @RequestBody java.util.Map<String, String> body) {
        String extra = body.get("extra");
        java.math.BigDecimal precioExtra = new java.math.BigDecimal(body.get("precioExtra"));
        return ResponseEntity.ok(service.agregarExtra(id, extra, precioExtra));
    }

}

