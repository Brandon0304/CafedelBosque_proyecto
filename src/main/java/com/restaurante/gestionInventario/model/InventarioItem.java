package com.restaurante.gestionInventario.model;

import jakarta.persistence.*;

@Entity
@Table(name = "inventario")
public class InventarioItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreInsumo;
    private String unidad;        // kg, unidades, litros, etc.
    private int stockActual;
    private int stockMinimo;

    public InventarioItem() {}

    public InventarioItem(Long id, String nombreInsumo, String unidad,
                          int stockActual, int stockMinimo) {
        this.id = id;
        this.nombreInsumo = nombreInsumo;
        this.unidad = unidad;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
    }

    public Long getId() {
        return id;
    }

    public String getNombreInsumo() {
        return nombreInsumo;
    }

    public String getUnidad() {
        return unidad;
    }

    public int getStockActual() {
        return stockActual;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombreInsumo(String nombreInsumo) {
        this.nombreInsumo = nombreInsumo;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    @Transient
    public boolean isBajoStock() {
        return stockActual <= stockMinimo;
    }

}

