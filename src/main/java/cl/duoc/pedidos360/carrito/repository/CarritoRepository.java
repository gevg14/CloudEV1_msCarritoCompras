package cl.duoc.pedidos360.carrito.repository;

import cl.duoc.pedidos360.carrito.entity.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    List<Carrito> findByUsuarioId(String usuarioId);

    Optional<Carrito> findByUsuarioIdAndEstado(String usuarioId, Carrito.EstadoCarrito estado);
}
