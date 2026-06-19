-- Datos de prueba para la base de datos
INSERT INTO public.usuario (nombre,n_usuario,fecha_nacimiento,saldo,correo,estrellas,rol,"password") VALUES
   ('Ángel','AngelMA','2003-09-02',406.0,'angelma@gmail.com',4.0,'CLIENTE','$2a$10$xoQuQBXtD1RbjfcbjTKFDuoVkMWtYcMZf3j6CDYgjywZngpwTfsoe'),
   ('Andrés','Carballo','1997-06-20',504.0,'andresc@gmail.com',4.3,'CLIENTE','$2a$10$x8RPiOI4FudS.U4ZglB/7e.l87Wu.An5gvwm6zRcQ2L2X5O0Rhrae'),
   ('Iker','IkerGlz','1991-07-31',20.0,'ikerg@gmail.com',4.7,'CLIENTE','$2a$10$ymAtIkrl95qmToZGDndzTOO4V9L.RPdGQToiBVDd.hdnDpEwW1G7y'),
	 ('Marcos','Marax','2003-07-22',44.0,'marcosx@gmail.com',3.1,'CLIENTE','$2a$10$xoQuQBXtD1RbjfcbjTKFDuoVkMWtYcMZf3j6CDYgjywZngpwTfsoe'),
	 ('Nacho','Nack','2004-03-12',120.0,'nack@gmail.com',3.9,'CLIENTE','$2a$10$xoQuQBXtD1RbjfcbjTKFDuoVkMWtYcMZf3j6CDYgjywZngpwTfsoe'),
	 ('Victoria','Vicky','2003-08-06',320.0,'vicky@gmail.com',1.2,'CLIENTE','$2a$10$xoQuQBXtD1RbjfcbjTKFDuoVkMWtYcMZf3j6CDYgjywZngpwTfsoe'),
	 ('Diego','DiegoRP','1994-03-01',120.0,'diegorp@gmail.com',2.7,'CLIENTE','$2a$10$xoQuQBXtD1RbjfcbjTKFDuoVkMWtYcMZf3j6CDYgjywZngpwTfsoe'),
	 ('Hugo','Hugito03','2000-02-28',20.0,'hugo@gmail.com',3.4,'CLIENTE','$2a$10$DdB0M3zlzqKcnF.dHfNaW.purUEx.9Y6Ssv7RZ9DLZzRJPg8Wz7jy'),
	 ('Adrián','AdriGG','1999-11-03',60.0,'adri@gmail.com',2.2,'CLIENTE','$2a$10$3cEQttnLN9QyzO1KJl6tUeWEbOKOXm4/Mp6wqYj1A5AZP28.mylQG'),
	 ('Ainhoa','nhoawa','2004-03-14',30.0,'ainhoa@gmail.com',3.8,'CLIENTE','$2a$10$GV1xaRA6b1efq/QUg0Sip.ypWlwvgbVOdq8w.EWbAOOTqGaseAH/m');
	 

INSERT INTO public.carrito (id_usuario,coste) VALUES
	 (1,0.0),
	 (2,0.0),
	 (3,0.0),
	 (4,0.0),
	 (5,0.0),
	 (6,0.0),
	 (7,0.0),
	 (8,0.0),
	 (9,0.0),
   (10,0.0);

INSERT INTO public.producto (id_api,nombre,estado) VALUES
	 (28204,'Call of Duty: WWII','NUEVO'),
	 (83728,'Battlefield V','SEMINUEVO'),
	 (135400,'Minecraft','USADO'),
	 (103281,'Halo Infinite','SEMINUEVO'),
	 (2350,'Mario Kart 8','USADO'),
	 (37382,'Pokémon Sword','CUALQUIERA'),
	 (3212,'The Sims 4','MUY_USADO'),
	 (8173,'Overwatch','CUALQUIERA'),
	 (7335,'Splatoon','USADO'),
	 (154986,'FIFA 22','NUEVO');
INSERT INTO public.producto (id_api,nombre,estado) VALUES
	 (205780,'FIFA 23','NUEVO'),
	 (19918,'EA Sports UFC 2','SEMINUEVO'),
	 (241492,'Mortal Kombat X','SEMINUEVO'),
	 (8258,'Street Fighter V','SEMINUEVO'),
	 (1622,'Mortal Kombat 4','CUALQUIERA'),
	 (2707,'Mario & Sonic at the Olympic Winter Games','USADO'),
	 (229246,'Super Mario Galaxy','CUALQUIERA'),
	 (119171,'Baldur''s Gate III','SEMINUEVO'),
	 (7360,'Rainbow Six Siege','CUALQUIERA'),
	 (7346,'The Legend of Zelda: Breath of the Wild','SEMINUEVO');
