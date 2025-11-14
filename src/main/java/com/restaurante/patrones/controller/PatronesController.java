package com.restaurante.patrones.controller;

import com.restaurante.gestionPedidos.model.Pedido;
import com.restaurante.patrones.service.GestorPatronesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller para demostrar los patrones de diseño
 */
@RestController
@RequestMapping("/api/v1/patrones")
public class PatronesController {
    
    private final GestorPatronesService gestorPatrones;

    public PatronesController(GestorPatronesService gestorPatrones) {
        this.gestorPatrones = gestorPatrones;
    }

    /**
     * Crear pedido usando Command Pattern
     */
    @PostMapping("/pedido/crear")
    public ResponseEntity<Pedido> crearPedidoConCommand(@RequestBody Map<String, String> body) {
        String nombreCliente = body.getOrDefault("nombreCliente", "Cliente");
        Pedido pedido = gestorPatrones.crearPedidoConCommand(nombreCliente);
        return ResponseEntity.ok(pedido);
    }

    /**
     * Enviar pedido al cocinero usando Command + Mediator
     */
    @PostMapping("/pedido/{id}/enviar-cocinero")
    public ResponseEntity<String> enviarACocinero(@PathVariable Long id) {
        gestorPatrones.enviarPedidoACocinero(id);
        return ResponseEntity.ok("Pedido enviado al cocinero");
    }

    /**
     * Terminar pedido usando State Pattern
     */
    @PostMapping("/pedido/{id}/terminar")
    public ResponseEntity<String> terminarPedido(@PathVariable Long id) {
        gestorPatrones.terminarPedido(id);
        return ResponseEntity.ok("Pedido terminado");
    }

    /**
     * Manejar pedido según rol usando Chain of Responsibility
     */
    @PostMapping("/pedido/{id}/manejar")
    public ResponseEntity<String> manejarPorRol(@PathVariable Long id, 
                                                  @RequestBody Map<String, String> body) {
        String rol = body.getOrDefault("rol", "MESERO");
        gestorPatrones.manejarPedidoPorRol(id, rol);
        return ResponseEntity.ok("Pedido manejado por rol: " + rol);
    }

    /**
     * Mostrar menú usando Facade
     */
    @GetMapping("/menu")
    public ResponseEntity<String> mostrarMenu() {
        gestorPatrones.mostrarMenu();
        return ResponseEntity.ok("Menú mostrado (ver logs)");
    }

    /**
     * Ver historial usando Memento
     */
    @GetMapping("/historial")
    public ResponseEntity<String> verHistorial() {
        gestorPatrones.verHistorial();
        return ResponseEntity.ok("Historial mostrado (ver logs)");
    }

    /**
     * Registrar mesero para notificaciones (Observer)
     */
    @PostMapping("/registrar-mesero")
    public ResponseEntity<String> registrarMesero(@RequestBody Map<String, String> body) {
        String nombre = body.getOrDefault("nombre", "Mesero");
        gestorPatrones.registrarMeseroParaNotificaciones(nombre);
        return ResponseEntity.ok("Mesero " + nombre + " registrado para notificaciones");
    }
}

