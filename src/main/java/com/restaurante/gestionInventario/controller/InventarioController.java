package com.restaurante.gestionInventario.controller;

import com.restaurante.gestionInventario.model.InventarioItem;
import com.restaurante.gestionInventario.service.InventarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario")
public class InventarioController {

    private final InventarioService service;

    public InventarioController(InventarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<InventarioItem> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioItem> obtener(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public InventarioItem crear(@RequestBody InventarioItem i) {
        return service.crear(i);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioItem> actualizar(@PathVariable Long id,
                                                     @RequestBody InventarioItem datos) {
        return service.actualizar(id, datos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/bajo-stock")
    public List<InventarioItem> bajoStock() {
        return service.bajoStock();
    }

}

