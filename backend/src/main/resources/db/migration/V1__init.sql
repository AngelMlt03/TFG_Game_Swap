-- 1. Tabla de Usuarios
CREATE TABLE Usuario (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    n_usuario VARCHAR(100) UNIQUE NOT NULL,
    fecha_nacimiento DATE,
    saldo DOUBLE PRECISION DEFAULT 0.0,
    correo VARCHAR(255) UNIQUE NOT NULL,
    estrellas DOUBLE PRECISION DEFAULT 0.0
);

-- 2. Tabla de Productos (Maestra)
CREATE TABLE Producto (
    id BIGSERIAL PRIMARY KEY,
    id_API BIGINT NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    estado VARCHAR(50) CHECK (estado IN ('NUEVO', 'USADO'))
);

-- 3. Historial: Compras / Ventas
CREATE TABLE CompraVenta (
    id BIGSERIAL PRIMARY KEY,
    id_comprador BIGINT REFERENCES Usuario(id),
    id_vendedor BIGINT REFERENCES Usuario(id),
    id_producto BIGINT REFERENCES Producto(id),
    precio DOUBLE PRECISION NOT NULL,
    fecha DATE DEFAULT CURRENT_DATE
);

-- 4. Historial: Intercambio
CREATE TABLE Intercambio (
    id BIGSERIAL PRIMARY KEY,
    id_producto BIGINT REFERENCES Producto(id),
    id_cambio BIGINT REFERENCES Producto(id),
    id_usuario_producto BIGINT REFERENCES Usuario(id),
    id_usuario_cambio BIGINT REFERENCES Usuario(id),
    fecha DATE DEFAULT CURRENT_DATE
);

-- 5. Posts: Post Ventas
CREATE TABLE PostVenta (
    id BIGSERIAL PRIMARY KEY,
    id_vendedor BIGINT REFERENCES Usuario(id),
    id_producto BIGINT REFERENCES Producto(id),
    precio DOUBLE PRECISION NOT NULL
);

-- 6. Posts: Post Intercambio
CREATE TABLE PostIntercambio (
    id BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT REFERENCES Usuario(id),
    id_producto BIGINT REFERENCES Producto(id),
    id_producto_cambio BIGINT REFERENCES Producto(id)
);

-- 7. Review
CREATE TABLE Review (
    id BIGSERIAL PRIMARY KEY,
    id_reviewer BIGINT REFERENCES Usuario(id),
    id_reviewed BIGINT REFERENCES Usuario(id),
    contenido TEXT,
    estrellas DOUBLE PRECISION CHECK (estrellas >= 0 AND estrellas <= 5)
);

-- 8. Carrito
CREATE TABLE Carrito (
    id BIGSERIAL PRIMARY KEY,
    id_usuario BIGINT REFERENCES Usuario(id),
    coste DOUBLE PRECISION DEFAULT 0.0
);

-- 9. Productos en el Carrito (Tabla intermedia)
CREATE TABLE ProductoCarrito (
    id BIGSERIAL PRIMARY KEY,
    id_producto BIGINT REFERENCES Producto(id),
    id_carrito BIGINT REFERENCES Carrito(id)
);