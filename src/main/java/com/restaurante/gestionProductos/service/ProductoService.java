package com.restaurante.gestionProductos.service;

import com.restaurante.gestionProductos.model.Producto;
import com.restaurante.gestionProductos.repository.ProductoRepository;
import com.restaurante.patrones.decorator.ProductoConDescuento;
import com.restaurante.patrones.decorator.ProductoConExtra;
import com.restaurante.patrones.decorator.ProductoDecorator;
import com.restaurante.patrones.factory.ProductoCalienteFactory;
import com.restaurante.patrones.factory.ProductoFrioFactory;
import com.restaurante.patrones.factory.ProductoFactory;
import com.restaurante.patrones.builder.MenuBuilder;
import com.restaurante.patrones.builder.Menu;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service refactorizado con patrones: Abstract Factory, Builder, Decorator, Facade
 */
@Service
public class ProductoService {

    private final ProductoRepository repo;
    
    // Facade - Interfaz simplificada para crear productos
    private final ProductoFactory factoryCaliente;
    private final ProductoFactory factoryFrio;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
        // Abstract Factory - Inicializar factories
        this.factoryCaliente = new ProductoCalienteFactory();
        this.factoryFrio = new ProductoFrioFactory();
    }

    // Facade - Método simplificado para listar todos
    public List<Producto> listarTodos() {
        return repo.findAll();
    }

    // Facade - Método simplificado para listar disponibles
    public List<Producto> listarDisponibles() {
        return repo.findByDisponibleTrue();
    }

    public Optional<Producto> buscarPorId(Long id) {
        return repo.findById(id);
    }

    // Builder Pattern - Construir menú paso a paso
    public Menu construirMenu() {
        MenuBuilder builder = new MenuBuilder();
        
        // Agregar productos usando Factory
        builder.agregarBebida(factoryCaliente.crearBebida())
               .agregarBebida(factoryFrio.crearBebida())
               .agregarPlato(factoryCaliente.crearPlato())
               .agregarPlato(factoryFrio.crearPlato())
               .agregarPostre(factoryCaliente.crearPostre())
               .agregarPostre(factoryFrio.crearPostre());
        
        return builder.construir();
    }

    // Abstract Factory - Crear producto usando factory
    public Producto crearBebidaCaliente() {
        return repo.save(factoryCaliente.crearBebida());
    }

    public Producto crearBebidaFria() {
        return repo.save(factoryFrio.crearBebida());
    }

    public Producto crearPlatoCaliente() {
        return repo.save(factoryCaliente.crearPlato());
    }

    public Producto crearPlatoFrio() {
        return repo.save(factoryFrio.crearPlato());
    }

    public Producto crearPostreCaliente() {
        return repo.save(factoryCaliente.crearPostre());
    }

    public Producto crearPostreFrio() {
        return repo.save(factoryFrio.crearPostre());
    }

    // Método tradicional también disponible
    public Producto crear(Producto p) {
        p.setId(null);
        return repo.save(p);
    }

    // Decorator Pattern - Actualizar producto aplicando decoradores
    public Optional<Producto> actualizar(Long id, Producto datos) {
        return repo.findById(id).map(p -> {
            p.setNombre(datos.getNombre());
            p.setCategoria(datos.getCategoria());
            p.setPrecio(datos.getPrecio());
            p.setDisponible(datos.isDisponible());
            return repo.save(p);
        });
    }

    // Decorator - Aplicar descuento a un producto existente
    public Producto aplicarDescuento(Long id, double porcentajeDescuento) {
        Producto producto = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        ProductoDecorator decorado = new ProductoConDescuento(producto, porcentajeDescuento);
        
        // Actualizar el producto original con el precio decorado
        producto.setPrecio(decorado.getPrecio());
        producto.setNombre(decorado.getNombre());
        
        return repo.save(producto);
    }

    // Decorator - Agregar extra a un producto
    public Producto agregarExtra(Long id, String extra, BigDecimal precioExtra) {
        Producto producto = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        ProductoDecorator decorado = new ProductoConExtra(producto, extra, precioExtra);
        
        producto.setPrecio(decorado.getPrecio());
        producto.setNombre(decorado.getNombre());
        
        return repo.save(producto);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    // Facade - Método simplificado para mostrar menú
    public void mostrarMenu() {
        Menu menu = construirMenu();
        menu.mostrarMenu();
        
        System.out.println("\n--- Productos Disponibles en Base de Datos ---");
        listarDisponibles().forEach(p -> 
            System.out.println("✓ " + p.getNombre() + " - $" + p.getPrecio())
        );
    }

}

