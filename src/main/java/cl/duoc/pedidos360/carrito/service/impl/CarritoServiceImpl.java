package cl.duoc.pedidos360.carrito.service.impl;

import cl.duoc.pedidos360.carrito.dto.AgregarItemRequest;
import cl.duoc.pedidos360.carrito.dto.CarritoResponse;
import cl.duoc.pedidos360.carrito.entity.Carrito;
import cl.duoc.pedidos360.carrito.entity.CarritoItem;
import cl.duoc.pedidos360.carrito.exception.AccesoDenegadoException;
import cl.duoc.pedidos360.carrito.exception.ResourceNotFoundException;
import cl.duoc.pedidos360.carrito.repository.CarritoRepository;
import cl.duoc.pedidos360.carrito.service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;

    @Override
    public CarritoResponse obtenerCarritoActual(String usuarioId) {
        Carrito carrito = obtenerOCrearCarritoAbierto(usuarioId);
        return toResponse(carrito);
    }

    @Override
    public CarritoResponse agregarItem(String usuarioId, AgregarItemRequest request) {
        Carrito carrito = obtenerOCrearCarritoAbierto(usuarioId);

        CarritoItem item = CarritoItem.builder()
                .carrito(carrito)
                .productoId(request.getProductoId())
                .cantidad(request.getCantidad())
                .precioUnitario(request.getPrecioUnitario())
                .build();

        carrito.getItems().add(item);
        return toResponse(carritoRepository.save(carrito));
    }

    @Override
    public CarritoResponse eliminarItem(String usuarioId, Long itemId) {
        Carrito carrito = obtenerOCrearCarritoAbierto(usuarioId);
        boolean removido = carrito.getItems().removeIf(item -> item.getId().equals(itemId));
        if (!removido) {
            throw new ResourceNotFoundException("Item no encontrado en el carrito: " + itemId);
        }
        return toResponse(carritoRepository.save(carrito));
    }

    @Override
    public CarritoResponse confirmarCompra(String usuarioId) {
        Carrito carrito = obtenerOCrearCarritoAbierto(usuarioId);
        if (carrito.getItems().isEmpty()) {
            throw new IllegalStateException("No se puede confirmar un carrito vacio");
        }
        carrito.setEstado(Carrito.EstadoCarrito.CONFIRMADO);
        return toResponse(carritoRepository.save(carrito));
    }

    /**
     * Verificacion clave de seguridad a nivel de dominio: incluso teniendo
     * un JWT valido, un usuario SOLO puede leer/modificar su propio
     * carrito. El id del carrito nunca se usa solo; siempre se valida
     * contra el "usuarioId" extraido del token.
     */
    private void verificarPertenencia(Carrito carrito, String usuarioId) {
        if (!carrito.getUsuarioId().equals(usuarioId)) {
            throw new AccesoDenegadoException("El carrito no pertenece al usuario autenticado");
        }
    }

    private Carrito obtenerOCrearCarritoAbierto(String usuarioId) {
        return carritoRepository.findByUsuarioIdAndEstado(usuarioId, Carrito.EstadoCarrito.ABIERTO)
                .map(carrito -> {
                    verificarPertenencia(carrito, usuarioId);
                    return carrito;
                })
                .orElseGet(() -> carritoRepository.save(
                        Carrito.builder().usuarioId(usuarioId).build()
                ));
    }

    private CarritoResponse toResponse(Carrito carrito) {
        List<CarritoResponse.CarritoItemResponse> items = carrito.getItems().stream()
                .map(item -> CarritoResponse.CarritoItemResponse.builder()
                        .id(item.getId())
                        .productoId(item.getProductoId())
                        .cantidad(item.getCantidad())
                        .precioUnitario(item.getPrecioUnitario())
                        .subtotal(item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())))
                        .build())
                .toList();

        BigDecimal total = items.stream()
                .map(CarritoResponse.CarritoItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CarritoResponse.builder()
                .id(carrito.getId())
                .usuarioId(carrito.getUsuarioId())
                .estado(carrito.getEstado().name())
                .fechaCreacion(carrito.getFechaCreacion())
                .items(items)
                .total(total)
                .build();
    }
}
