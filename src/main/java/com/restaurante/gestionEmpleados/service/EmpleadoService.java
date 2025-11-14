package com.restaurante.gestionEmpleados.service;

import com.restaurante.gestionEmpleados.model.Empleado;
import com.restaurante.gestionEmpleados.repository.EmpleadoRepository;
import com.restaurante.gestionPedidos.model.Pedido;
import com.restaurante.patrones.chainofresponsibility.ManejadorAdmin;
import com.restaurante.patrones.chainofresponsibility.ManejadorCocinero;
import com.restaurante.patrones.chainofresponsibility.ManejadorMesero;
import com.restaurante.patrones.chainofresponsibility.ManejadorPedido;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service refactorizado con patrón: Chain of Responsibility
 */
@Service
public class EmpleadoService {

    private final EmpleadoRepository repo;
    
    // Chain of Responsibility - Cadena de manejadores por rol
    private final ManejadorPedido chainOfResponsibility;

    public EmpleadoService(EmpleadoRepository repo) {
        this.repo = repo;
        // Configurar Chain of Responsibility
        this.chainOfResponsibility = configurarChain();
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

    // Chain of Responsibility - Manejar pedido según rol del empleado
    public void manejarPedidoPorRol(Pedido pedido, String rol) {
        chainOfResponsibility.manejar(pedido, rol);
    }

    // Chain of Responsibility - Manejar pedido usando ID de empleado
    public void manejarPedidoPorEmpleado(Long idEmpleado, Pedido pedido) {
        Empleado empleado = repo.findById(idEmpleado)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        chainOfResponsibility.manejar(pedido, empleado.getRol());
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

