package com.restaurante.gestionPedidos.model;

import com.restaurante.patrones.state.EstadoPedido;
import com.restaurante.patrones.state.EstadoRecibido;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCliente;  // o mesa
    private LocalDateTime fechaHora;
    private boolean pagado;
    
    @Transient
    private EstadoPedido estadoActual;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedido> detalles = new ArrayList<>();

    public Pedido() {
        this.fechaHora = LocalDateTime.now();
        this.pagado = false;
        this.estadoActual = new EstadoRecibido(); // Estado inicial
    }

    public Pedido(Long id, String nombreCliente) {
        this.id = id;
        this.nombreCliente = nombreCliente;
        this.fechaHora = LocalDateTime.now();
        this.pagado = false;
        this.estadoActual = new EstadoRecibido(); // Estado inicial
    }
    
    public void cambiarEstado(EstadoPedido nuevoEstado) {
        this.estadoActual = nuevoEstado;
    }
    
    public EstadoPedido getEstadoActual() {
        return estadoActual;
    }
    
    public String obtenerEstadoActual() {
        return estadoActual != null ? estadoActual.obtenerEstado() : "SIN_ESTADO";
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

