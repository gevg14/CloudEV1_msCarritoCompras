package cl.duoc.pedidos360.carrito.controller;

import cl.duoc.pedidos360.carrito.dto.AgregarItemRequest;
import cl.duoc.pedidos360.carrito.dto.CarritoResponse;
import cl.duoc.pedidos360.carrito.service.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

/**
 * Todos los endpoints exigen JWT valido (ver SecurityConfig: anyRequest
 * es "authenticated" en este microservicio). Ademas, las operaciones de
 * escritura exigen el scope "Carrito.Write" emitido por Azure AD.
 *
 * El id del usuario NUNCA se recibe como parametro del cliente: siempre
 * se extrae del claim del propio JWT ya validado ("oid" o "sub"), para
 * que sea imposible operar el carrito de otra persona.
 */
@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @GetMapping
    public ResponseEntity<CarritoResponse> obtenerCarritoActual(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(carritoService.obtenerCarritoActual(usuarioIdDesde(jwt)));
    }

    @PostMapping("/items")
    @PreAuthorize("hasAuthority('SCOPE_Carrito.Write')")
    public ResponseEntity<CarritoResponse> agregarItem(@AuthenticationPrincipal Jwt jwt,
                                                         @Valid @RequestBody AgregarItemRequest request) {
        return ResponseEntity.ok(carritoService.agregarItem(usuarioIdDesde(jwt), request));
    }

    @DeleteMapping("/items/{itemId}")
    @PreAuthorize("hasAuthority('SCOPE_Carrito.Write')")
    public ResponseEntity<CarritoResponse> eliminarItem(@AuthenticationPrincipal Jwt jwt,
                                                          @PathVariable Long itemId) {
        return ResponseEntity.ok(carritoService.eliminarItem(usuarioIdDesde(jwt), itemId));
    }

    @PostMapping("/confirmar")
    @PreAuthorize("hasAuthority('SCOPE_Carrito.Write')")
    public ResponseEntity<CarritoResponse> confirmarCompra(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(carritoService.confirmarCompra(usuarioIdDesde(jwt)));
    }

    /**
     * "oid" es el identificador de objeto estable del usuario en Azure AD
     * (recomendado por Microsoft por sobre "sub" para este uso). Se deja
     * un fallback a "sub" por si el tenant no emite "oid" en el JWT.
     */
    private String usuarioIdDesde(Jwt jwt) {
        String oid = jwt.getClaimAsString("oid");
        return (oid != null) ? oid : jwt.getSubject();
    }
}
