-- V7__carrito.sql

DELETE FROM Producto_Carrito;
DELETE FROM Carrito;

DROP TABLE IF EXISTS Producto_Carrito;
DROP TABLE IF EXISTS Carrito;

CREATE TABLE Carrito (
  id BIGSERIAL PRIMARY KEY,
  id_usuario BIGINT NOT NULL,

  CONSTRAINT fk_carrito_usuario FOREIGN KEY (id_usuario) REFERENCES Usuario(id)
);

CREATE TABLE Producto_Carrito (
  id BIGSERIAL PRIMARY KEY,
  id_carrito BIGINT NOT NULL,
  id_post_venta BIGINT NOT NULL,

  CONSTRAINT fk_producto_carrito_carrito FOREIGN KEY (id_carrito) REFERENCES Carrito(id) ON DELETE CASCADE,
  CONSTRAINT fk_producto_carrito_post FOREIGN KEY (id_post_venta) REFERENCES Post_Venta(id) ON DELETE CASCADE
);

-- INSERTS

INSERT INTO Carrito (id_usuario)
VALUES (1);

INSERT INTO Producto_Carrito (id_carrito,id_post_venta)
VALUES
(1, 1),
(1, 2);