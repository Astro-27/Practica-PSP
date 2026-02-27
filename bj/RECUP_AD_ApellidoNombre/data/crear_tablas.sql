-- Script de creación de tablas
-- Base de datos: app.db

CREATE TABLE IF NOT EXISTS clientes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    email TEXT UNIQUE,
    empresa TEXT,
    ciudad TEXT,
    telefono TEXT,
    fecha_registro DATE,
    estado TEXT DEFAULT 'activo'
);

CREATE TABLE IF NOT EXISTS productos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    categoria TEXT,
    precio REAL NOT NULL,
    stock INTEGER,
    proveedor TEXT,
    activo BOOLEAN DEFAULT 1
);

CREATE TABLE IF NOT EXISTS transacciones (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cliente_id INTEGER NOT NULL,
    producto_id INTEGER NOT NULL,
    fecha DATE NOT NULL,
    importe REAL NOT NULL,
    cantidad INTEGER,
    estado TEXT,
    categoriaImporte TEXT,
    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);

-- Crear índices para mejor rendimiento
CREATE INDEX IF NOT EXISTS idx_cliente_id ON transacciones(cliente_id);
CREATE INDEX IF NOT EXISTS idx_producto_id ON transacciones(producto_id);
CREATE INDEX IF NOT EXISTS idx_fecha ON transacciones(fecha);
