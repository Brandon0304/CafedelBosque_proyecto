# 📚 Patrones de Diseño Implementados

Este documento describe todos los patrones de diseño aplicados en el proyecto.

## ✅ Patrones Implementados

### 1. **State (Comportamental)** - Estados de Pedido
**Ubicación:** `com.restaurante.patrones.state`

**Propósito:** Manejar los estados del pedido (recibido, cocinando, terminado)

**Clases:**
- `EstadoPedido` (interfaz)
- `EstadoRecibido` - Estado inicial
- `EstadoCocinando` - Pedido en preparación
- `EstadoTerminado` - Pedido finalizado

**Uso:**
```java
Pedido pedido = new Pedido();
pedido.cambiarEstado(new EstadoCocinando());
System.out.println(pedido.obtenerEstadoActual()); // "COCINANDO"
```

---

### 2. **Command (Comportamental)** - Comandos de Pedido
**Ubicación:** `com.restaurante.patrones.command`

**Propósito:** Encapsular peticiones como objetos para generar y enviar pedidos

**Clases:**
- `ComandoPedido` (interfaz)
- `ComandoCrearPedido` - Crea un nuevo pedido
- `ComandoEnviarACocinero` - Envía pedido al cocinero

**Uso:**
```java
ComandoPedido comando = new ComandoCrearPedido(pedidoRepo, "Juan Pérez");
comando.ejecutar();
comando.deshacer(); // Reversible
```

**API:** `POST /api/v1/patrones/pedido/crear`

---

### 3. **Observer (Comportamental)** - Notificaciones
**Ubicación:** `com.restaurante.patrones.observer`

**Propósito:** Notificar al mesero el estado del pedido

**Clases:**
- `ObservadorPedido` (interfaz)
- `ObservadorMesero` - Recibe notificaciones
- `SujetoPedido` - Notifica cambios

**Uso:**
```java
SujetoPedido sujeto = new SujetoPedido();
sujeto.agregarObservador(new ObservadorMesero("Carlos"));
sujeto.notificarObservadores("TERMINADO");
```

**API:** `POST /api/v1/patrones/registrar-mesero`

---

### 4. **Mediator (Comportamental)** - Intermediario
**Ubicación:** `com.restaurante.patrones.mediator`

**Propósito:** Generar un intermediario entre el cliente y el cocinero

**Clases:**
- `MediadorRestaurante` - Coordina comunicación

**Uso:**
```java
mediador.enviarPedidoACocinero(pedido);
mediador.pedidoListo(pedido);
```

**API:** `POST /api/v1/patrones/pedido/{id}/enviar-cocinero`

---

### 5. **Abstract Factory (Creacional)** - Familias de Productos
**Ubicación:** `com.restaurante.patrones.factory`

**Propósito:** Crear familias de objetos (bebidas, platos fuertes, postres)

**Clases:**
- `ProductoFactory` (abstracta)
- `ProductoCalienteFactory` - Productos calientes
- `ProductoFrioFactory` - Productos fríos

**Uso:**
```java
ProductoFactory factory = new ProductoCalienteFactory();
Producto bebida = factory.crearBebida();
Producto plato = factory.crearPlato();
Producto postre = factory.crearPostre();
```

---

### 6. **Builder (Creacional)** - Construcción de Menú
**Ubicación:** `com.restaurante.patrones.builder`

**Propósito:** Construir el menú paso a paso y encapsular con Command

**Clases:**
- `MenuBuilder` - Construye el menú
- `Menu` - Producto final

**Uso:**
```java
Menu menu = new MenuBuilder()
    .agregarBebida(producto1)
    .agregarPlato(producto2)
    .agregarPostre(producto3)
    .construir();
```

---

### 7. **Decorator (Estructural)** - Actualizar Productos
**Ubicación:** `com.restaurante.patrones.decorator`

**Propósito:** Actualizar y agregar nuevos productos al menú dinámicamente

**Clases:**
- `ProductoDecorator` (abstracta)
- `ProductoConDescuento` - Aplica descuento
- `ProductoConExtra` - Agrega extras

**Uso:**
```java
Producto base = new Producto(...);
ProductoDecorator conDescuento = new ProductoConDescuento(base, 20.0);
ProductoDecorator conExtra = new ProductoConExtra(conDescuento, "Leche extra", new BigDecimal(1000));
```

---

### 8. **Facade (Estructural)** - Interfaz Simplificada
**Ubicación:** `com.restaurante.patrones.facade`

