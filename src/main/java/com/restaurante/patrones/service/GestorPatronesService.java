package com.restaurante.patrones.service;

import com.restaurante.gestionEmpleados.repository.EmpleadoRepository;
import com.restaurante.gestionPedidos.model.Pedido;
import com.restaurante.gestionPedidos.repository.PedidoRepository;
import com.restaurante.patrones.chainofresponsibility.*;
import com.restaurante.patrones.command.ComandoCrearPedido;
import com.restaurante.patrones.command.ComandoEnviarACocinero;
import com.restaurante.patrones.command.ComandoPedido;
import com.restaurante.patrones.facade.MenuFacade;
import com.restaurante.patrones.memento.HistorialPedidos;
import com.restaurante.patrones.mediator.MediadorRestaurante;
import com.restaurante.patrones.singleton.GestorConfiguracion;
import com.restaurante.patrones.state.EstadoCocinando;
import com.restaurante.patrones.state.EstadoTerminado;
import org.springframework.stereotype.Service;

/**
 * Servicio que integra todos los patrones de diseño
 */
@Service
public class GestorPatronesService {
    private final PedidoRepository pedidoRepository;
    private final EmpleadoRepository empleadoRepository;
    private final MediadorRestaurante mediador;
    private final HistorialPedidos historial;
    private final MenuFacade menuFacade;
    private final ManejadorPedido chainOfResponsibility;

    public GestorPatronesService(PedidoRepository pedidoRepository,
                                EmpleadoRepository empleadoRepository,
                                MediadorRestaurante mediador,
                                HistorialPedidos historial,
                                MenuFacade menuFacade) {
        this.pedidoRepository = pedidoRepository;
        this.empleadoRepository = empleadoRepository;
        this.mediador = mediador;
        this.historial = historial;
        this.menuFacade = menuFacade;
        
        // Configurar Chain of Responsibility
        this.chainOfResponsibility = configurarChain();
        
        // Configurar Singleton
        mostrarConfiguracion();
    }

    private ManejadorPedido configurarChain() {
        ManejadorCocinero cocinero = new ManejadorCocinero();
        ManejadorMesero mesero = new ManejadorMesero();
        ManejadorAdmin admin = new ManejadorAdmin();
        
        cocinero.establecerSiguiente(mesero);
        mesero.establecerSiguiente(admin);
        
        return cocinero;
    }

    private void mostrarConfiguracion() {
        GestorConfiguracion config = GestorConfiguracion.obtenerInstancia();
        config.mostrarConfiguracion();
    }

    /**
     * Crear pedido usando Command Pattern
     */
    public Pedido crearPedidoConCommand(String nombreCliente) {
        ComandoPedido comando = new ComandoCrearPedido(pedidoRepository, nombreCliente);
        comando.ejecutar();
        Pedido pedido = comando.obtenerPedido();
        historial.guardarEstado(pedido); // Memento
        return pedido;
    }

    /**
     * Enviar pedido al cocinero usando Command + Mediator
     */
    public void enviarPedidoACocinero(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        pedido.cambiarEstado(new EstadoCocinando());
        pedidoRepository.save(pedido);
        
        ComandoPedido comando = new ComandoEnviarACocinero(pedido, mediador);
        comando.ejecutar();
        
        historial.guardarEstado(pedido); // Memento
    }

    /**
     * Terminar pedido usando State Pattern
     */
    public void terminarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        pedido.cambiarEstado(new EstadoTerminado());
        pedidoRepository.save(pedido);
        
        mediador.pedidoListo(pedido); // Notifica a observadores
        historial.guardarEstado(pedido); // Memento
    }

    /**
     * Manejar pedido según rol usando Chain of Responsibility
     */
    public void manejarPedidoPorRol(Long pedidoId, String rol) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        
        chainOfResponsibility.manejar(pedido, rol);
    }

    /**
     * Obtener menú usando Facade
     */
    public void mostrarMenu() {
        menuFacade.mostrarMenuAlCliente();
    }

    /**
     * Ver historial usando Memento
     */
    public void verHistorial() {
        historial.mostrarHistorial();
    }

    /**
     * Registrar mesero para notificaciones (Observer)
     */
    public void registrarMeseroParaNotificaciones(String nombreMesero) {
        mediador.registrarMesero(nombreMesero);
    }
}

