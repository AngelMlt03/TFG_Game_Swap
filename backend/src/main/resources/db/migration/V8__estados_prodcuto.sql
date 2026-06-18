ALTER TABLE producto
DROP CONSTRAINT producto_estado_check;

ALTER TABLE producto
ADD CONSTRAINT producto_estado_check
CHECK (
    estado IN (
        'CUALQUIERA',
        'NUEVO',
        'SEMINUEVO',
        'USADO',
        'MUY_USADO'
    )
);