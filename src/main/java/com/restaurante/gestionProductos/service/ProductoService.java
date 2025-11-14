package com.restaurante.gestionProductos.service;

import com.restaurante.gestionProductos.model.Producto;
import com.restaurante.gestionProductos.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service refactorizado con patrones integrados: Abstract Factory, Builder, Decorator, Facade
 */
@Service
public class ProductoService {

    private final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    // ========== PATRÓN SINGLETON ==========
    // Singleton Pattern - Factory única para crear productos
    private static class ProductoFactory {
        private static ProductoFactory instancia;
        
        private ProductoFactory() {} // Constructor privado para evitar instancias externas
        
        // Singleton Pattern - Obtener única instancia (lazy initialization)
        public static ProductoFactory obtenerInstancia() {
            if (instancia == null) {
                instancia = new ProductoFactory();
            }
            return instancia;
        }
        
        // ========== PATRÓN ABSTRACT FACTORY ==========
        // Abstract Factory Pattern - Crear familia de productos calientes
        public Producto crearBebidaCaliente() {
            Producto p = new Producto();
            p.setNombre("Café Caliente");
            p.setCategoria("BEBIDA_CALIENTE");
            p.setPrecio(new BigDecimal("5000"));
            p.setDisponible(true);
            return p;
        }
        
        public Producto crearPlatoCaliente() {
            Producto p = new Producto();
            p.setNombre("Sopa del Día");
            p.setCategoria("PLATO_CALIENTE");
            p.setPrecio(new BigDecimal("12000"));
            p.setDisponible(true);
            return p;
        }
        
        public Producto crearPostreCaliente() {
            Producto p = new Producto();
            p.setNombre("Tarta Caliente de Chocolate");
            p.setCategoria("POSTRE_CALIENTE");
            p.setPrecio(new BigDecimal("8000"));
            p.setDisponible(true);
            return p;
        }
        
        // Abstract Factory Pattern - Crear familia de productos fríos
        public Producto crearBebidaFria() {
            Producto p = new Producto();
            p.setNombre("Limonada");
            p.setCategoria("BEBIDA_FRIA");
            p.setPrecio(new BigDecimal("6000"));
            p.setDisponible(true);
            return p;
        }
        
        public Producto crearPlatoFrio() {
            Producto p = new Producto();
            p.setNombre("Ensalada César");
            p.setCategoria("PLATO_FRIO");
            p.setPrecio(new BigDecimal("15000"));
            p.setDisponible(true);
            return p;
        }
        
        public Producto crearPostreFrio() {
            Producto p = new Producto();
            p.setNombre("Helado de Vainilla");
            p.setCategoria("POSTRE_FRIO");
            p.setPrecio(new BigDecimal("7000"));
            p.setDisponible(true);
            return p;
        }
    }

    // ========== PATRÓN BUILDER ==========
    // Builder Pattern - Construir menú complejo paso a paso
    public static class MenuBuilder {
        private final List<Producto> productos = new ArrayList<>();
        private final ProductoFactory factory = ProductoFactory.obtenerInstancia();

        public MenuBuilder agregarBebida(Producto bebida) {
            productos.add(bebida);
            return this;
        }

        public MenuBuilder agregarPlato(Producto plato) {
            productos.add(plato);
            return this;
        }

        public MenuBuilder agregarPostre(Producto postre) {
            productos.add(postre);
            return this;
        }

        public MenuBuilder conBebidasCalientes() {
            productos.add(factory.crearBebidaCaliente());
            return this;
        }

        public MenuBuilder conBebidasFrias() {
            productos.add(factory.crearBebidaFria());
            return this;
        }

        public MenuBuilder conPlatosCalientes() {
            productos.add(factory.crearPlatoCaliente());
            return this;
        }

        public MenuBuilder conPlatosFrios() {
            productos.add(factory.crearPlatoFrio());
            return this;
        }

        public MenuBuilder conPostresCalientes() {
            productos.add(factory.crearPostreCaliente());
            return this;
        }

        public MenuBuilder conPostresFrios() {
            productos.add(factory.crearPostreFrio());
            return this;
        }

        // Builder Pattern - Método final para construir el objeto completo
        public List<Producto> construir() {
            return new ArrayList<>(productos);
        }
    }