**Propósito:** Crear interfaz simplificada para interactuar con el cliente (menú)

**Clases:**
- `MenuFacade` - Interfaz simplificada

**Uso:**
```java
menuFacade.mostrarMenuAlCliente();
List<Producto> disponibles = menuFacade.obtenerProductosDisponibles();
```

**API:** `GET /api/v1/patrones/menu`

---

### 9. **Chain of Responsibility (Comportamental)** - Roles
**Ubicación:** `com.restaurante.patrones.chainofresponsibility`

**Propósito:** Repartir responsabilidades entre roles (cocinero, mesero, admin)

**Clases:**
- `ManejadorPedido` (abstracta)
- `ManejadorCocinero` - Maneja pedidos de cocineros
- `ManejadorMesero` - Maneja pedidos de meseros
- `ManejadorAdmin` - Fallback para admin

**Uso:**
```java
ManejadorPedido chain = configurarChain();
chain.manejar(pedido, "MESERO");
```

**API:** `POST /api/v1/patrones/pedido/{id}/manejar`

---

### 10. **Memento (Comportamental)** - Historial
**Ubicación:** `com.restaurante.patrones.memento`

**Propósito:** Ver y guardar el historial de los pedidos

**Clases:**
- `PedidoMemento` - Guarda estado del pedido
- `HistorialPedidos` - Gestiona historial

**Uso:**
```java
historial.guardarEstado(pedido);
historial.mostrarHistorial();
PedidoMemento memento = historial.obtenerPorId(pedidoId);
```

**API:** `GET /api/v1/patrones/historial`

---

### 11. **Singleton (Creacional)** - Instancia Única
**Ubicación:** `com.restaurante.patrones.singleton`

**Propósito:** Manejar una única instancia (configuración)

**Clases:**
- `GestorConfiguracion` - Configuración única del restaurante

**Uso:**
```java
GestorConfiguracion config = GestorConfiguracion.obtenerInstancia();
config.mostrarConfiguracion();
```

---

## 🚀 Endpoints de la API de Patrones

Todos los endpoints están en `/api/v1/patrones`:

1. **Crear pedido con Command:**
   ```
   POST /api/v1/patrones/pedido/crear
   Body: { "nombreCliente": "Juan Pérez" }
   ```

2. **Enviar pedido al cocinero:**
   ```
   POST /api/v1/patrones/pedido/{id}/enviar-cocinero
   ```

3. **Terminar pedido (State):**
   ```
   POST /api/v1/patrones/pedido/{id}/terminar
   ```

4. **Manejar pedido por rol (Chain of Responsibility):**
   ```
   POST /api/v1/patrones/pedido/{id}/manejar
   Body: { "rol": "MESERO" }
   ```

5. **Mostrar menú (Facade):**
   ```
   GET /api/v1/patrones/menu
   ```

6. **Ver historial (Memento):**
   ```
   GET /api/v1/patrones/historial
   ```

7. **Registrar mesero (Observer):**
   ```
   POST /api/v1/patrones/registrar-mesero
   Body: { "nombre": "Carlos Mesero" }
   ```

---

## 📋 Estructura de Paquetes

```
com.restaurante.patrones/
├── state/              # State Pattern
├── command/            # Command Pattern
├── observer/           # Observer Pattern
├── mediator/           # Mediator Pattern
├── factory/            # Abstract Factory Pattern
├── builder/            # Builder Pattern
├── decorator/          # Decorator Pattern
├── facade/             # Facade Pattern
├── chainofresponsibility/  # Chain of Responsibility
├── memento/            # Memento Pattern
├── singleton/          # Singleton Pattern
├── service/            # Servicio integrador
└── controller/         # Controller de patrones
```

---

## 🎯 Ejemplo Completo de Uso

```java
// 1. Crear pedido con Command
Pedido pedido = gestorPatrones.crearPedidoConCommand("Juan Pérez");

// 2. Enviar al cocinero (Command + Mediator)
gestorPatrones.enviarPedidoACocinero(pedido.getId());

// 3. Terminar pedido (State + Observer notifica mesero)
gestorPatrones.terminarPedido(pedido.getId());

// 4. Ver historial (Memento)
gestorPatrones.verHistorial();
```

---

## ✨ Notas Importantes

- Todos los patrones están integrados y funcionando
- El servicio `GestorPatronesService` coordina todos los patrones
- Los patrones se pueden usar independientemente o en conjunto
- Los logs muestran el flujo de ejecución de cada patrón

