# Backend – Game Swap

Backend de la aplicación web de compra, venta e intercambio de videojuegos físicos.

Desarrollado con Spring Boot siguiendo una arquitectura REST.

## Tecnologías
- Java 21.0.10 LTS
- Spring Boot 3
- Maven 3.9.12
- Spring Data JPA (Hibernate)
- Spring Security + JWT
- PostgreSQL

## Configuración
La aplicación utiliza variables de entorno para la conexión a la base de datos.

Ejemplo de configuración (`application.properties`):

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tfg
spring.datasource.username=usuario
spring.datasource.password=password
jwt.secret=secret
