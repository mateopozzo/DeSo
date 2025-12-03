
-- cargar datos alojados --

INSERT INTO datos_alojado (nro_doc, tipo_doc, apellido, nombre, fechanac, CUIT, posicion_iva, telefono, email, ocupacion, nacionalidad, pais, prov, localidad, cod_post, calle, nro_calle, depto, piso) VALUES
('28123456', 'DNI', 'Gómez', 'Ana María', '1980-05-15', '20281234567', 'consumidor-final', '3425550001', 'ana.gomez@mail.com', 'Comerciante', 'ARGENTINA', 'ARGENTINA', 'SANTA FE', 'Rosario', '2000', 'Bv. Oroño', '1234', 'ROSARIO', NULL),
('35987654', 'DNI', 'Pérez', 'Juan Carlos', '1995-10-22', '20359876547', 'consumidor-final', '3415550002', NULL, 'Abogado', 'ARGENTINA', 'ARGENTINA', 'BUENOS AIRES', 'La Plata', '1900', 'Calle 50', '585', 'LA PLATA', '3'),
('40543210', 'DNI', 'Rodríguez', 'Laura Sofía', '2000-02-01', NULL, 'consumidor-final', '3515550003', 'lrodriguez@mail.com', 'Estudiante', 'ARGENTINA', 'ARGENTINA', 'CÓRDOBA', 'Córdoba', '5000', 'Av. Vélez Sársfield', '300', 'CAPITAL', '10'),
('P9012345', 'PASAPORTE', 'Fernández', 'Martín Andrés', '1975-11-30', NULL, 'consumidor-final', '3875550004', NULL, 'Ingeniero', 'ARGENTINA', 'ARGENTINA', 'SALTA', 'Salta', '4400', 'Caseros', '180', 'CAPITAL', NULL),
('05112233', 'LE', 'Díaz', 'Estela Mabel', '1950-08-10', '27051122338', 'consumidor-final', '3765550005', 'e.diaz@mail.com', 'Jubilada', 'ARGENTINA', 'ARGENTINA', 'MISIONES', 'Posadas', '3300', 'San Martín', '2050', 'CAPITAL', NULL),
('32789012', 'DNI', 'Lopez', 'Federico Manuel', '1988-03-25', '20327890127', 'consumidor-final', '3425550006', 'flopez@mail.com', 'Diseñador', 'ARGENTINA', 'ARGENTINA', 'SANTA FE', 'Santa Fe', '3000', '25 de Mayo', '2800', 'LA CAPITAL', NULL),
('41345678', 'DNI', 'Martínez', 'Camila Belén', '2002-07-19', NULL, 'consumidor-final', '3435550007', NULL, 'Cocinera', 'ARGENTINA', 'ARGENTINA', 'ENTRE RÍOS', 'Paraná', '3100', 'Corrientes', '450', 'PARANÁ', NULL),
('10456789', 'LC', 'Sánchez', 'Roberto Ariel', '1960-01-05', '20104567897', 'consumidor-final', '2995550008', 'rsanchez@mail.com', 'Técnico', 'ARGENTINA', 'ARGENTINA', 'NEUQUÉN', 'Neuquén', '8300', 'Ruta 22', '3500', 'CONFLUENCIA', NULL),
('37654321', 'DNI', 'Torres', 'Valeria Gisela', '1998-09-12', '27376543218', 'consumidor-final', '2615550009', 'v.torres@mail.com', 'Periodista', 'ARGENTINA', 'ARGENTINA', 'MENDOZA', 'Mendoza', '5500', 'San Martín', '150', 'CAPITAL', '8'),
('CHL56789', 'PASAPORTE', 'Rojas', 'Benjamín Soto', '1985-04-03', NULL, 'consumidor-final', '987654321', 'b.rojas@chile.cl', 'Consultor', 'CHILENA', 'CHILE', 'SANTIAGO', 'Santiago', '8320000', 'Av. Libertador', '100', 'SANTIAGO', '1'),
('30901234', 'DNI', 'Ruiz', 'Pablo Daniel', '1983-12-28', '20309012347', 'consumidor-final', '3405550011', NULL, 'Programador', 'ARGENTINA', 'ARGENTINA', 'SANTA FE', 'Rosario', '2000', 'Jujuy', '150', 'ROSARIO', NULL),
('25876543', 'DNI', 'Herrera', 'Marina Sol', '1978-06-17', '27258765438', 'consumidor-final', '3415550012', 'mherrera@mail.com', 'Maestra', 'ARGENTINA', 'ARGENTINA', 'BUENOS AIRES', 'Tigre', '1648', 'Alvear', '900', 'TIGRE', NULL),
('42123456', 'DNI', 'Castro', 'Gonzalo Ezequiel', '2004-01-08', NULL, 'consumidor-final', '3515550013', NULL, 'Repartidor', 'ARGENTINA', 'ARGENTINA', 'CÓRDOBA', 'Villa Carlos Paz', '5152', 'Libertad', '50', 'PUNILLA', NULL),
('URY00112', 'PASAPORTE', 'Silva', 'Alicia Inés', '1965-09-09', NULL, 'consumidor-final', '99112233', 'asilva@uy.com', 'Arquitecta', 'URUGUAYA', 'URUGUAY', 'MONTEVIDEO', 'Montevideo', '11000', '18 de Julio', '1250', 'MONTEVIDEO', '4'),
('08234567', 'LE', 'Morales', 'Ricardo José', '1955-02-14', '20082345677', 'consumidor-final', '3765550015', 'rmorales@mail.com', 'Chofer', 'ARGENTINA', 'ARGENTINA', 'MISIONES', 'Eldorado', '3380', 'Av. San Martín', '100', 'ELDORADO', NULL),
('33890123', 'DNI', 'Vázquez', 'Sofía Carolina', '1990-11-05', '27338901238', 'consumidor-final', '3425550016', 'svazquez@mail.com', 'Marketing', 'ARGENTINA', 'ARGENTINA', 'SANTA FE', 'Santa Fe', '3000', 'San Jerónimo', '2400', 'LA CAPITAL', 'B'),
('43456789', 'DNI', 'Guerra', 'Diego Javier', '2005-06-20', NULL, 'consumidor-final', '3435550017', NULL, 'Músico', 'ARGENTINA', 'ARGENTINA', 'ENTRE RÍOS', 'Paraná', '3100', 'Corrientes', '450', 'PARANÁ', NULL),
('12567890', 'LC', 'Navarro', 'Elena Rita', '1962-03-10', '27125678908', 'consumidor-final', '2995550018', 'e.navarro@mail.com', 'Administrativa', 'ARGENTINA', 'ARGENTINA', 'NEUQUÉN', 'San Martín de los Andes', '8370', 'Roca', '888', 'LACAR', NULL),
('38765432', 'DNI', 'Quinteros', 'Matías Gabriel', '1999-09-01', '20387654327', 'consumidor-final', '2615550019', 'mquinteros@mail.com', 'Vendedor', 'ARGENTINA', 'ARGENTINA', 'MENDOZA', 'San Rafael', '5600', 'Av. Hipólito Yrigoyen', '200', 'SAN RAFAEL', NULL),
('BRA87654', 'PASAPORTE', 'Oliveira', 'Ricardo Luiz', '1982-07-29', NULL, 'consumidor-final', '5511987654321', 'r.oliveira@brasil.com', 'Gerente', 'BRASILEÑA', 'BRASIL', 'SAO PAULO', 'Sao Paulo', '01310-200', 'Rua Augusta', '250', 'SAO PAULO', '2'),
('31112233', 'DNI', 'Acosta', 'Luisa Fernanda', '1986-04-14', '27311122338', 'consumidor-final', '3405550021', 'lacosta@mail.com', 'Psicóloga', 'ARGENTINA', 'ARGENTINA', 'SANTA FE', 'Santa Fe', '3000', 'Rivadavia', '3100', 'LA CAPITAL', NULL),
('26443322', 'DNI', 'Bustos', 'Nicolás Ariel', '1979-11-20', '20264433227', 'consumidor-final', '3415550022', NULL, 'Contador', 'ARGENTINA', 'ARGENTINA', 'BUENOS AIRES', 'Mar del Plata', '7600', 'Av. Luro', '4500', 'GENERAL PUEYRREDÓN', NULL),
('44887766', 'DNI', 'Herrera', 'Milagros Lucía', '2006-03-03', NULL, 'consumidor-final', '3515550023', 'mherrera2@mail.com', 'Estudiante', 'ARGENTINA', 'ARGENTINA', 'CÓRDOBA', 'Córdoba', '5000', 'Bv. Chacabuco', '10', 'CAPITAL', '1'),
('ESP10987', 'PASAPORTE', 'García', 'Pedro Jesús', '1970-12-05', NULL, 'consumidor-final', '34600112233', 'p.garcia@es.com', 'Empresario', 'ESPAÑOLA', 'ESPAÑA', 'MADRID', 'Madrid', '28001', 'Gran Vía', '60', 'DISTRITO CENTRAL', '5'),
('06554433', 'LE', 'Luna', 'Cecilia Inés', '1958-01-27', '27065544338', 'consumidor-final', '3765550025', NULL, 'Ama de casa', 'ARGENTINA', 'ARGENTINA', 'MISIONES', 'Puerto Iguazú', '3370', 'Av. Victoria Aguirre', '350', 'IGUAZÚ', NULL),
('34556677', 'DNI', 'Ramos', 'Javier Ignacio', '1992-08-11', '20345566777', 'consumidor-final', '3425550026', 'jramos@mail.com', 'Electricista', 'ARGENTINA', 'ARGENTINA', 'SANTA FE', 'Rosario', '2000', 'San Juan', '1350', 'ROSARIO', NULL),
('45998877', 'DNI', 'Vidal', 'Florencia Ana', '2003-05-29', NULL, 'consumidor-final', '3435550027', 'fvidal@mail.com', 'Artesana', 'ARGENTINA', 'ARGENTINA', 'ENTRE RÍOS', 'Concordia', '3200', 'Entre Ríos', '100', 'CONCORDIA', NULL),
('13665544', 'LC', 'Zárate', 'Héctor Raúl', '1968-07-04', '20136655447', 'consumidor-final', '2995550028', NULL, 'Jardinero', 'ARGENTINA', 'ARGENTINA', 'NEUQUÉN', 'Cutral Có', '8318', 'Av. del Trabajo', '700', 'CONFLUENCIA', NULL),
('39221100', 'DNI', 'Paz', 'Elena Micaela', '2001-10-17', '27392211008', 'consumidor-final', '2615550029', 'epaz@mail.com', 'Fotógrafa', 'ARGENTINA', 'ARGENTINA', 'MENDOZA', 'Godoy Cruz', '5501', 'Av. San Martín', '1500', 'CAPITAL', 'C'),
('MEX43210', 'PASAPORTE', 'López', 'Alejandro Gael', '1994-01-25', NULL, 'consumidor-final', '525512345678', 'a.lopez@mex.com', 'Chef', 'MEXICANA', 'MÉXICO', 'CIUDAD DE MÉXICO', 'CDMX', '06000', 'Paseo de la Reforma', '10', 'CUAUHTÉMOC', '12');

