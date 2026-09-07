# ms-carrito-compras

Microservicio de carrito/compras del sistema **Pedidos360**. A diferencia
del catálogo, es **completamente privado**: ningún endpoint es público,
todos exigen un JWT válido emitido por Azure AD, y las operaciones de
escritura además exigen el scope `Carrito.Write`.

## Estructura

```
src/main/java/cl/duoc/pedidos360/carrito/
 ├─ CarritoComprasApplication.java
 ├─ config/SecurityConfig.java          anyRequest().authenticated() + @EnableMethodSecurity
 ├─ controller/CarritoController.java   extrae el usuario SIEMPRE desde el JWT (claim oid/sub)
 ├─ dto/
 ├─ entity/Carrito.java, CarritoItem.java
 ├─ repository/CarritoRepository.java
 ├─ service/                           valida que el carrito pertenezca al usuario autenticado
 └─ exception/
```

## Decisiones de diseño relevantes

- **El `usuarioId` nunca viaja en el body de la request.** Siempre se
  obtiene del JWT ya validado (claim `oid`, con fallback a `sub`). Esto
  evita que un usuario autenticado pueda leer o modificar el carrito de
  otra persona simplemente cambiando un id en el request.
- **Doble candado en escritura:** `@PreAuthorize("hasAuthority('SCOPE_Carrito.Write')")`
  exige que el JWT traiga ese scope, ademas de estar autenticado.

## Cómo correrlo localmente

```bash
mvn spring-boot:run
```

Perfil `dev` por defecto, con H2 en memoria (puerto de la app: 8082).

## Pendiente para el encargo (EP1) y el despliegue (EP2)

- [ ] Reemplazar `{tenant-id}` en `application-dev.yml` con el tenant real.
- [ ] Crear en Azure AD el scope `Carrito.Write` dentro del App Registration
      y asignarlo a los usuarios/roles correspondientes.
- [ ] Desplegar en EC2 y registrar sus rutas en el API Gateway de AWS,
      igual que `ms-productos-catalogo`.
- [ ] Migrar `application-prod.yml` con el endpoint real de RDS.
