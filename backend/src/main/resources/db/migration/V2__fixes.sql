-- V2: Cambios sobre el modelo inicial

-- 1. PostVenta -> añadir estado
ALTER TABLE Post_Venta
ADD COLUMN estado VARCHAR(50) DEFAULT 'ACTIVO';

ALTER TABLE Post_Venta
ADD CONSTRAINT chk_post_venta_estado
CHECK (estado IN ('ACTIVO', 'FINALIZADO'));


-- 2. PostIntercambio -> añadir estado
ALTER TABLE Post_Intercambio
ADD COLUMN estado VARCHAR(50) DEFAULT 'ACTIVO';

ALTER TABLE Post_Intercambio
ADD CONSTRAINT chk_post_intercambio_estado
CHECK (estado IN ('ACTIVO', 'FINALIZADO'));


-- 3. CompraVenta -> cambiar estructura

-- eliminar FK antigua (si existe)
ALTER TABLE Compra_Venta DROP COLUMN id_vendedor;
ALTER TABLE Compra_Venta DROP COLUMN id_producto;

-- añadir nueva FK
ALTER TABLE Compra_Venta
ADD COLUMN id_post_venta BIGINT REFERENCES Post_Venta(id);


-- 4. Intercambio -> cambiar estructura

-- eliminar columnas antiguas
ALTER TABLE Intercambio DROP COLUMN id_producto;
ALTER TABLE Intercambio DROP COLUMN id_cambio;
ALTER TABLE Intercambio DROP COLUMN id_usuario_producto;

-- añadir nuevas columnas
ALTER TABLE Intercambio
ADD COLUMN id_post_intercambio BIGINT REFERENCES Post_Intercambio(id);


-- 5. ProductoCarrito -> cambiar producto por postVenta

ALTER TABLE Producto_Carrito DROP COLUMN id_producto;

ALTER TABLE Producto_Carrito
ADD COLUMN id_post_venta BIGINT REFERENCES Post_Venta(id);