-- cargar datos alojado --

INSERT INTO alojado (tipo_alojado, tipo_doc, nro_doc, razon_social) VALUES
('Huesped', 'DNI', '28123456', NULL),
('Invitado', 'DNI', '35987654', NULL),
('Huesped', 'DNI', '40543210', NULL),
('Huesped', 'PASAPORTE', 'P9012345', NULL),
('Invitado', 'LE', '05112233', NULL),
('Huesped', 'DNI', '32789012', NULL),
('Huesped', 'DNI', '41345678', NULL),
('Invitado', 'LC', '10456789', NULL),
('Huesped', 'DNI', '37654321', NULL),
('Invitado', 'PASAPORTE', 'CHL56789', 'Gira Sudamericana'),
('Huesped', 'DNI', '30901234', NULL),
('Invitado', 'DNI', '25876543', NULL),
('Huesped', 'DNI', '42123456', NULL),
('Invitado', 'PASAPORTE', 'URY00112', 'Congreso IT Montevideo'),
('Huesped', 'LE', '08234567', NULL),
('Invitado', 'DNI', '33890123', NULL),
('Huesped', 'DNI', '43456789', NULL),
('Invitado', 'LC', '12567890', NULL),
('Huesped', 'DNI', '38765432', NULL),
('Invitado', 'PASAPORTE', 'BRA87654', 'Tour Carnaval Rio'),
('Huesped', 'DNI', '31112233', NULL),
('Invitado', 'DNI', '26443322', NULL),
('Huesped', 'DNI', '44887766', NULL),
('Invitado', 'PASAPORTE', 'ESP10987', 'Delegación Europea'),
('Invitado', 'LE', '06554433', NULL),
('Huesped', 'DNI', '34556677', NULL),
('Huesped', 'DNI', '45998877', NULL),
('Invitado', 'LC', '13665544', NULL),
('Huesped', 'DNI', '39221100', NULL),
('Invitado', 'PASAPORTE', 'MEX43210', 'Equipo Gastronómico');


select * from check_in
select * from alojado_lista_estadias
select * from alojado_lista_reservas
select * from estadia
select * from estadia_lista_habitaciones
select * from estadia_lista_servicios
select * from habitacion
select * from reserva
select * from reserva_lista_habitaciones
select * from servicio

-- cargar habitaciones --

INSERT INTO habitacion (nro_hab, capacidad, estado_hab, tarifa, tipo_hab) VALUES
(101, 1, 0, 50800.00, 0),
(102, 1, 0, 50800.00, 0),
(103, 2, 0, 70230.00, 1),
(201, 2, 0, 90560.00, 2),
(202, 2, 0, 90560.00, 2),
(203, 5, 0, 110500.00, 3),
(301, 2, 0, 128600.00, 4),
(302, 2, 0, 128600.00, 4);













