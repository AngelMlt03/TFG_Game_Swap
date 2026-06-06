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
```

También se debe configurar las credenciales necesarias para los servicios externos utilizados por la aplicación:
```properties
IGDB_CLIENT_ID
IGDB_CLIENT_SECRET
STRIPE_SECRET_KEY
FRONTEND_URL
```
Posteriormente se compila el backend ejecutando:

```bash
$ mvn clean install
```

Y para iniciar el backend se ejecuta este comando:

```bash
$ mvn spring-boot
```

Por defecto, el backend desplegado estará disponible en http://localhost:8080.
Las migraciones de base de datos se ejecutan automáticamente mediante Flyway durante el arranque de la aplicación, por lo que no es necesario hacer nada.
