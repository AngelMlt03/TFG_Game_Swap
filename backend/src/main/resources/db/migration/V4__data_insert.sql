-- '$2a$10$bKGDwSWcxsXf4tngOy3X3uHgp9xR4d4lpkcd2o15XVjIIWQ6bUeia'; -> "1234"

-- Datos de prueba

INSERT INTO usuario (id, correo, estrellas, fecha_nacimiento, nombre, n_usuario, saldo, rol, password)
VALUES
(1, 'victor@gmail.com', 2, '2004-08-30', 'Victor', 'Victor02', 90, 'CLIENTE', '$2a$10$bKGDwSWcxsXf4tngOy3X3uHgp9xR4d4lpkcd2o15XVjIIWQ6bUeia'),
(2, 'andres@gmail.com', 3, '2003-09-22', 'Andrés', 'Andrés03', 110, 'CLIENTE', '$2a$10$bKGDwSWcxsXf4tngOy3X3uHgp9xR4d4lpkcd2o15XVjIIWQ6bUeia'),
(3, 'admin@gmail.com', 5, '1997-04-05', 'Admin', 'Admin01', 9999, 'ADMIN', '$2a$10$bKGDwSWcxsXf4tngOy3X3uHgp9xR4d4lpkcd2o15XVjIIWQ6bUeia'),
(4, 'lucia@gmail.com', 4.5, '1995-03-12', 'Lucía', 'Lucia95', 50.0, 'CLIENTE', '$2a$10$bKGDwSWcxsXf4tngOy3X3uHgp9xR4d4lpkcd2o15XVjIIWQ6bUeia'),
(5, 'carlos@gmail.com', 3.8, '1990-07-24', 'Carlos', 'Carlos90', 120.5, 'CLIENTE', '$2a$10$bKGDwSWcxsXf4tngOy3X3uHgp9xR4d4lpkcd2o15XVjIIWQ6bUeia'),
(6, 'maria@gmail.com', 4.9, '1998-11-02', 'María', 'MariaGamer', 250.0, 'CLIENTE', '$2a$10$bKGDwSWcxsXf4tngOy3X3uHgp9xR4d4lpkcd2o15XVjIIWQ6bUeia'),
(7, 'javier@gmail.com', 2.5, '1988-01-15', 'Javier', 'JaviRetro', 15.0, 'CLIENTE', '$2a$10$bKGDwSWcxsXf4tngOy3X3uHgp9xR4d4lpkcd2o15XVjIIWQ6bUeia'),
(8, 'sofia@gmail.com', 4.2, '2001-05-30', 'Sofía', 'Sofi_Star', 85.0, 'CLIENTE', '$2a$10$bKGDwSWcxsXf4tngOy3X3uHgp9xR4d4lpkcd2o15XVjIIWQ6bUeia'),
(9, 'diego@gmail.com', 5.0, '1993-09-18', 'Diego', 'DiegoMaster', 300.0, 'CLIENTE', '$2a$10$bKGDwSWcxsXf4tngOy3X3uHgp9xR4d4lpkcd2o15XVjIIWQ6bUeia'),
(10, 'elena@gmail.com', 4.0, '1996-12-04', 'Elena', 'Elena_96', 45.0, 'CLIENTE', '$2a$10$bKGDwSWcxsXf4tngOy3X3uHgp9xR4d4lpkcd2o15XVjIIWQ6bUeia'),
(11, 'pablo@gmail.com', 3.5, '1992-04-22', 'Pablo', 'Pablo_PK', 112.0, 'CLIENTE', '$2a$10$bKGDwSWcxsXf4tngOy3X3uHgp9xR4d4lpkcd2o15XVjIIWQ6bUeia'),
(12, 'ana@gmail.com', 4.7, '1999-08-08', 'Ana', 'Ana_Play', 60.0, 'CLIENTE', '$2a$10$bKGDwSWcxsXf4tngOy3X3uHgp9xR4d4lpkcd2o15XVjIIWQ6bUeia');

INSERT INTO producto (id, estado, id_api, nombre)
VALUES
(1, 'NUEVO', 1942, 'The Witcher 3: Wild Hunt'),
(2, 'USADO', 1020, 'Grand Theft Auto V'),
(3, 'NUEVO', 119133, 'Elden Ring'),
(4, 'USADO', 119388, 'The Legend of Zelda: Tears of the Kingdom'),
(5, 'NUEVO', 236691, 'Cyberpunk 2077'),
(6, 'USADO', 25076, 'Red Dead Redemption 2'),
(7, 'NUEVO', 112875, 'God of War Ragnarök'),
(8, 'USADO', 14593, 'Hollow Knight'),
(9, 'NUEVO', 119171, 'Baldur''s Gate III'),
(10, 'USADO', 121, 'Minecraft');

