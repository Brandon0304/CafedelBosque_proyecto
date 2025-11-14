package com.restaurante.patrones.memento;

import com.restaurante.gestionPedidos.model.Pedido;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Patrón Memento - Guarda y restaura historial de pedidos
 */
@Component
public class HistorialPedidos {
    private final List<PedidoMemento> historial = new ArrayList<>();

    public void guardarEstado(Pedido pedido) {
        historial.add(new PedidoMemento(pedido));
        System.out.println("💾 Estado del pedido #" + pedido.getId() + " guardado en historial");
    }

    public List<PedidoMemento> obtenerHistorial() {
        return new ArrayList<>(historial);
    }

    public void mostrarHistorial() {
        System.out.println("\n=== HISTORIAL DE PEDIDOS ===");
        if (historial.isEmpty()) {
            System.out.println("No hay pedidos en el historial");
        } else {
            historial.forEach(m -> System.out.println("  📋 " + m));
        }
        System.out.println("Total de pedidos: " + historial.size() + "\n");
    }

    public PedidoMemento obtenerPorId(Long id) {
        return historial.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}

