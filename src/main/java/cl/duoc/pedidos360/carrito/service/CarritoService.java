package cl.duoc.pedidos360.carrito.service;

import cl.duoc.pedidos360.carrito.dto.AgregarItemRequest;
import cl.duoc.pedidos360.carrito.dto.CarritoResponse;

public interface CarritoService {

    /**
     * Obtiene (o crea si no existe) el carrito ABIERTO del usuario
     * autenticado. usuarioId proviene siempre del JWT, nunca del body
     * de la request, para evitar que un usuario opere el carrito de otro.
     */
    CarritoResponse obtenerCarritoActual(String usuarioId);

    CarritoResponse agregarItem(String usuarioId, AgregarItemRequest request);

    CarritoResponse eliminarItem(String usuarioId, Long itemId);

    CarritoResponse confirmarCompra(String usuarioId);
}