INSERT INTO post_venta (id, estado, precio, id_producto, id_vendedor, plataforma, descripcion)
VALUES
(1, 'ACTIVO', 45.0, 1, 4, 'PC', 'Juego en perfecto estado'),
(2, 'ACTIVO', 35.0, 2, 5, 'PlayStation 4', 'Juego en buen estado'),
(3, 'ACTIVO', 50.0, 3, 6, 'Xbox One', 'Juego en excelente estado'),
(4, 'FINALIZADO', 40.0, 4, 7, 'Nintendo Switch', 'Juego en buen estado'),
(5, 'ACTIVO', 30.0, 5, 8, 'PC', 'Juego en perfecto estado');


INSERT INTO post_intercambio (id, estado, id_producto, id_producto_cambio, id_usuario, plataforma, plataforma_cambio, descripcion)
VALUES
(1, 'ACTIVO', 6, 4, 4, 'PC', 'Nintendo Switch', 'Intercambio por juego en buen estado'),
(2, 'ACTIVO', 7, 5, 5, 'PlayStation 4', 'Xbox One', 'Intercambio por juego en excelente estado'),
(3, 'FINALIZADO', 8, 6, 6, 'Nintendo Switch', 'PC', 'Intercambio finalizado'),
(4, 'ACTIVO', 9, 7, 7, 'Xbox One', 'PlayStation 4', 'Intercambio por juego en perfecto estado'),
(5, 'FINALIZADO', 10, 8, 8, 'PC', 'Nintendo Switch', 'Intercambio finalizado');


INSERT INTO intercambio (id, fecha, id_post_intercambio, id_usuario_cambio)
VALUES
(1, '2026-05-01', 3, 7),
(2, '2026-05-02', 5, 10);


INSERT INTO compra_venta (id, fecha, precio, id_comprador, id_post_venta)
VALUES
(1, '2026-05-01', 40.0, 5, 4);

INSERT INTO review (id, contenido, estrellas, id_reviewed, id_reviewer)
VALUES
(1, 'Muy bien', 3, 1, 2),
(2, 'Vendedor muy amable y el juego en perfecto estado.', 5.0, 4, 5),
(3, 'Todo correcto, envío rápido.', 4.0, 5, 6),
(4, 'Tardó un poco más de lo esperado pero bien.', 3.5, 6, 7),
(5, 'Muy recomendado, impecable.', 5.0, 7, 8),
(6, 'Buen trato y comunicación excelente.', 4.5, 8, 9),
(7, 'El disco venía un poco rayado.', 2.5, 9, 10),
(8, 'Excelente intercambio, sin problemas.', 5.0, 10, 2),
(9, 'Gran vendedor, muy confiable.', 4.8, 2, 8),
(10, 'Todo bien, lo recomiendo.', 4.0, 8, 4),
(11, 'Muy profesional.', 4.5, 1, 6);

INSERT INTO carrito (id, coste, id_usuario)
VALUES
(1, 40, 1),
(2, 45.0, 2),
(3, 35.0, 3),
(4, 50.0, 4),
(5, 30.0, 5);

INSERT INTO producto_carrito (id, id_carrito, id_post_venta)
VALUES
(1, 1, 1),
(2, 2, 2),
(3, 3, 3),
(4, 4, 3),
(5, 5, 5),
(6, 4, 5);

-- Sincronizar la secuencia del ID con los datos insertados
CREATE SEQUENCE IF NOT EXISTS "usuario_id_seq" START WITH 13;
ALTER SEQUENCE "usuario_id_seq" RESTART WITH 13;

CREATE SEQUENCE IF NOT EXISTS "producto_id_seq" START WITH 11;
ALTER SEQUENCE "producto_id_seq" RESTART WITH 11;

CREATE SEQUENCE IF NOT EXISTS "post_venta_id_seq" START WITH 6;
ALTER SEQUENCE "post_venta_id_seq" RESTART WITH 6;

CREATE SEQUENCE IF NOT EXISTS "post_intercambio_id_seq" START WITH 6;
ALTER SEQUENCE "post_intercambio_id_seq" RESTART WITH 6;

CREATE SEQUENCE IF NOT EXISTS "intercambio_id_seq" START WITH 11;
ALTER SEQUENCE "intercambio_id_seq" RESTART WITH 11;

CREATE SEQUENCE IF NOT EXISTS "compra_venta_id_seq" START WITH 11;
ALTER SEQUENCE "compra_venta_id_seq" RESTART WITH 11;

CREATE SEQUENCE IF NOT EXISTS "review_id_seq" START WITH 12;
ALTER SEQUENCE "review_id_seq" RESTART WITH 12;

CREATE SEQUENCE IF NOT EXISTS "carrito_id_seq" START WITH 12;
ALTER SEQUENCE "carrito_id_seq" RESTART WITH 12;

CREATE SEQUENCE IF NOT EXISTS "producto_carrito_id_seq" START WITH 12;
ALTER SEQUENCE "producto_carrito_id_seq" RESTART WITH 12;