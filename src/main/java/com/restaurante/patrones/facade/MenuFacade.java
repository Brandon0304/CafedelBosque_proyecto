package com.restaurante.patrones.facade;

import com.restaurante.gestionProductos.model.Producto;
import com.restaurante.gestionProductos.repository.ProductoRepository;
import com.restaurante.patrones.builder.Menu;
import com.restaurante.patrones.builder.MenuBuilder;
import com.restaurante.patrones.factory.ProductoCalienteFactory;
import com.restaurante.patrones.factory.ProductoFrioFactory;
import com.restaurante.patrones.factory.ProductoFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Patrón Facade - Interfaz simplificada para interactuar con el menú
 */
@Component
public class MenuFacade {
    private final ProductoRepository productoRepository;

    public MenuFacade(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Método simplificado para obtener el menú completo
     */
    public Menu obtenerMenuCompleto() {
        MenuBuilder builder = new MenuBuilder();
        
        // Usar Abstract Factory para crear productos
        ProductoFactory factoryCaliente = new ProductoCalienteFactory();
        ProductoFactory factoryFrio = new ProductoFrioFactory();

        builder.agregarBebida(factoryCaliente.crearBebida())
               .agregarBebida(factoryFrio.crearBebida())
               .agregarPlato(factoryCaliente.crearPlato())
               .agregarPlato(factoryFrio.crearPlato())
               .agregarPostre(factoryCaliente.crearPostre())
               .agregarPostre(factoryFrio.crearPostre());

        return builder.construir();
    }

    /**
     * Obtener solo productos disponibles del menú
     */
    public List<Producto> obtenerProductosDisponibles() {
        return productoRepository.findByDisponibleTrue();
    }

    /**
     * Mostrar menú de forma amigable al cliente
     */
    public void mostrarMenuAlCliente() {
        Menu menu = obtenerMenuCompleto();
        menu.mostrarMenu();
        
        System.out.println("\n--- Productos Disponibles en Base de Datos ---");
        obtenerProductosDisponibles().forEach(p -> 
            System.out.println("✓ " + p.getNombre() + " - $" + p.getPrecio())
        );
    }
}

