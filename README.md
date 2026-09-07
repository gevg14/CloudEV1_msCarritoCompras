# Pedidos360 - Microservicio de Carrito de Compras (ms-carrito-compras)

Este microservicio gestiona la lógica del carrito de compras, la adición/eliminación de ítems y la confirmación de la compra dentro de la arquitectura de Pedidos360.

## 🛠️ Tecnologías Utilizadas

- Java 17
- Spring Boot 3.3.x
- Spring Security (OAuth2 Resource Server)
- Spring Data JPA
- H2 Database (Base de datos en memoria para desarrollo)
- Maven

## 🔐 Seguridad e Integración

- Autenticación: Microsoft Entra ID (Azure AD).
- Validación JWT: Implementación personalizada de JwtDecoder para consumo de llaves JWK y manejo de tokens v1.0/v2.0 (scp claim scope).
- CORS: Configurado para permitir peticiones desde la aplicación frontend Angular (http://localhost:4200).

## 🚀 Requisitos e Instalación

### Requisitos previos
- JDK 17 instalado
- Maven (o el ejecutable wrapper ./mvnw incluido)

### Ejecución local

1. Clonar el repositorio:
   git clone <URL_DEL_REPOSITORIO_BACKEND>
   cd ms-carrito-compras

2. Ejecutar la aplicación:
   ./mvnw spring-boot:run

   *El servicio iniciará en el puerto 8082.*

## 📌 Endpoints Principales

- GET /api/carrito - Obtiene o crea el carrito activo del usuario autenticado.
- POST /api/carrito/items - Añade un producto al carrito.
- DELETE /api/carrito/items/{itemId} - Elimina un ítem del carrito.
- POST /api/carrito/confirmar - Cambia el estado del carrito de ABIERTO a CONFIRMADO.
- GET /h2-console - Consola interactiva de base de datos H2 (jdbc:h2:mem:carritodb).
