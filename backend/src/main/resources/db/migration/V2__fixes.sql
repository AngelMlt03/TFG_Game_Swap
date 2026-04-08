-- V2: Cambios sobre el modelo inicial

-- 1. PostVenta -> añadir estado
ALTER TABLE PostVenta
ADD COLUMN estado VARCHAR(50) DEFAULT 'ACTIVO';

ALTER TABLE PostVenta
ADD CONSTRAINT chk_postventa_estado
CHECK (estado IN ('ACTIVO', 'FINALIZADO'));


-- 2. PostIntercambio -> añadir estado
ALTER TABLE PostIntercambio
ADD COLUMN estado VARCHAR(50) DEFAULT 'ACTIVO';

ALTER TABLE PostIntercambio
ADD CONSTRAINT chk_postintercambio_estado
CHECK (estado IN ('ACTIVO', 'FINALIZADO'));


-- 3. CompraVenta -> cambiar estructura

-- eliminar FK antigua (si existe)
ALTER TABLE CompraVenta DROP COLUMN id_vendedor;
ALTER TABLE CompraVenta DROP COLUMN id_producto;

-- añadir nueva FK
ALTER TABLE CompraVenta
ADD COLUMN id_post_venta INTEGER REFERENCES PostVenta(id);


-- 4. Intercambio -> cambiar estructura

-- eliminar columnas antiguas
ALTER TABLE Intercambio DROP COLUMN id_producto;
ALTER TABLE Intercambio DROP COLUMN id_cambio;
ALTER TABLE Intercambio DROP COLUMN id_usuario_producto;

-- añadir nuevas columnas
ALTER TABLE Intercambio
ADD COLUMN id_post_intercambio INTEGER REFERENCES PostIntercambio(id);


-- 5. ProductoCarrito -> cambiar producto por postVenta

ALTER TABLE ProductoCarrito DROP COLUMN id_producto;

ALTER TABLE ProductoCarrito
ADD COLUMN id_post_venta INTEGER REFERENCES PostVenta(id);


-- 6. DATOS DE PRUEBA (opcional pero OK)

INSERT INTO usuario (id, correo, estrellas, fecha_nacimiento, nombre, n_usuario, saldo)
VALUES
(1, 'victor@gmail.com', 2, CURRENT_DATE, 'Victor', 'Victor02', 90),
(2, 'andres@gmail.com', 3, CURRENT_DATE, 'Andrés', 'Andrés03', 110);

INSERT INTO producto (id, estado, id_api, nombre)
VALUES
(1, 'NUEVO', 1, 'Tablet'),
(2, 'USADO', 2, 'Mesa');

INSERT INTO postventa (id, estado, precio, id_producto, id_vendedor)
VALUES
(1, 'ACTIVO', 30, 1, 2),
(2, 'FINALIZADO', 15, 1, 2);

INSERT INTO postintercambio (id, estado, id_producto, id_producto_cambio, id_usuario)
VALUES
(1, 'ACTIVO', 1, 2, 2);

INSERT INTO intercambio (id, fecha, id_post_intercambio, id_usuario_cambio)
VALUES
(1, CURRENT_DATE, 1, 2);

INSERT INTO compraventa (id, fecha, precio, id_comprador, id_post_venta)
VALUES
(1, CURRENT_DATE, 100, 1, 1);

INSERT INTO review (id, contenido, estrellas, id_reviewed, id_reviewer)
VALUES
(1, 'Muy bien', 3, 1, 2);

INSERT INTO carrito (id, coste, id_usuario)
VALUES
(1, 40, 1);

INSERT INTO productocarrito (id, id_carrito, id_post_venta)
VALUES
(1, 1, 2);