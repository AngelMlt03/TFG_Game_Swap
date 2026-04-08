-- 1. Tabla de Usuarios
CREATE TABLE Usuario (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    n_usuario VARCHAR(100) UNIQUE NOT NULL,
    fecha_nacimiento DATE,
    saldo DOUBLE PRECISION DEFAULT 0.0,
    correo VARCHAR(255) UNIQUE NOT NULL,
    estrellas DOUBLE PRECISION DEFAULT 0.0
);

-- 2. Tabla de Productos (Maestra)
CREATE TABLE Producto (
    id SERIAL PRIMARY KEY,
    id_API INTEGER NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    estado VARCHAR(50) CHECK (estado IN ('NUEVO', 'USADO'))
);

-- 3. Historial: Compras / Ventas
CREATE TABLE CompraVenta (
    id SERIAL PRIMARY KEY,
    id_comprador INTEGER REFERENCES Usuario(id),
    id_vendedor INTEGER REFERENCES Usuario(id),
    id_producto INTEGER REFERENCES Producto(id),
    precio DOUBLE PRECISION NOT NULL,
    fecha DATE DEFAULT CURRENT_DATE
);

-- 4. Historial: Intercambio
CREATE TABLE Intercambio (
    id SERIAL PRIMARY KEY,
    id_producto INTEGER REFERENCES Producto(id),
    id_cambio INTEGER REFERENCES Producto(id),
    id_usuario_producto INTEGER REFERENCES Usuario(id),
    id_usuario_cambio INTEGER REFERENCES Usuario(id),
    fecha DATE DEFAULT CURRENT_DATE
);

-- 5. Posts: Post Ventas
CREATE TABLE PostVenta (
    id SERIAL PRIMARY KEY,
    id_vendedor INTEGER REFERENCES Usuario(id),
    id_producto INTEGER REFERENCES Producto(id),
    precio DOUBLE PRECISION NOT NULL
);

-- 6. Posts: Post Intercambio
CREATE TABLE PostIntercambio (
    id SERIAL PRIMARY KEY,
    id_usuario INTEGER REFERENCES Usuario(id),
    id_producto INTEGER REFERENCES Producto(id),
    id_producto_cambio INTEGER REFERENCES Producto(id)
);

-- 7. Review
CREATE TABLE Review (
    id SERIAL PRIMARY KEY,
    id_reviewer INTEGER REFERENCES Usuario(id),
    id_reviewed INTEGER REFERENCES Usuario(id),
    contenido TEXT,
    estrellas DOUBLE PRECISION CHECK (estrellas >= 0 AND estrellas <= 5)
);

-- 8. Carrito
CREATE TABLE Carrito (
    id SERIAL PRIMARY KEY,
    id_usuario INTEGER REFERENCES Usuario(id),
    coste DOUBLE PRECISION DEFAULT 0.0
);

-- 9. Productos en el Carrito (Tabla intermedia)
CREATE TABLE ProductoCarrito (
    id SERIAL PRIMARY KEY,
    id_producto INTEGER REFERENCES Producto(id),
    id_carrito INTEGER REFERENCES Carrito(id)
);