-- V3: Se añade la columna rol_usuario y password a la tabla Usuario

ALTER TABLE Usuario
ADD COLUMN rol VARCHAR(20) DEFAULT 'CLIENTE';

ALTER TABLE Usuario
ADD CONSTRAINT chk_usuario_rol CHECK (rol IN ('ADMIN', 'CLIENTE'));

ALTER TABLE Usuario
ADD COLUMN password VARCHAR(255);
