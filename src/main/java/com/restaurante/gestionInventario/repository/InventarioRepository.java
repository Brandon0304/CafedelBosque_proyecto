package com.restaurante.gestionInventario.repository;

import com.restaurante.gestionInventario.model.InventarioItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventarioRepository extends JpaRepository<InventarioItem, Long> {

    List<InventarioItem> findByStockActualLessThanEqual(int stock);

}