    // ========== PATRÓN DECORATOR ==========
    // Decorator Pattern - Agregar funcionalidad (descuento) sin modificar la clase original
    public Producto aplicarDescuento(Producto producto, double porcentajeDescuento) {
        BigDecimal precioOriginal = producto.getPrecio();
        BigDecimal descuento = precioOriginal.multiply(BigDecimal.valueOf(porcentajeDescuento / 100.0));
        BigDecimal precioFinal = precioOriginal.subtract(descuento);
        
        Producto decorado = new Producto();
        decorado.setId(producto.getId());
        decorado.setNombre(producto.getNombre() + " (con " + porcentajeDescuento + "% descuento)");
        decorado.setCategoria(producto.getCategoria());
        decorado.setPrecio(precioFinal);
        decorado.setDisponible(producto.isDisponible());
        
        return decorado;
    }

    // Decorator Pattern - Agregar funcionalidad (extra) sin modificar la clase original
    public Producto agregarExtra(Producto producto, String extra, BigDecimal precioExtra) {
        Producto decorado = new Producto();
        decorado.setId(producto.getId());
        decorado.setNombre(producto.getNombre() + " con " + extra);
        decorado.setCategoria(producto.getCategoria());
        decorado.setPrecio(producto.getPrecio().add(precioExtra));
        decorado.setDisponible(producto.isDisponible());
        
        return decorado;
    }

    // Facade Pattern - Métodos simplificados para interactuar con productos
    public List<Producto> listarTodos() {
        return repo.findAll();
    }

    public List<Producto> listarDisponibles() {
        return repo.findByDisponibleTrue();
    }

    public Optional<Producto> buscarPorId(Long id) {
        return repo.findById(id);
    }

    // Builder Pattern - Construir menú paso a paso usando el builder
    public List<Producto> construirMenu() {
        MenuBuilder builder = new MenuBuilder();
        
        return builder.conBebidasCalientes()
                     .conBebidasFrias()
                     .conPlatosCalientes()
                     .conPlatosFrios()
                     .conPostresCalientes()
                     .conPostresFrios()
                     .construir(); // Builder Pattern - Fluent interface
    }

    // Abstract Factory Pattern - Crear productos usando factory
    public Producto crearBebidaCaliente() {
        ProductoFactory factory = ProductoFactory.obtenerInstancia();
        return repo.save(factory.crearBebidaCaliente());
    }

    public Producto crearBebidaFria() {
        ProductoFactory factory = ProductoFactory.obtenerInstancia();
        return repo.save(factory.crearBebidaFria());
    }

    public Producto crearPlatoCaliente() {
        ProductoFactory factory = ProductoFactory.obtenerInstancia();
        return repo.save(factory.crearPlatoCaliente());
    }

    public Producto crearPlatoFrio() {
        ProductoFactory factory = ProductoFactory.obtenerInstancia();
        return repo.save(factory.crearPlatoFrio());
    }

    public Producto crearPostreCaliente() {
        ProductoFactory factory = ProductoFactory.obtenerInstancia();
        return repo.save(factory.crearPostreCaliente());
    }

    public Producto crearPostreFrio() {
        ProductoFactory factory = ProductoFactory.obtenerInstancia();
        return repo.save(factory.crearPostreFrio());
    }

    public Producto crear(Producto p) {
        p.setId(null);
        return repo.save(p);
    }

    public Optional<Producto> actualizar(Long id, Producto datos) {
        return repo.findById(id).map(p -> {
            p.setNombre(datos.getNombre());
            p.setCategoria(datos.getCategoria());
            p.setPrecio(datos.getPrecio());
            p.setDisponible(datos.isDisponible());
            return repo.save(p);
        });
    }

    // Decorator Pattern - Aplicar descuento a un producto existente
    public Producto aplicarDescuento(Long id, double porcentajeDescuento) {
        Producto producto = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        Producto decorado = aplicarDescuento(producto, porcentajeDescuento);
        decorado.setId(producto.getId());
        
        return repo.save(decorado);
    }

    // Decorator Pattern - Agregar extra a un producto
    public Producto agregarExtra(Long id, String extra, BigDecimal precioExtra) {
        Producto producto = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        
        Producto decorado = agregarExtra(producto, extra, precioExtra);
        decorado.setId(producto.getId());
        
        return repo.save(decorado);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    // Facade Pattern - Método simplificado para mostrar menú al cliente
    public void mostrarMenu() {
        System.out.println("\n=== MENÚ DEL DÍA (Builder + Factory) ===");
        List<Producto> menu = construirMenu();
        menu.forEach(p -> 
            System.out.println("- " + p.getNombre() + " ($" + p.getPrecio() + ") - " + p.getCategoria())
        );
        
        System.out.println("\n--- Productos Disponibles en Base de Datos ---");
        listarDisponibles().forEach(p -> 
            System.out.println("✓ " + p.getNombre() + " - $" + p.getPrecio())
        );
        System.out.println();
    }

}

