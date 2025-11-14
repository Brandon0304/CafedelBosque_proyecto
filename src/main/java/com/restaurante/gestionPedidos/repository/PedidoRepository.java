package com.restaurante.gestionPedidos.repository;

import com.restaurante.gestionPedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByFechaHoraBetween(LocalDateTime desde, LocalDateTime hasta);

}

