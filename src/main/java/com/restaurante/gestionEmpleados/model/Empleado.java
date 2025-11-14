package com.restaurante.gestionEmpleados.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "empleados")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String rol;           // BARISTA, MESERO, ADMIN, etc.
    private BigDecimal salario;

    public Empleado() {}

    public Empleado(Long id, String nombre, String rol, BigDecimal salario) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.salario = salario;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRol() {
        return rol;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

}