INSERT INTO public.producto (id_api,nombre,estado) VALUES
	 (76882,'Sekiro: Shadows Die Twice','USADO'),
	 (71,'Portal','SEMINUEVO'),
	 (7331,'Uncharted 4: A Thief''s End','NUEVO'),
	 (119133,'Elden Ring','NUEVO'),
	 (103298,'Doom Eternal','CUALQUIERA'),
	 (135400,'Minecraft','USADO'),
	 (239064,'Grand Theft Auto V','USADO'),
	 (26192,'The Last of Us Part II','CUALQUIERA'),
	 (305152,'Clair Obscur: Expedition 33','SEMINUEVO'),
	 (26226,'Celeste','NUEVO');
INSERT INTO public.producto (id_api,nombre,estado) VALUES
	 (11133,'Dark Souls III','NUEVO'),
	 (26758,'Super Mario Odyssey','CUALQUIERA'),
	 (9061,'Cuphead','SEMINUEVO'),
	 (1985,'Metal Gear Solid V: The Phantom Pain','NUEVO'),
	 (499,'God of War III','USADO'),
	 (1942,'The Witcher 3: Wild Hunt','CUALQUIERA'),
	 (305472,'Before Your Eyes','SEMINUEVO'),
	 (115289,'Hollow Knight: Silksong','USADO'),
	 (76253,'Devil May Cry 5','NUEVO'),
	 (11182,'Enter the Gungeon','CUALQUIERA');
INSERT INTO public.producto (id_api,nombre,estado) VALUES
	 (148241,'Lies of P','SEMINUEVO'),
	 (80529,'Hades','CUALQUIERA'),
	 (139090,'Inscryption','SEMINUEVO'),
	 (36926,'Monster Hunter: World','NUEVO'),
	 (144022,'Sifu','NUEVO'),
	 (19686,'Resident Evil 2','SEMINUEVO'),
	 (23248,'Frostpunk','NUEVO'),
	 (127044,'Marvel''s Spider-Man 2','NUEVO'),
	 (135243,'It Takes Two','SEMINUEVO');

