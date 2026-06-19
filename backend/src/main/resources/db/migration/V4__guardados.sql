CREATE TABLE guardados (
  id BIGSERIAL PRIMARY KEY,
  id_usuario BIGINT REFERENCES Usuario(id),
  id_post BIGINT NOT NULL,
  tipo_post VARCHAR(50) NOT NULL CHECK (tipo_post IN ('VENTA', 'INTERCAMBIO'))
);
