package com.restaurante.gestionEmpleados.service;

import com.restaurante.gestionEmpleados.model.Empleado;
import com.restaurante.gestionEmpleados.repository.EmpleadoRepository;
import com.restaurante.gestionPedidos.model.Pedido;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service refactorizado con patrón Chain of Responsibility integrado directamente
 */
@Service
public class EmpleadoService {

    private final EmpleadoRepository repo;
    
    // ========== PATRÓN CHAIN OF RESPONSIBILITY ==========
    // Chain of Responsibility Pattern - Cadena de manejadores por rol de empleado
    private final ManejadorPedido manejadorPrincipal;

    public EmpleadoService(EmpleadoRepository repo) {
        this.repo = repo;
        // Configurar Chain of Responsibility integrado
        this.manejadorPrincipal = configurarChain();
    }

    // Chain of Responsibility Pattern - Interfaz base del manejador (Handler abstracto)
    private abstract static class ManejadorPedido {
        protected ManejadorPedido siguiente;

        public void establecerSiguiente(ManejadorPedido siguiente) {
            this.siguiente = siguiente;
        }

        public abstract void manejar(Pedido pedido, String rol);

        protected void pasarASiguiente(Pedido pedido, String rol) {
            if (siguiente != null) {
                siguiente.manejar(pedido, rol);
            }
        }
    }

    // Chain of Responsibility - Manejador concreto: Cocinero
    private static class ManejadorCocinero extends ManejadorPedido {
        @Override
        public void manejar(Pedido pedido, String rol) {
            if ("COCINERO".equalsIgnoreCase(rol) || "BARISTA".equalsIgnoreCase(rol)) {
                System.out.println("👨‍🍳 Cocinero: Procesando pedido #" + pedido.getId());
                System.out.println("   Preparando " + pedido.getDetalles().size() + " item(s)...");
            } else {
                pasarASiguiente(pedido, rol);
            }
        }
    }

    // Chain of Responsibility - Manejador concreto: Mesero
    private static class ManejadorMesero extends ManejadorPedido {
        @Override
        public void manejar(Pedido pedido, String rol) {
            if ("MESERO".equalsIgnoreCase(rol)) {
                System.out.println("👤 Mesero: Atendiendo pedido #" + pedido.getId());
                System.out.println("   Cliente: " + pedido.getNombreCliente());
                System.out.println("   Total: $" + pedido.getTotal());
            } else {
                pasarASiguiente(pedido, rol);
            }
        }
    }

    // Chain of Responsibility - Manejador concreto: Admin (fallback)
    private static class ManejadorAdmin extends ManejadorPedido {
        @Override
        public void manejar(Pedido pedido, String rol) {
            if ("ADMIN".equalsIgnoreCase(rol)) {
                System.out.println("👔 Admin: Revisando pedido #" + pedido.getId());
            } else {
                System.out.println("⚠️ Rol '" + rol + "' no tiene manejador asignado");
            }
        }
    }

    private ManejadorPedido configurarChain() {
        ManejadorCocinero cocinero = new ManejadorCocinero();
        ManejadorMesero mesero = new ManejadorMesero();
        ManejadorAdmin admin = new ManejadorAdmin();
        
        cocinero.establecerSiguiente(mesero);
        mesero.establecerSiguiente(admin);
        
        return cocinero;
    }

    public List<Empleado> listarTodos() {
        return repo.findAll();
    }

    public Optional<Empleado> buscarPorId(Long id) {
        return repo.findById(id);
    }

    // Chain of Responsibility Pattern - Manejar pedido según rol del empleado
    public void manejarPedidoPorRol(Pedido pedido, String rol) {
        manejadorPrincipal.manejar(pedido, rol);
    }

    // Chain of Responsibility Pattern - Manejar pedido usando ID de empleado
    public void manejarPedidoPorEmpleado(Long idEmpleado, Pedido pedido) {
        Empleado empleado = repo.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        manejadorPrincipal.manejar(pedido, empleado.getRol());
    }

    public List<Empleado> buscarPorRol(String rol) {
        return repo.findByRol(rol);
    }

    public Empleado crear(Empleado e) {
        e.setId(null);
        return repo.save(e);
    }

    public Optional<Empleado> actualizar(Long id, Empleado datos) {
        return repo.findById(id).map(e -> {
            e.setNombre(datos.getNombre());
            e.setRol(datos.getRol());
            e.setSalario(datos.getSalario());
            return repo.save(e);
        });
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }

}

