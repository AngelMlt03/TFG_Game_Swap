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

-- 3. Posts: Post Ventas
CREATE TABLE PostVenta (
    id SERIAL PRIMARY KEY,
    id_vendedor INTEGER REFERENCES Usuario(id),
    id_producto INTEGER REFERENCES Producto(id),
    estado VARCHAR(50) CHECK (estado IN ('ACTIVO', 'FINALIZADO')),
    precio DOUBLE PRECISION NOT NULL
);

-- 4. Posts: Post Intercambio
CREATE TABLE PostIntercambio (
    id SERIAL PRIMARY KEY,
    id_usuario INTEGER REFERENCES Usuario(id),
    id_producto INTEGER REFERENCES Producto(id),
    estado VARCHAR(50) CHECK (estado IN ('ACTIVO', 'FINALIZADO')),
    id_producto_cambio INTEGER REFERENCES Producto(id)
);

-- 5. Historial: Compras / Ventas
CREATE TABLE CompraVenta (
    id SERIAL PRIMARY KEY,
    id_post_venta INTEGER REFERENCES PostVenta(id),
    id_comprador INTEGER REFERENCES Usuario(id),
    precio DOUBLE PRECISION NOT NULL,
    fecha DATE DEFAULT CURRENT_DATE
);

-- 6. Historial: Intercambio
CREATE TABLE Intercambio (
    id SERIAL PRIMARY KEY,
    id_post_intercambio INTEGER REFERENCES PostIntercambio(id),
    id_usuario_cambio INTEGER REFERENCES Usuario(id),
    fecha DATE DEFAULT CURRENT_DATE
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
    id_post_venta INTEGER REFERENCES PostVenta(id),
    id_carrito INTEGER REFERENCES Carrito(id)
);


INSERT INTO public.usuario
(id, correo, estrellas, fecha_nacimiento, nombre, n_usuario, saldo)
VALUES(1, 'victor@gmail.com', 2, CURRENT_DATE, 'Victor', 'Victor02', 90);
INSERT INTO public.usuario
(id, correo, estrellas, fecha_nacimiento, nombre, n_usuario, saldo)
VALUES(2, 'andres@gmail.com', 3, CURRENT_DATE, 'Andrés', 'Andrés03', 110);

INSERT INTO public.producto
(id, estado, id_api, nombre)
VALUES(1, 'NUEVO', 1, 'Tablet');
INSERT INTO public.producto
(id, estado, id_api, nombre)
VALUES(2, 'USADO', 2, 'Mesa');

INSERT INTO public.postventa
(id, estado, precio, id_producto, id_vendedor)
VALUES(1, 'ACTIVO', 30, 1, 2);
INSERT INTO public.postventa
(id, estado, precio, id_producto, id_vendedor)
VALUES(2,'FINALIZADO', 15, 1, 2);

INSERT INTO public.postintercambio
(id, estado, id_producto, id_producto_cambio, id_usuario)
VALUES(1, 'ACTIVO', 1, 2, 2);

INSERT INTO public.intercambio
(id, fecha, id_post_intercambio, id_usuario_cambio)
VALUES(1, CURRENT_DATE, 1, 2);

INSERT INTO public.compraventa
(id, fecha, precio, id_comprador, id_post_venta)
VALUES(1, CURRENT_DATE, 100, 1, 1);

INSERT INTO public.review
(id, contenido, estrellas, id_reviewed, id_reviewer)
VALUES(1, 'Muy bien', 3, 1, 2);

INSERT INTO public.carrito
(id, coste, id_usuario)
VALUES(1, 40, 1);

INSERT INTO public.productocarrito
(id, id_carrito, id_post_venta)
VALUES(1, 1, 2);
