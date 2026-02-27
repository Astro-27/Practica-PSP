import csv
from collections import defaultdict
from datetime import datetime

def cargarDatos():
    filas = []
    try:
        with open('data/limpio.csv', 'r', encoding='utf-8') as archivo:
            lector = csv.DictReader(archivo)
            for fila in lector:
                fila['importe'] = float(fila['importe']) if fila['importe'] else 0
                filas.append(fila)
        print(f"✓ Datos cargados: {len(filas)} registros\n")
        return filas
    except FileNotFoundError:
        print("❌ Error: No se encontró data/limpio.csv")
        return []

def consulta1TotalMovimientos(datos):
    total = len(datos)
    print(f"CONSULTA 1: TOTAL DE MOVIMIENTOS")
    print(f"Total: {total}\n")
    return [{'metrica': 'Total Movimientos', 'valor': total}]

def consulta2TotalIngresos(datos):
    total = sum(fila['importe'] for fila in datos)
    print(f"CONSULTA 2: TOTAL DE INGRESOS")
    print(f"Total: ${total:,.2f}\n")
    return [{'metrica': 'Total Ingresos', 'valor': f"{total:,.2f}"}]

def consulta3Top5Clientes(datos):
    clienteTotal = defaultdict(float)
    clienteCount = defaultdict(int)
    
    for fila in datos:
        cliente = fila['cliente']
        clienteTotal[cliente] += fila['importe']
        clienteCount[cliente] += 1
    
    top5 = sorted(clienteTotal.items(), key=lambda x: x[1], reverse=True)[:5]
    
    print("CONSULTA 3: TOP 5 CLIENTES")
    resultado = []
    for i, (cliente, total) in enumerate(top5, 1):
        cantidad = clienteCount[cliente]
        print(f"  {i}. {cliente}: ${total:,.2f} ({cantidad} transacciones)")
        resultado.append({
            'rango': i,
            'cliente': cliente,
            'total': f"{total:,.2f}",
            'transacciones': cantidad
        })
    print()
    return resultado

def consulta4TotalPorMes(datos):
    mesTotales = defaultdict(float)
    
    for fila in datos:
        fecha = fila['fecha']
        mesYear = fecha[:7]
        mesTotales[mesYear] += fila['importe']
    
    mesSorted = sorted(mesTotales.items())
    
    print("CONSULTA 4: TOTAL POR MES")
    resultado = []
    for mes, total in mesSorted:
        print(f"  {mes}: ${total:,.2f}")
        resultado.append({
            'mes': mes,
            'total': f"{total:,.2f}"
        })
    print()
    return resultado

def consulta5MovimientosPorEstado(datos):
    estadoCount = defaultdict(int)
    estadoTotal = defaultdict(float)
    
    for fila in datos:
        estado = fila['estado']
        estadoCount[estado] += 1
        estadoTotal[estado] += fila['importe']
    
    print("CONSULTA 5: MOVIMIENTOS POR ESTADO")
    resultado = []
    for estado, cantidad in sorted(estadoCount.items()):
        total = estadoTotal[estado]
        print(f"  {estado.upper()}: {cantidad} movimientos - ${total:,.2f}")
        resultado.append({
            'estado': estado,
            'cantidad': cantidad,
            'total': f"{total:,.2f}"
        })
    print()
    return resultado

def consulta6BuscarCliente(datos, nombreCliente):
    coincidencias = [fila for fila in datos if nombreCliente.lower() in fila['cliente'].lower()]
    
    print(f"CONSULTA 6: BUSQUEDA DE CLIENTE - '{nombreCliente}'")
    print(f"Coincidencias encontradas: {len(coincidencias)}\n")
    
    resultado = []
    for fila in coincidencias:
        print(f"  ID: {fila['id']} | Fecha: {fila['fecha']} | Concepto: {fila['concepto']} | ${fila['importe']:,.2f} | Estado: {fila['estado']}")
        resultado.append({
            'id': fila['id'],
            'fecha': fila['fecha'],
            'concepto': fila['concepto'],
            'importe': fila['importe'],
            'estado': fila['estado'],
            'categoria': fila['categoriaImporte']
        })
    print()
    return resultado

def exportarCSV(nombreArchivo, datos, encabezados):
    try:
        with open(f"data/{nombreArchivo}", 'w', newline='', encoding='utf-8') as archivo:
            escritor = csv.DictWriter(archivo, fieldnames=encabezados)
            escritor.writeheader()
            escritor.writerows(datos)
        print(f"✓ Exportado: data/{nombreArchivo}")
    except Exception as e:
        print(f"❌ Error al exportar: {e}")

print("="*50)
print("SISTEMA DE CONSULTAS DE DATOS")
print("="*50 + "\n")

datos = cargarDatos()

if datos:
    resultado1 = consulta1TotalMovimientos(datos)
    exportarCSV("consulta1Movimientos.csv", resultado1, ['metrica', 'valor'])
    
    resultado2 = consulta2TotalIngresos(datos)
    exportarCSV("consulta2Ingresos.csv", resultado2, ['metrica', 'valor'])
    
    resultado3 = consulta3Top5Clientes(datos)
    exportarCSV("consulta3Top5Clientes.csv", resultado3, ['rango', 'cliente', 'total', 'transacciones'])
    
    resultado4 = consulta4TotalPorMes(datos)
    exportarCSV("consulta4TotalMes.csv", resultado4, ['mes', 'total'])
    
    resultado5 = consulta5MovimientosPorEstado(datos)
    exportarCSV("consulta5MovimientosEstado.csv", resultado5, ['estado', 'cantidad', 'total'])
    
    resultado6 = consulta6BuscarCliente(datos, "Cliente A")
    if resultado6:
        exportarCSV("consulta6BusquedaCliente.csv", resultado6, ['id', 'fecha', 'concepto', 'importe', 'estado', 'categoria'])
    
    print("="*50)
    print("✓ Todas las consultas completadas")
    print("="*50)
