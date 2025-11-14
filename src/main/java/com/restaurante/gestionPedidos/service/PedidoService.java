package com.restaurante.gestionPedidos.service;

import com.restaurante.gestionPedidos.model.DetallePedido;
import com.restaurante.gestionPedidos.model.Pedido;
import com.restaurante.gestionPedidos.repository.PedidoRepository;
import com.restaurante.gestionProductos.model.Producto;
import com.restaurante.gestionProductos.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service refactorizado con patrones integrados: State, Command, Observer, Mediator, Memento
 */
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepo;
    private final ProductoRepository productoRepo;
    
    // ========== PATRÓN OBSERVER ==========
    // Observer Pattern - Lista de observadores (meseros) para notificaciones automáticas
    private final List<MeseroObserver> observadoresMeseros = new ArrayList<>();
    
    // ========== PATRÓN MEMENTO ==========
    // Memento Pattern - Historial de estados de pedidos para restaurar estados anteriores
    private final List<PedidoMemento> historial = new ArrayList<>();
    
    // ========== PATRÓN MEDIATOR ==========
    // Mediator Pattern - Intermediario para coordinar comunicación entre componentes
    private final MediadorRestaurante mediador = new MediadorRestaurante();

    public PedidoService(PedidoRepository pedidoRepo, 
                        ProductoRepository productoRepo) {
        this.pedidoRepo = pedidoRepo;
        this.productoRepo = productoRepo;
        
        // Registrar mesero por defecto (Observer)
        registrarMesero("Mesero Principal");
    }

    // Observer Pattern - Interfaz para observadores
    @FunctionalInterface
    private interface MeseroObserver {
        void notificar(Pedido pedido, String estado);
    }

    // Observer Pattern - Registrar mesero para notificaciones
    public void registrarMesero(String nombreMesero) {
        observadoresMeseros.add((pedido, estado) -> 
            System.out.println("🔔 NOTIFICACIÓN para Mesero " + nombreMesero + 
                             ": El pedido #" + pedido.getId() + " cambió a estado: " + estado)
        );
    }

    // Observer Pattern - Notificar a todos los observadores
    private void notificarObservadores(Pedido pedido, String estado) {
        observadoresMeseros.forEach(obs -> obs.notificar(pedido, estado));
    }

    // Memento Pattern - Clase interna para guardar snapshot del estado del pedido
    private static class PedidoMemento {
        private final Long id;
        private final String cliente;
        private final LocalDateTime fecha;
        private final Pedido.EstadoPedido estado;
        private final double total;

        public PedidoMemento(Pedido pedido) {
            this.id = pedido.getId();
            this.cliente = pedido.getNombreCliente();
            this.fecha = pedido.getFechaHora();
            this.estado = pedido.getEstado();
            this.total = pedido.getTotal();
        }

        @Override
        public String toString() {
            return String.format("PedidoMemento{id=%d, cliente='%s', estado=%s, total=$%.2f}",
                    id, cliente, estado, total);
        }
    }

    // Mediator Pattern - Clase interna que centraliza la comunicación entre cocinero y mesero
    private static class MediadorRestaurante {
        public void enviarPedidoACocinero(Pedido pedido) {
            System.out.println("📋 Mediador: Recibiendo pedido #" + pedido.getId() + " del cliente");
            System.out.println("👨‍🍳 Mediador: Enviando pedido al cocinero...");
        }

        public void pedidoListo(Pedido pedido) {
            System.out.println("✅ Mediador: Pedido #" + pedido.getId() + " terminado, notificando mesero...");
        }
    }

    // ========== PATRÓN COMMAND ==========
    // Command Pattern - Encapsula la operación de crear pedido como un comando
    private Pedido ejecutarComandoCrear(String nombreCliente) {
        Pedido pedido = new Pedido();
        pedido.setNombreCliente(nombreCliente);
        return pedidoRepo.save(pedido);
    }

    // Command Pattern - Encapsula la operación de enviar pedido al cocinero como un comando
    private void ejecutarComandoEnviarACocinero(Pedido pedido) {
        mediador.enviarPedidoACocinero(pedido);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepo.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepo.findById(id);
    }

    // Command Pattern - Crear pedido (comando integrado directamente)
    public Pedido crearPedido(String nombreCliente) {
        // Command Pattern - Ejecutar comando de creación
        Pedido pedido = ejecutarComandoCrear(nombreCliente);
        
        // Memento Pattern - Guardar estado inicial
        historial.add(new PedidoMemento(pedido));
        
        // Observer Pattern - Notificar estado inicial
        notificarObservadores(pedido, pedido.obtenerEstadoActual());
        
        return pedido;
    }

    public Optional<Pedido> agregarProducto(Long idPedido, Long idProducto, int cantidad) {
        return pedidoRepo.findById(idPedido).map(pedido -> {
            Producto producto = productoRepo.findById(idProducto)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            DetallePedido detalle = new DetallePedido();
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecio().doubleValue());
            detalle.setProducto(producto);

            pedido.agregarDetalle(detalle);

            Pedido guardado = pedidoRepo.save(pedido);
            
            // Memento Pattern - Guardar estado después de agregar producto
            historial.add(new PedidoMemento(guardado));
            
            return guardado;
        });
    }

    // ========== PATRÓN STATE ==========
    // State Pattern - Cambiar estado del pedido: RECIBIDO -> COCINANDO
    public Optional<Pedido> enviarACocinero(Long idPedido) {
        return pedidoRepo.findById(idPedido).map(pedido -> {
            // State Pattern - Validar transición de estado antes de cambiar
            if (!pedido.puedeCocinar()) {
                throw new RuntimeException("El pedido no puede pasar a cocinando desde el estado: " + pedido.getEstado());
            }
            
            // State Pattern - Cambiar a estado COCINANDO
            pedido.cambiarEstado(Pedido.EstadoPedido.COCINANDO);
            
            // Command Pattern - Ejecutar comando de envío al cocinero
            ejecutarComandoEnviarACocinero(pedido);
            
            Pedido guardado = pedidoRepo.save(pedido);
            
            // Memento Pattern - Guardar snapshot del estado actual
            historial.add(new PedidoMemento(guardado));
            
            // Observer Pattern - Notificar cambio de estado a todos los observadores (meseros)
            notificarObservadores(guardado, guardado.obtenerEstadoActual());
            
            return guardado;
        });
    }

    // State Pattern - Cambiar estado del pedido: COCINANDO -> TERMINADO
    public Optional<Pedido> terminarPedido(Long idPedido) {
        return pedidoRepo.findById(idPedido).map(pedido -> {
            // State Pattern - Validar transición de estado antes de cambiar
            if (!pedido.puedeTerminar()) {
                throw new RuntimeException("El pedido no puede terminar desde el estado: " + pedido.getEstado());
            }
            
            // State Pattern - Cambiar a estado TERMINADO
            pedido.cambiarEstado(Pedido.EstadoPedido.TERMINADO);
            
            // Mediator Pattern - Notificar a través del mediador que el pedido está listo
            mediador.pedidoListo(pedido);
            
            Pedido guardado = pedidoRepo.save(pedido);
            
            // Memento Pattern - Guardar snapshot del estado actual
            historial.add(new PedidoMemento(guardado));
            
            // Observer Pattern - Notificar cambio de estado a todos los observadores (meseros)
            notificarObservadores(guardado, guardado.obtenerEstadoActual());
            
            return guardado;
        });
    }

    public Optional<Pedido> marcarPagado(Long idPedido) {
        return pedidoRepo.findById(idPedido).map(p -> {
            p.setPagado(true);
            Pedido guardado = pedidoRepo.save(p);
            
            // Memento Pattern - Guardar estado
            historial.add(new PedidoMemento(guardado));
            
            return guardado;
        });
    }

    public List<Pedido> pedidosEntre(LocalDateTime desde, LocalDateTime hasta) {
        return pedidoRepo.findByFechaHoraBetween(desde, hasta);
    }

    // Memento Pattern - Obtener historial
    public void mostrarHistorial() {
        System.out.println("\n=== HISTORIAL DE PEDIDOS (Memento) ===");
        if (historial.isEmpty()) {
            System.out.println("No hay pedidos en el historial");
        } else {
            historial.forEach(m -> System.out.println("  📋 " + m));
        }
        System.out.println("Total de pedidos guardados: " + historial.size() + "\n");
    }

}

