import csv
from datetime import datetime

def convertirFecha(fechaStr):
    formatos = ['%d/%m/%Y', '%Y-%m-%d', '%d-%m-%Y', '%Y/%m/%d']
    for fmt in formatos:
        try:
            objDate = datetime.strptime(fechaStr.strip(), fmt)
            return objDate.strftime('%Y-%m-%d')
        except ValueError:
            continue
    return fechaStr

def limpiarImporte(importeStr):
    numLimpio = importeStr.strip().replace('"', '').replace(',', '.').strip()
    try:
        return float(numLimpio)
    except ValueError:
        return None

def calcularCategoria(importe):
    if importe is None:
        return 'invalido'
    elif importe < 100:
        return 'bajo'
    elif importe < 500:
        return 'medio'
    else:
        return 'alto'

print("Leyendo CSV original...")
filas = []
idProcessados = set()

try:
    with open('data/datos_crudos.csv', 'r', encoding='utf-8') as archivoOrigen:
        lector = csv.DictReader(archivoOrigen)
        
        for fila in lector:
            idRegistro = fila.get('id', '').strip()
            
            if idRegistro in idProcessados:
                print(f"  ⊘ Duplicado eliminado: ID {idRegistro}")
                continue
            
            idProcessados.add(idRegistro)
            
            importeNuevo = limpiarImporte(fila.get('importe', ''))
            fechaNueva = convertirFecha(fila.get('fecha', ''))
            
            filaLimpia = {
                'id': idRegistro,
                'fecha': fechaNueva,
                'cliente': fila.get('cliente', '').strip().title(),
                'concepto': fila.get('concepto', '').strip().title(),
                'importe': importeNuevo,
                'estado': fila.get('estado', '').strip().lower(),
                'categoriaImporte': calcularCategoria(importeNuevo)
            }
            
            filas.append(filaLimpia)

    print(f"✓ Lectura completada: {len(filas)} registros válidos")
    
except FileNotFoundError:
    print("❌ Error: No se encontró data/datos_crudos.csv")
    exit()

print("\nGuardando datos limpios...")
try:
    with open('data/limpio.csv', 'w', newline='', encoding='utf-8') as archivoLimpio:
        encabezados = ['id', 'fecha', 'cliente', 'concepto', 'importe', 'estado', 'categoriaImporte']
        escritor = csv.DictWriter(archivoLimpio, fieldnames=encabezados)
        escritor.writeheader()
        escritor.writerows(filas)
    
    print(f"✓ Archivo guardado: data/limpio.csv")
    print(f"  Registros: {len(filas)}")
    print(f"  Duplicados eliminados: {sum(1 for _ in idProcessados)}")
    print(f"✓ Limpieza completada")
    
except Exception as e:
    print(f"❌ Error al guardar: {e}")
