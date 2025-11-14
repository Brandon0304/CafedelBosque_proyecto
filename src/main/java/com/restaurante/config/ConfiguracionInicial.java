package com.restaurante.config;

import com.restaurante.gestionEmpleados.model.Empleado;
import com.restaurante.gestionEmpleados.repository.EmpleadoRepository;
import com.restaurante.gestionInventario.model.InventarioItem;
import com.restaurante.gestionInventario.repository.InventarioRepository;
import com.restaurante.gestionProductos.model.Producto;
import com.restaurante.gestionProductos.repository.ProductoRepository;
import com.restaurante.patrones.singleton.GestorConfiguracion;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class ConfiguracionInicial {

    @Bean
    CommandLineRunner initData(ProductoRepository productoRepo,
                               InventarioRepository inventarioRepo,
                               EmpleadoRepository empleadoRepo) {
        return args -> {
            // Singleton - Mostrar configuración única del restaurante
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

