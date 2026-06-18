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

-- INSERTS DE PRUEBA

INSERT INTO Review (id_reviewer,id_reviewed,contenido,estrellas,tipo_review,id_compra_venta)
VALUES
(2,1,'Todo perfecto, envío rápido y producto impecable.',5,'VENTA',1);

INSERT INTO Review (id_reviewer,id_reviewed,contenido,estrellas,tipo_review,id_intercambio)
VALUES
(3,1,'Intercambio muy fácil y buena comunicación.',4.5,'INTERCAMBIO',1);