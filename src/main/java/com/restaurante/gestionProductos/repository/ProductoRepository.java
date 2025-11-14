package com.restaurante.gestionProductos.repository;

import com.restaurante.gestionProductos.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByDisponibleTrue();

    List<Producto> findByCategoria(String categoria);

}

