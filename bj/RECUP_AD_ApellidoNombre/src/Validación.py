import csv
import json
from datetime import datetime

columnasRequeridas = {'id', 'fecha', 'cliente', 'concepto', 'importe', 'estado'}

def esDateValida(dateStr):
    formatos = ['%d/%m/%Y', '%Y-%m-%d', '%d-%m-%Y', '%Y/%m/%d']
    for fmt in formatos:
        try:
            datetime.strptime(dateStr.strip(), fmt)
            return True
        except ValueError:
            continue
    return False

def esNumeroValido(numeroStr):
    numLimpio = numeroStr.strip().replace('"', '').replace(',', '.').strip()
    try:
        float(numLimpio)
        return True
    except ValueError:
        return False

print("Leyendo CSV...")
listaErrores = []
numFila = 0

try:
    with open('data/datos_crudos.csv', 'r', encoding='utf-8') as archivoCSV:
        lectorCSV = csv.DictReader(archivoCSV)
        
        if lectorCSV.fieldnames and not columnasRequeridas.issubset(set(lectorCSV.fieldnames)):
            print("❌ Error: Faltan columnas obligatorias")
            listaErrores.append({
                'fila': 0,
                'id': '',
                'error': f'Columnas faltantes. Esperadas: {columnasRequeridas}'
            })
        
        for fila in lectorCSV:
            numFila += 1
            
            if not esDateValida(fila.get('fecha', '')):
                listaErrores.append({
                    'fila': numFila,
                    'id': fila.get('id', ''),
                    'error': f'Fecha inválida: {fila.get("fecha", "VACIO")}'
                })
            
            if not esNumeroValido(fila.get('importe', '')):
                listaErrores.append({
                    'fila': numFila,
                    'id': fila.get('id', ''),
                    'error': f'Importe no numerico: {fila.get("importe", "VACIO")}'
                })
            
            for columna in columnasRequeridas:
                if not fila.get(columna, '').strip():
                    listaErrores.append({
                        'fila': numFila,
                        'id': fila.get('id', ''),
                        'error': f'Campo vacio: {columna}'
                    })

except FileNotFoundError:
    print("❌ Error: No se encontró data/datos_crudos.csv")

print(f"✓ CSV procesado: {numFila} filas")

print("Leyendo JSON...")
try:
    with open('data/productos.json', 'r', encoding='utf-8') as archivoJSON:
        datosJSON = json.load(archivoJSON)
        numProductos = len(datosJSON.get('productos', []))
        print(f"✓ JSON procesado: {numProductos} productos")
except FileNotFoundError:
    print("⚠ Advertencia: No se encontró data/productos.json")
except json.JSONDecodeError:
    print("❌ Error: JSON invalido")

print("\nGuardando errores...")
if listaErrores:
    with open('data/errores.csv', 'w', newline='', encoding='utf-8') as archivoErrores:
        escritor = csv.DictWriter(archivoErrores, fieldnames=['fila', 'id', 'error'])
        escritor.writeheader()
        escritor.writerows(listaErrores)
    print(f"✓ Errores guardados: {len(listaErrores)} errores encontrados")
else:
    print("✓ No se encontraron errores")

print("\n✓ Validacion completada")
