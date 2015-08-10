CREATE DATABASE panaderiabsas;

use panaderiabsas;

CREATE TABLE articulos(
id INT auto_increment,
codigo VARCHAR(14),
nombre VARCHAR(45), 
tipo VARCHAR(15), -- Pesable o No pesable
precio_compra FLOAT DEFAULT 0,
precio FLOAT DEFAULT 0,
stock INT,
min_stock INT,
descripcion VARCHAR (45),
PRIMARY KEY(id));

CREATE TABLE ventas(
id INT AUTO_INCREMENT,
monto FLOAT,
fecha DATE,
PRIMARY KEY(id));

CREATE TABLE articulos_ventas(
id INT AUTO_INCREMENT,
venta_id INT,
articulo_id INT,
cantidad_articulo FLOAT,
monto_articulo FLOAT,
PRIMARY KEY(id));

CREATE TABLE demos(
id INT,
fecha DATE,
PRIMARY KEY(id));

insert into demos values (2,'2015-08-14')

