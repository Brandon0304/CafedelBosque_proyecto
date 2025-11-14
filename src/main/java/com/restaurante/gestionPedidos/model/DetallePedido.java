package com.restaurante.gestionPedidos.model;

import com.restaurante.gestionProductos.model.Producto;
import jakarta.persistence.*;

@Entity
@Table(name = "detalles_pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cantidad;
    private double precioUnitario;

    @ManyToOne(optional = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    public DetallePedido() {}

    public DetallePedido(Long id, int cantidad, double precioUnitario, Producto producto) {
        this.id = id;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.producto = producto;
    }

    public Long getId() {
        return id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public Producto getProducto() {
        return producto;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Transient
    public double getSubtotal() {
        return cantidad * precioUnitario;
    }

}

