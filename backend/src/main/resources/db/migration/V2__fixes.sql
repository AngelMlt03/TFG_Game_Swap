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