INSERT INTO public.post_venta (id_vendedor,id_producto,plataforma,precio,descripcion,estado) VALUES
	 (2,4,'Xbox Series X|S',10.0,'','ACTIVO'),
	 (2,3,'PC (Microsoft Windows)',14.0,'','FINALIZADO'),
	 (2,2,'PlayStation 4',12.0,'','FINALIZADO'),
	 (1,12,'PlayStation 4',6.0,'','FINALIZADO'),
	 (1,11,'PlayStation 5',10.0,'','FINALIZADO'),
	 (1,9,'Nintendo Switch',5.0,'Juego muy usado, pero funciona perfectamente.
La caja está un poco desgastada.','FINALIZADO'),
	 (2,1,'PlayStation 4',24.0,'','FINALIZADO'),
	 (3,20,'Nintendo Switch 2',18.0,'','ACTIVO'),
	 (1,10,'PlayStation 5',7.0,'','FINALIZADO'),
	 (3,21,'PlayStation 4',14.0,'Juego con algo de uso, funciona perfectamente.','FINALIZADO');
INSERT INTO public.post_venta (id_vendedor,id_producto,plataforma,precio,descripcion,estado) VALUES
	 (3,24,'PlayStation 4',16.0,'','FINALIZADO'),
	 (1,13,'PlayStation 4',8.0,'','FINALIZADO'),
	 (3,22,'PC (Microsoft Windows)',4.0,'','FINALIZADO'),
	 (1,33,'PC (Microsoft Windows)',12.0,'','ACTIVO'),
	 (1,34,'PC (Microsoft Windows)',27.0,'','ACTIVO'),
	 (8,37,'Xbox One',7.0,'','ACTIVO'),
	 (8,38,'Xbox One',12.0,'','ACTIVO'),
	 (8,43,'Xbox Series X|S',8.0,'','ACTIVO'),
	 (8,46,'PlayStation 4',40.0,'','ACTIVO'),
	 (8,47,'PlayStation 5',24.0,'','ACTIVO');
INSERT INTO public.post_venta (id_vendedor,id_producto,plataforma,precio,descripcion,estado) VALUES
	 (8,48,'PlayStation 5',45.0,'','ACTIVO'),
	 (8,49,'PlayStation 5',24.0,'Caja y manuales en perfectas condiciones.','ACTIVO'),
	 (3,23,'PC (Microsoft Windows)',3.0,'','FINALIZADO');

INSERT INTO public.post_intercambio (id_usuario,id_producto,id_producto_cambio,plataforma,plataforma_cambio,descripcion,estado) VALUES
	 (2,5,6,'Nintendo Switch 2','Nintendo Switch 2','Juego en perfectas condiciones.
Caja original con manuales.','ACTIVO'),
	 (2,7,8,'PC (Microsoft Windows)','PC (Microsoft Windows)','','ACTIVO'),
	 (1,18,19,'PC (Microsoft Windows)','PC (Microsoft Windows)','','ACTIVO'),
	 (1,14,15,'PlayStation 5','PC (Microsoft Windows)','','FINALIZADO'),
	 (1,16,17,'Nintendo Switch','Wii','','FINALIZADO'),
	 (3,25,26,'Xbox One','Xbox One','','ACTIVO'),
	 (3,29,30,'Xbox One','PC (Microsoft Windows)','','ACTIVO'),
	 (3,31,32,'PlayStation 4','Nintendo Switch 2','','ACTIVO'),
	 (3,27,28,'PlayStation 4','PC (Microsoft Windows)','','FINALIZADO'),
	 (1,35,36,'PlayStation 4','PlayStation 4','','ACTIVO');
INSERT INTO public.post_intercambio (id_usuario,id_producto,id_producto_cambio,plataforma,plataforma_cambio,descripcion,estado) VALUES
	 (8,39,40,'Nintendo Switch','PC (Microsoft Windows)','','ACTIVO'),
	 (8,41,42,'Nintendo Switch','PC (Microsoft Windows)','','ACTIVO'),
	 (8,44,45,'Xbox Series X|S','PC (Microsoft Windows)','','ACTIVO');

INSERT INTO public.compra_venta (id_comprador,precio,fecha,id_post_venta) VALUES
	 (1,14.0,'2026-06-18',3),
	 (1,12.0,'2026-06-18',2),
	 (2,6.0,'2026-06-18',8),
	 (1,10.0,'2026-06-18',7),
	 (3,5.0,'2026-06-18',5),
	 (3,24.0,'2026-06-18',1),
	 (3,7.0,'2026-06-19',6),
	 (2,14.0,'2026-06-19',11),
	 (2,16.0,'2026-06-19',14),
	 (3,8.0,'2026-06-19',9);
INSERT INTO public.compra_venta (id_comprador,precio,fecha,id_post_venta) VALUES
	 (2,4.0,'2026-06-19',12),
	 (4,3.0,'2026-06-19',13);

INSERT INTO public.intercambio (id_usuario_cambio,fecha,id_post_intercambio) VALUES
	 (2,'2026-06-18',3),
	 (3,'2026-06-18',4),
	 (2,'2026-06-19',7);

INSERT INTO public.review (id_reviewer,id_reviewed,contenido,estrellas,tipo_review,id_compra_venta,id_intercambio) VALUES
	 (1,2,'Vendedor muy amable y envío super rápido, muy recomendable.',5.0,'VENTA',1,NULL),
	 (3,1,'El juego estaba bastante usado.',1.0,'VENTA',5,NULL),
	 (3,2,'Un poco caro para el juego que es.',3.0,'VENTA',6,NULL),
	 (3,1,'Juego en muy buenas condiciones, aunque tardó un poco en llegar.',4.0,'VENTA',7,NULL),
	 (2,1,'Juego en buenísimas condiciones, está prácticamente perfecto.',5.0,'VENTA',3,NULL),
	 (1,2,'Muy barato, buena compra y buen servicio.',5.0,'VENTA',4,NULL),
	 (2,3,'Tenía ganas de jugarlo. Un intercambio muy justo de dos juegos increíbles.',5.0,'INTERCAMBIO',NULL,3),
	 (3,1,'Precio muy rebajado, increíble trato.',5.0,'VENTA',10,NULL),
	 (2,1,'Juego muy barato.',4.0,'VENTA',11,NULL),
	 (1,2,'Ya he comprado otro juego a este usuario, pero esta vez ha tardado más de la cuenta en enviarme el juego.',4.0,'VENTA',2,NULL);
INSERT INTO public.review (id_reviewer,id_reviewed,contenido,estrellas,tipo_review,id_compra_venta,id_intercambio) VALUES
	 (1,2,'Juego en perfectas condiciones.',5.0,'INTERCAMBIO',NULL,1),
	 (1,3,'Juego en muy buenas condiciones y con la caja y manuales perfectos.',5.0,'INTERCAMBIO',NULL,2);


-- Sincronizar la secuencia del ID con los datos insertados
CREATE SEQUENCE IF NOT EXISTS "usuario_id_seq" START WITH 11;
ALTER SEQUENCE "usuario_id_seq" RESTART WITH 11;

CREATE SEQUENCE IF NOT EXISTS "producto_id_seq" START WITH 50;
ALTER SEQUENCE "producto_id_seq" RESTART WITH 50;

CREATE SEQUENCE IF NOT EXISTS "post_venta_id_seq" START WITH 24;
ALTER SEQUENCE "post_venta_id_seq" RESTART WITH 24;

CREATE SEQUENCE IF NOT EXISTS "post_intercambio_id_seq" START WITH 14;
ALTER SEQUENCE "post_intercambio_id_seq" RESTART WITH 14;

CREATE SEQUENCE IF NOT EXISTS "intercambio_id_seq" START WITH 4;
ALTER SEQUENCE "intercambio_id_seq" RESTART WITH 4;

CREATE SEQUENCE IF NOT EXISTS "compra_venta_id_seq" START WITH 13;
ALTER SEQUENCE "compra_venta_id_seq" RESTART WITH 13;

CREATE SEQUENCE IF NOT EXISTS "review_id_seq" START WITH 13;
ALTER SEQUENCE "review_id_seq" RESTART WITH 13;

CREATE SEQUENCE IF NOT EXISTS "carrito_id_seq" START WITH 11;
ALTER SEQUENCE "carrito_id_seq" RESTART WITH 11;