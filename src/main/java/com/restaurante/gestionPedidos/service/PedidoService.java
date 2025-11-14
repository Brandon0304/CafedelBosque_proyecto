package com.restaurante.gestionPedidos.service;

import com.restaurante.gestionPedidos.model.DetallePedido;
import com.restaurante.gestionPedidos.model.Pedido;
import com.restaurante.gestionPedidos.repository.PedidoRepository;
import com.restaurante.gestionProductos.model.Producto;
import com.restaurante.gestionProductos.repository.ProductoRepository;
import com.restaurante.patrones.command.ComandoCrearPedido;
import com.restaurante.patrones.command.ComandoEnviarACocinero;
import com.restaurante.patrones.command.ComandoPedido;
import com.restaurante.patrones.mediator.MediadorRestaurante;
import com.restaurante.patrones.memento.HistorialPedidos;
import com.restaurante.patrones.observer.SujetoPedido;
import com.restaurante.patrones.observer.ObservadorMesero;
import com.restaurante.patrones.state.EstadoCocinando;
import com.restaurante.patrones.state.EstadoTerminado;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service refactorizado con patrones: State, Command, Observer, Mediator, Memento
 */
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepo;
    private final ProductoRepository productoRepo;
    
    // Mediator - Intermediario entre cliente y cocinero
    private final MediadorRestaurante mediador;
    
    // Observer - Sujeto observable para notificaciones
    private final SujetoPedido sujetoPedido;
    
    // Memento - Historial de pedidos
    private final HistorialPedidos historial;

    public PedidoService(PedidoRepository pedidoRepo, 
                        ProductoRepository productoRepo,
                        MediadorRestaurante mediador,
                        HistorialPedidos historial) {
        this.pedidoRepo = pedidoRepo;
        this.productoRepo = productoRepo;
        this.mediador = mediador;
        this.historial = historial;
        this.sujetoPedido = new SujetoPedido();
        
        // Registrar meseros por defecto para notificaciones (Observer)
        registrarMeserosParaNotificaciones();
    }

    private void registrarMeserosParaNotificaciones() {
        // Se pueden registrar meseros desde la base de datos
        sujetoPedido.agregarObservador(new ObservadorMesero("Mesero Principal"));
    }

    public List<Pedido> listarTodos() {
        return pedidoRepo.findAll();
    }

    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepo.findById(id);
    }

    // Command Pattern - Crear pedido usando comando
    public Pedido crearPedido(String nombreCliente) {
        ComandoPedido comando = new ComandoCrearPedido(pedidoRepo, nombreCliente);
        comando.ejecutar();
        Pedido pedido = comando.obtenerPedido();
        
        // Memento - Guardar estado inicial
        historial.guardarEstado(pedido);
        
        // Observer - Notificar estado inicial
        sujetoPedido.setPedido(pedido);
        sujetoPedido.notificarObservadores(pedido.obtenerEstadoActual());
        
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
            historial.guardarEstado(guardado); // Memento
            
            return guardado;
        });
    }

    // State Pattern - Cambiar estado del pedido a "cocinando"
    public Optional<Pedido> enviarACocinero(Long idPedido) {
        return pedidoRepo.findById(idPedido).map(pedido -> {
            // State - Cambiar a estado cocinando
            pedido.cambiarEstado(new EstadoCocinando());
            
            // Command - Enviar al cocinero usando comando
            ComandoPedido comando = new ComandoEnviarACocinero(pedido, mediador);
            comando.ejecutar();
            
            Pedido guardado = pedidoRepo.save(pedido);
            historial.guardarEstado(guardado); // Memento
            
            // Observer - Notificar cambio de estado
            sujetoPedido.setPedido(guardado);
            sujetoPedido.notificarObservadores(guardado.obtenerEstadoActual());
            
            return guardado;
        });
    }

    // State Pattern - Cambiar estado del pedido a "terminado"
    public Optional<Pedido> terminarPedido(Long idPedido) {
        return pedidoRepo.findById(idPedido).map(pedido -> {
            // State - Cambiar a estado terminado
            pedido.cambiarEstado(new EstadoTerminado());
            
            // Mediator - Notificar que el pedido está listo
            mediador.pedidoListo(pedido);
            
            Pedido guardado = pedidoRepo.save(pedido);
            historial.guardarEstado(guardado); // Memento
            
            // Observer - Notificar cambio de estado
            sujetoPedido.setPedido(guardado);
            sujetoPedido.notificarObservadores(guardado.obtenerEstadoActual());
            
            return guardado;
        });
    }

    public Optional<Pedido> marcarPagado(Long idPedido) {
        return pedidoRepo.findById(idPedido).map(p -> {
            p.setPagado(true);
            Pedido guardado = pedidoRepo.save(p);
            historial.guardarEstado(guardado); // Memento
            return guardado;
        });
    }

    public List<Pedido> pedidosEntre(LocalDateTime desde, LocalDateTime hasta) {
        return pedidoRepo.findByFechaHoraBetween(desde, hasta);
    }

    // Memento - Obtener historial
    public void mostrarHistorial() {
        historial.mostrarHistorial();
    }

    // Observer - Registrar nuevo mesero
    public void registrarMesero(String nombreMesero) {
        sujetoPedido.agregarObservador(new ObservadorMesero(nombreMesero));
        mediador.registrarMesero(nombreMesero);
    }

}

