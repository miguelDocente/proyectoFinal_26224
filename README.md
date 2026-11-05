# API Techlab

API REST para la gestión de productos de una tienda, desarrollada con Spring Boot. Los datos se guardan en una base MySQL.

## Tecnologías

- Java 17
- Spring Boot 4.1.2 (Spring Web MVC)
- Spring Data JPA / Hibernate
- MySQL
- Lombok
- Maven (incluye Maven Wrapper)

## Requisitos previos

- JDK 17 o superior
- MySQL en ejecución en `localhost:3306`
- Un cliente HTTP para probar los endpoints (por ejemplo, la extensión REST Client de VS Code)

## Estructura del proyecto

```
src/main/java/com/techlab/api_techlab/
├── ApiTechlabApplication.java      # Clase principal
├── controller/
│   └── ProductoController.java     # Endpoints REST (/productos)
├── service/
│   └── ProductoService.java        # Lógica de negocio
├── repository/
│   ├── ProductoRepository.java     # Acceso a datos de productos
│   └── CategoriaRepository.java    # Acceso a datos de categorías
├── model/
│   ├── Producto.java               # Entidad Producto
│   └── Categoria.java              # Entidad Categoria
├── util/
│   └── Validador.java              # Validaciones de nombre, precio y stock
└── exception/
    ├── GlobalExceptionHandler.java # Manejo centralizado de errores
    ├── ProductoNoEncontradoException.java
    ├── ProductoDuplicadoException.java
    └── StockInsuficienteException.java
```

La aplicación sigue una arquitectura en capas: **controller → service → repository → base de datos**.

## Configuración de la base de datos

La conexión se define en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tienda
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
```

Como `ddl-auto` está en `validate`, Hibernate **no crea las tablas**: solo verifica que existan y coincidan con las entidades. Antes de iniciar la aplicación hay que crear la base y las tablas:

```sql
CREATE DATABASE tienda;
USE tienda;

CREATE TABLE categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255)
);

CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255),
    precio DECIMAL(38,2),
    stock INT NOT NULL,
    categoria_id INT,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

-- La API no tiene endpoints para categorías: se cargan directamente en la base
INSERT INTO categoria (nombre) VALUES ('Ropa'), ('Calzado'), ('Accesorios');
```

Si el usuario o la contraseña de MySQL son otros, se modifican en `application.properties`.

## Cómo ejecutar

Desde la carpeta del proyecto (`api-techlab`):

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

## Pruebas

El archivo `pruebas.http` contiene peticiones de ejemplo para la extensión **REST Client** de VS Code. Antes de usarlo, las peticiones POST y PUT deben enviar la categoría como objeto con su id (`"categoria": { "id": 1 }`) y no como texto.

Para ejecutar las pruebas de Maven:

```bash
./mvnw test
```

## Autor

Miguel Ángel Nefle
