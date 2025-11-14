package com.restaurante.gestionPedidos.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de Pedido con patrón State integrado directamente
 */
@Entity
@Table(name = "pedidos")
public class Pedido {

    // Patrón State - Estados integrados directamente
    public enum EstadoPedido {
        RECIBIDO("Pedido recibido, listo para procesar"),
        COCINANDO("Pedido en preparación"),
        TERMINADO("Pedido terminado y listo para servir");

        private final String descripcion;

        EstadoPedido(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCliente;  // o mesa
    private LocalDateTime fechaHora;
    private boolean pagado;
    
    // State Pattern - Estado del pedido integrado
    @Enumerated(EnumType.STRING)
    private EstadoPedido estado = EstadoPedido.RECIBIDO;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles = new ArrayList<>();

    public Pedido() {
        this.fechaHora = LocalDateTime.now();
        this.pagado = false;
        this.estado = EstadoPedido.RECIBIDO; // Estado inicial
    }

    public Pedido(Long id, String nombreCliente) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.fechaHora = LocalDateTime.now();
        this.pagado = false;
        this.estado = EstadoPedido.RECIBIDO; // Estado inicial
    }
    
    // State Pattern - Cambiar estado
    public void cambiarEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
    }
    
    public EstadoPedido getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }
    
    public String obtenerEstadoActual() {
        return estado != null ? estado.name() : "SIN_ESTADO";
    }
    
    // State Pattern - Validar transición de estado: RECIBIDO -> COCINANDO
    public boolean puedeCocinar() {
        return estado == EstadoPedido.RECIBIDO;
    }
    
    // State Pattern - Validar transición de estado: COCINANDO -> TERMINADO
    public boolean puedeTerminar() {
        return estado == EstadoPedido.COCINANDO;
    }
    
    // State Pattern - Verificar si el pedido está en estado final
    public boolean estaTerminado() {
        return estado == EstadoPedido.TERMINADO;
    }

    public Long getId() {
        return id;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public boolean isPagado() {
        return pagado;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }

    public void agregarDetalle(DetallePedido d) {
        d.setPedido(this);
        this.detalles.add(d);
    }

    @Transient
    public double getTotal() {
        return detalles.stream()
                .mapToDouble(DetallePedido::getSubtotal)
                .sum();
    }

}

