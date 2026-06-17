CREATE TABLE guardados (
  id BIGSERIAL PRIMARY KEY,
  id_usuario BIGINT REFERENCES Usuario(id),
  id_post BIGINT NOT NULL,
  tipo_post VARCHAR(50) NOT NULL CHECK (tipo_post IN ('VENTA', 'INTERCAMBIO'))
);


INSERT INTO guardados (id_usuario, id_post, tipo_post)
VALUES
(1, 1, 'VENTA'),
(1, 2, 'VENTA'),
(3, 1, 'VENTA'),
(3, 3, 'VENTA');

INSERT INTO guardados (id_usuario, id_post, tipo_post)
VALUES
(1, 1, 'INTERCAMBIO'),
(1, 2, 'INTERCAMBIO'),
(3, 1, 'INTERCAMBIO'),
(3, 2, 'INTERCAMBIO');