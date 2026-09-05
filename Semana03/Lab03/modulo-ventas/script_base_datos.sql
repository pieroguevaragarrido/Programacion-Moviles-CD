-- =========================================================
-- Script de creación de la base de datos: Módulo de Ventas
-- =========================================================

CREATE DATABASE IF NOT EXISTS modulo_ventas;
USE modulo_ventas;

-- No es obligatorio crear las tablas manualmente: con
-- spring.jpa.hibernate.ddl-auto=update Hibernate las genera
-- solas a partir de las entidades la primera vez que ejecutes
-- la aplicación. Este script queda como referencia / respaldo.

CREATE TABLE IF NOT EXISTS categoria (
    id_categoria BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS producto (
    id_producto BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    precio DOUBLE NOT NULL,
    stock INT NOT NULL,
    id_categoria BIGINT,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (id_categoria)
        REFERENCES categoria(id_categoria)
);

CREATE TABLE IF NOT EXISTS cliente (
    id_cliente BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS empleado (
    id_empleado BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    cargo VARCHAR(100),
    telefono VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS venta (
    id_venta BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    total DOUBLE NOT NULL,
    id_cliente BIGINT,
    id_empleado BIGINT,
    CONSTRAINT fk_venta_cliente FOREIGN KEY (id_cliente)
        REFERENCES cliente(id_cliente),
    CONSTRAINT fk_venta_empleado FOREIGN KEY (id_empleado)
        REFERENCES empleado(id_empleado)
);

CREATE TABLE IF NOT EXISTS detalle_venta (
    id_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_venta BIGINT NOT NULL,
    id_producto BIGINT NOT NULL,
    cantidad INT NOT NULL,
    precio DOUBLE NOT NULL,
    subtotal DOUBLE NOT NULL,
    CONSTRAINT fk_detalle_venta FOREIGN KEY (id_venta)
        REFERENCES venta(id_venta),
    CONSTRAINT fk_detalle_producto FOREIGN KEY (id_producto)
        REFERENCES producto(id_producto)
);
