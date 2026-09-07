package cl.duoc.pedidos360.carrito;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Microservicio de Carrito / Compras - Pedidos360.
 *
 * Gestiona el registro de pedidos de forma privada y protegida. A
 * diferencia del catalogo, NINGUN endpoint de este servicio es publico:
 * todos exigen un JWT valido y, en las operaciones de escritura, un
 * scope/rol especifico (ver SecurityConfig).
 */
@SpringBootApplication
public class CarritoComprasApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarritoComprasApplication.class, args);
    }
}
