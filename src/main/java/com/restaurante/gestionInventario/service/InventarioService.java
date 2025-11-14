package com.restaurante.gestionInventario.service;

import com.restaurante.gestionInventario.model.InventarioItem;
import com.restaurante.gestionInventario.repository.InventarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    private final InventarioRepository repo;

    public InventarioService(InventarioRepository repo) {
        this.repo = repo;
    }

    public List<InventarioItem> listarTodos() {
        return repo.findAll();
    }

    public Optional<InventarioItem> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public InventarioItem crear(InventarioItem i) {
        i.setId(null);
        return repo.save(i);
    }

    public Optional<InventarioItem> actualizar(Long id, InventarioItem datos) {
        return repo.findById(id).map(i -> {
            i.setNombreInsumo(datos.getNombreInsumo());
            i.setUnidad(datos.getUnidad());
            i.setStockActual(datos.getStockActual());
            i.setStockMinimo(datos.getStockMinimo());
            return repo.save(i);
        });
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    public List<InventarioItem> bajoStock() {
        return repo.findByStockActualLessThanEqual(5); // umbral simple
    }

}

