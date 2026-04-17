-- V3: Se añade la columna rol_usuario y password a la tabla Usuario

ALTER TABLE Usuario
ADD COLUMN rol VARCHAR(20) DEFAULT 'CLIENTE';

ALTER TABLE Usuario
ADD CONSTRAINT chk_usuario_rol CHECK (rol IN ('ADMIN', 'CLIENTE'));

ALTER TABLE Usuario
ADD COLUMN password VARCHAR(255);
-- '$2a$10$bKGDwSWcxsXf4tngOy3X3uHgp9xR4d4lpkcd2o15XVjIIWQ6bUeia'; -> "1234"

-- Datos de prueba

INSERT INTO usuario (id, correo, estrellas, fecha_nacimiento, nombre, n_usuario, saldo, rol, password)
VALUES
(1, 'victor@gmail.com', 2, CURRENT_DATE, 'Victor', 'Victor02', 90, 'CLIENTE', '$2a$10$bKGDwSWcxsXf4tngOy3X3uHgp9xR4d4lpkcd2o15XVjIIWQ6bUeia'),
(2, 'andres@gmail.com', 3, CURRENT_DATE, 'Andrés', 'Andrés03', 110, 'CLIENTE', '$2a$10$bKGDwSWcxsXf4tngOy3X3uHgp9xR4d4lpkcd2o15XVjIIWQ6bUeia'),
(3, 'admin@gmail.com', 5, CURRENT_DATE, 'Admin', 'Admin01', 9999, 'ADMIN', '$2a$10$bKGDwSWcxsXf4tngOy3X3uHgp9xR4d4lpkcd2o15XVjIIWQ6bUeia');

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

-- Sincronizar la secuencia del ID con los datos insertados
CREATE SEQUENCE IF NOT EXISTS "usuario_id_seq" START WITH 4;
ALTER SEQUENCE "usuario_id_seq" RESTART WITH 4;