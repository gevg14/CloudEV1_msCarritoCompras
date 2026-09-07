package cl.duoc.pedidos360.carrito.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carritos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // "subject" (oid/sub) del usuario autenticado, extraido del JWT de
    // Azure AD. Asi cada usuario solo ve y modifica su propio carrito.
    @Column(name = "usuario_id", nullable = false, length = 100)
    private String usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private EstadoCarrito estado = EstadoCarrito.ABIERTO;

    @Column(name = "fecha_creacion", nullable = false)
    @Builder.Default
    private Instant fechaCreacion = Instant.now();

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CarritoItem> items = new ArrayList<>();

    public enum EstadoCarrito {
        ABIERTO, CONFIRMADO, CANCELADO
    }
}
