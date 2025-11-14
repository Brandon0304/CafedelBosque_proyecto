package com.restaurante.gestionPedidos.controller;

import com.restaurante.gestionPedidos.model.Pedido;
import com.restaurante.gestionPedidos.service.PedidoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pedido> listar() {
        return service.listarTodos();
    }

    @PostMapping
    public Pedido crear(@RequestBody Map<String, String> body) {
        String nombreCliente = body.getOrDefault("nombreCliente", "Mostrador");
        return service.crearPedido(nombreCliente);
    }

    @PostMapping("/{idPedido}/agregar-producto")
    public ResponseEntity<Pedido> agregarProducto(@PathVariable Long idPedido,
                                                  @RequestBody Map<String, String> body) {
        Long idProducto = Long.parseLong(body.get("idProducto"));
        int cantidad = Integer.parseInt(body.get("cantidad"));

        return service.agregarProducto(idPedido, idProducto, cantidad)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{idPedido}/pagar")
    public ResponseEntity<Pedido> pagar(@PathVariable Long idPedido) {
        return service.marcarPagado(idPedido)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/entre-fechas")
    public List<Pedido> entreFechas(
            @RequestParam("desde")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam("hasta")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return service.pedidosEntre(desde, hasta);
    }

    // Patrones de Diseño - Nuevos endpoints

    // State + Command + Mediator - Enviar pedido al cocinero
    @PostMapping("/{idPedido}/enviar-cocinero")
    public ResponseEntity<Pedido> enviarACocinero(@PathVariable Long idPedido) {
        return service.enviarACocinero(idPedido)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // State - Terminar pedido
    @PostMapping("/{idPedido}/terminar")
    public ResponseEntity<Pedido> terminar(@PathVariable Long idPedido) {
        return service.terminarPedido(idPedido)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Memento - Ver historial
    @GetMapping("/historial")
    public ResponseEntity<String> verHistorial() {
        service.mostrarHistorial();
        return ResponseEntity.ok("Historial mostrado (ver logs del servidor)");
    }

    // Observer - Registrar mesero para notificaciones
    @PostMapping("/registrar-mesero")
    public ResponseEntity<String> registrarMesero(@RequestBody Map<String, String> body) {
        String nombre = body.getOrDefault("nombre", "Mesero");
        service.registrarMesero(nombre);
        return ResponseEntity.ok("Mesero " + nombre + " registrado para notificaciones");
    }

}

