-- V6__reviews.sql

ALTER TABLE Review
ADD COLUMN tipo_review VARCHAR(20);

ALTER TABLE Review
ADD COLUMN id_compra_venta BIGINT;

ALTER TABLE Review
ADD COLUMN id_intercambio BIGINT;

-- FOREIGN KEYS

ALTER TABLE Review
ADD CONSTRAINT fk_review_compra
FOREIGN KEY (id_compra_venta)
REFERENCES Compra_Venta(id);

ALTER TABLE Review
ADD CONSTRAINT fk_review_intercambio
FOREIGN KEY (id_intercambio)
REFERENCES Intercambio(id);
