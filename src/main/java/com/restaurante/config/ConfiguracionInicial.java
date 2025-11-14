package com.restaurante.config;

import com.restaurante.gestionEmpleados.model.Empleado;
import com.restaurante.gestionEmpleados.repository.EmpleadoRepository;
import com.restaurante.gestionInventario.model.InventarioItem;
import com.restaurante.gestionInventario.repository.InventarioRepository;
import com.restaurante.gestionProductos.model.Producto;
import com.restaurante.gestionProductos.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * Configuración inicial con patrón Singleton integrado
 */
@Configuration
public class ConfiguracionInicial {

    // ========== PATRÓN SINGLETON ==========
    // Singleton Pattern - Configuración única del restaurante (una sola instancia)
    private static class GestorConfiguracion {
        private static GestorConfiguracion instancia;
        private String nombreRestaurante = "Café del Bosque";
        private String telefono = "+57 300 123 4567";
        private String direccion = "Calle Principal #123";

        private GestorConfiguracion() {} // Constructor privado para evitar instancias externas

        // Singleton Pattern - Obtener única instancia (thread-safe)
        public static synchronized GestorConfiguracion obtenerInstancia() {
            if (instancia == null) {
                instancia = new GestorConfiguracion();
            }
            return instancia;
        }

        public void mostrarConfiguracion() {
            System.out.println("\n=== CONFIGURACIÓN DEL RESTAURANTE (Singleton) ===");
            System.out.println("Nombre: " + nombreRestaurante);
            System.out.println("Teléfono: " + telefono);
            System.out.println("Dirección: " + direccion);
            System.out.println("=====================================\n");
        }
    }

    @Bean
    CommandLineRunner initData(ProductoRepository productoRepo,
                               InventarioRepository inventarioRepo,
                               EmpleadoRepository empleadoRepo) {
        return args -> {
            // Singleton Pattern - Mostrar configuración única del restaurante
            GestorConfiguracion config = GestorConfiguracion.obtenerInstancia();
            config.mostrarConfiguracion();
            
            // Productos
            if (productoRepo.count() == 0) {
                productoRepo.save(new Producto(null, "Capuchino", "BEBIDA_CALIENTE",
                        new BigDecimal("6500"), true));
                productoRepo.save(new Producto(null, "Limonada de panela", "BEBIDA_FRIA",
                        new BigDecimal("5000"), true));
                productoRepo.save(new Producto(null, "Brownie de chocolate", "POSTRE",
                        new BigDecimal("6000"), true));
            }

            // Inventario
            if (inventarioRepo.count() == 0) {
                inventarioRepo.save(new InventarioItem(null, "Café molido", "kg", 10, 3));
                inventarioRepo.save(new InventarioItem(null, "Pan para sandwich", "unidades", 50, 15));
            }

            // Empleados
            if (empleadoRepo.count() == 0) {
                empleadoRepo.save(new Empleado(null, "Laura Barista", "BARISTA",
                        new BigDecimal("1600000")));
                empleadoRepo.save(new Empleado(null, "Carlos Mesero", "MESERO",
                        new BigDecimal("1500000")));
            }
        };
    }

}

