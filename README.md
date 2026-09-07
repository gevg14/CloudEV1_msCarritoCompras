# Pedidos360 - Microservicio de Carrito de Compras (`ms-carrito-compras`)

Este microservicio gestiona la lógica del carrito de compras, la adición/eliminación de items y la confirmación de la compra en la arquitectura del sistema **Pedidos360**.

## 🛠️ Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.3.x**
- **Spring Security** (OAuth2 Resource Server)
- **Spring Data JPA**
- **H2 Database** (Base de datos en memoria para desarrollo)
- **Maven**

## 🔐 Seguridad e Integración

- **Autenticación:** Microsoft Entra ID (Azure AD).
- **Validación JWT:** Implementación personalizada de `JwtDecoder` para consumo de llaves JWK y manejo de tokens `v1.0`/`v2.0` (`scp` claim scope).
- **CORS:** Configurado para permitir peticiones desde la aplicación frontend Angular (`http://localhost:4200`).

## 🚀 Requisitos e Instalación

### Requisitos previos
- JDK 17 instalado
- Maven (opcional, se puede usar el wrapper incluido `./mvnw`)

### Ejecución local

1. Clonar el repositorio:
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd ms-carrito-compras
