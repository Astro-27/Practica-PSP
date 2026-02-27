import sqlite3

def conectarBD():
    try:
        conexion = sqlite3.connect('data/app.db')
        return conexion
    except sqlite3.Error as e:
        print(f"❌ Error de conexión: {e}")
        return None

def crearTablas(conexion):
    try:
        cursor = conexion.cursor()
        
        with open('data/crear_tablas.sql', 'r', encoding='utf-8') as archivo:
            sqlScript = archivo.read()
        
        cursor.executescript(sqlScript)
        conexion.commit()
        print("✓ Tablas creadas correctamente")
        return True
    except sqlite3.Error as e:
        print(f"❌ Error al crear tablas: {e}")
        return False

def insertarClientes(conexion):
    clientes = [
        (1, 'Juan García López', 'juan@email.com', 'Tech Solutions', 'Madrid', '+34 912345678', '2023-06-15', 'activo'),
        (2, 'María Rodríguez', 'maria@email.com', 'Global Consulting', 'Barcelona', '+34 934567890', '2023-09-22', 'activo'),
        (3, 'Carlos Fernández', 'carlos@email.com', 'Innovate Systems', 'Sevilla', '+34 956234567', '2024-01-10', 'activo'),
        (4, 'Patricia López', 'patricia@email.com', 'Digital Ventures', 'Valencia', '+34 871345678', '2023-11-05', 'inactivo'),
        (5, 'Roberto Sánchez', 'roberto@email.com', 'Enterprise Pro', 'Madrid', '+34 913456789', '2024-02-14', 'activo'),
    ]
    
    try:
        cursor = conexion.cursor()
        cursor.executemany(
            'INSERT OR IGNORE INTO clientes VALUES (?, ?, ?, ?, ?, ?, ?, ?)',
            clientes
        )
        conexion.commit()
        print(f"✓ {cursor.rowcount} clientes insertados")
    except sqlite3.Error as e:
        print(f"❌ Error al insertar clientes: {e}")

def insertarProductos(conexion):
    productos = [
        (1, 'Laptop Dell XPS 15', 'Electrónica', 1500.00, 25, 'Dell Inc.', 1),
        (2, 'Mouse Logitech MX', 'Accesorios', 99.99, 150, 'Logitech', 1),
        (3, 'Teclado Mecánico RGB', 'Accesorios', 250.50, 45, 'Corsair', 1),
        (4, 'Monitor LG 27 Pulgadas', 'Monitores', 350.00, 30, 'LG Electronics', 1),
        (5, 'SSD Samsung 1TB', 'Almacenamiento', 180.75, 85, 'Samsung', 1),
    ]
    
    try:
        cursor = conexion.cursor()
        cursor.executemany(
            'INSERT OR IGNORE INTO productos VALUES (?, ?, ?, ?, ?, ?, ?)',
            productos
        )
        conexion.commit()
        print(f"✓ {cursor.rowcount} productos insertados")
    except sqlite3.Error as e:
        print(f"❌ Error al insertar productos: {e}")

def insertarTransacciones(conexion):
    transacciones = [
        (1, 1, 1, '2024-01-15', 1500.00, 1, 'completado', 'alto'),
        (2, 2, 2, '2024-01-18', 99.99, 2, 'completado', 'bajo'),
        (3, 3, 3, '2024-01-20', 250.50, 1, 'pendiente', 'medio'),
        (4, 1, 4, '2024-02-01', 350.00, 1, 'completado', 'medio'),
        (5, 4, 5, '2024-02-05', 180.75, 2, 'cancelado', 'medio'),
        (6, 5, 1, '2024-02-10', 1500.00, 1, 'completado', 'alto'),
        (7, 2, 3, '2024-02-15', 250.50, 1, 'completado', 'medio'),
        (8, 3, 2, '2024-02-20', 199.98, 2, 'pendiente', 'medio'),
        (9, 1, 5, '2024-03-01', 360.00, 2, 'completado', 'medio'),
        (10, 5, 4, '2024-03-05', 350.00, 1, 'completado', 'medio'),
    ]
    
    try:
        cursor = conexion.cursor()
        cursor.executemany(
            'INSERT OR IGNORE INTO transacciones VALUES (?, ?, ?, ?, ?, ?, ?, ?)',
            transacciones
        )
        conexion.commit()
        print(f"✓ {cursor.rowcount} transacciones insertadas")
    except sqlite3.Error as e:
        print(f"❌ Error al insertar transacciones: {e}")

def mostrarEstadisticas(conexion):
    try:
        cursor = conexion.cursor()
        
        cursor.execute("SELECT COUNT(*) FROM clientes")
        numClientes = cursor.fetchone()[0]
        
        cursor.execute("SELECT COUNT(*) FROM productos")
        numProductos = cursor.fetchone()[0]
        
        cursor.execute("SELECT COUNT(*) FROM transacciones")
        numTransacciones = cursor.fetchone()[0]
        
        print("\n" + "="*50)
        print("ESTADISTICAS DE LA BASE DE DATOS:")
        print("="*50)
        print(f"Clientes: {numClientes}")
        print(f"Productos: {numProductos}")
        print(f"Transacciones: {numTransacciones}")
        print("="*50 + "\n")
    except sqlite3.Error as e:
        print(f"❌ Error al obtener estadísticas: {e}")

print("Inicializando base de datos SQLite...\n")

conexion = conectarBD()

if conexion:
    if crearTablas(conexion):
        insertarClientes(conexion)
        insertarProductos(conexion)
        insertarTransacciones(conexion)
        mostrarEstadisticas(conexion)
        conexion.close()
        print("✓ Base de datos inicializada correctamente")
    else:
        conexion.close()
else:
    print("❌ No se pudo conectar a la base de datos")
