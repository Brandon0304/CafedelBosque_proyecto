package com.restaurante.patrones.singleton;

/**
 * Patrón Singleton - Gestor de configuración único
 */
public class GestorConfiguracion {
    private static GestorConfiguracion instancia;
    private String nombreRestaurante;
    private String telefono;
    private String direccion;

    // Constructor privado para prevenir instanciación
    private GestorConfiguracion() {
        this.nombreRestaurante = "Café del Bosque";
        this.telefono = "+57 300 123 4567";
        this.direccion = "Calle Principal #123";
    }

    public static synchronized GestorConfiguracion obtenerInstancia() {
        if (instancia == null) {
            instancia = new GestorConfiguracion();
        }
        return instancia;
    }

    public String getNombreRestaurante() {
        return nombreRestaurante;
    }

    public void setNombreRestaurante(String nombreRestaurante) {
        this.nombreRestaurante = nombreRestaurante;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void mostrarConfiguracion() {
        System.out.println("\n=== CONFIGURACIÓN DEL RESTAURANTE ===");
        System.out.println("Nombre: " + nombreRestaurante);
        System.out.println("Teléfono: " + telefono);
        System.out.println("Dirección: " + direccion);
        System.out.println("=====================================\n");
    }
}

