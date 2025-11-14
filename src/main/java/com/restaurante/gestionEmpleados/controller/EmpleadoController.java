package com.restaurante.gestionEmpleados.controller;

import com.restaurante.gestionEmpleados.model.Empleado;
import com.restaurante.gestionEmpleados.service.EmpleadoService;
import com.restaurante.gestionPedidos.model.Pedido;
import com.restaurante.gestionPedidos.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/empleados")
public class EmpleadoController {

    private final EmpleadoService service;
    private final PedidoService pedidoService;

    public EmpleadoController(EmpleadoService service, PedidoService pedidoService) {
        this.service = service;
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Empleado> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> obtener(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Empleado crear(@RequestBody Empleado e) {
        return service.crear(e);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizar(@PathVariable Long id,
                                               @RequestBody Empleado datos) {
        return service.actualizar(id, datos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // Chain of Responsibility - Manejar pedido por rol
    @PostMapping("/manejar-pedido")
    public ResponseEntity<String> manejarPedidoPorRol(@RequestBody Map<String, Object> body) {
        Long idPedido = Long.parseLong(body.get("idPedido").toString());
        String rol = body.getOrDefault("rol", "MESERO").toString();
        
        // Obtener pedido desde el servicio de pedidos
        Pedido pedido = pedidoService.buscarPorId(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        // Chain of Responsibility - Manejar según rol
        service.manejarPedidoPorRol(pedido, rol);
        
        return ResponseEntity.ok("Pedido #" + idPedido + " manejado por rol: " + rol + " (ver logs)");
    }

    // Chain of Responsibility - Manejar pedido por ID de empleado
    @PostMapping("/{idEmpleado}/manejar-pedido/{idPedido}")
    public ResponseEntity<String> manejarPedidoPorEmpleado(@PathVariable Long idEmpleado,
                                                           @PathVariable Long idPedido) {
        Pedido pedido = pedidoService.buscarPorId(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        service.manejarPedidoPorEmpleado(idEmpleado, pedido);
        
        return ResponseEntity.ok("Pedido #" + idPedido + " manejado por empleado #" + idEmpleado + " (ver logs)");
    }

    // Buscar empleados por rol
    @GetMapping("/rol/{rol}")
    public List<Empleado> buscarPorRol(@PathVariable String rol) {
        return service.buscarPorRol(rol);
    }

}

