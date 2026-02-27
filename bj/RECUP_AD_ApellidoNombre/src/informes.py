import csv
from collections import defaultdict

def cargarDatos():
    filas = []
    try:
        with open('data/limpio.csv', 'r', encoding='utf-8') as archivo:
            lector = csv.DictReader(archivo)
            for fila in lector:
                fila['importe'] = float(fila['importe']) if fila['importe'] else 0
                filas.append(fila)
        return filas
    except FileNotFoundError:
        print("❌ Error: No se encontró data/limpio.csv")
        return []

def cargarErrores():
    errores = []
    try:
        with open('data/errores.csv', 'r', encoding='utf-8') as archivo:
            lector = csv.DictReader(archivo)
            errores = list(lector)
        return errores
    except FileNotFoundError:
        return []

def calcularEstadisticas(datos):
    numRegistros = len(datos)
    totalIngresos = sum(fila['importe'] for fila in datos)
    
    clienteTotal = defaultdict(float)
    for fila in datos:
        clienteTotal[fila['cliente']] += fila['importe']
    
    clienteMayor = max(clienteTotal.items(), key=lambda x: x[1]) if clienteTotal else ('N/A', 0)
    
    mesTotales = defaultdict(int)
    for fila in datos:
        mes = fila['fecha'][:7]
        mesTotales[mes] += 1
    
    mesMasActivo = max(mesTotales.items(), key=lambda x: x[1]) if mesTotales else ('N/A', 0)
    
    return {
        'numRegistros': numRegistros,
        'totalIngresos': totalIngresos,
        'clienteMayor': clienteMayor,
        'mesMasActivo': mesMasActivo
    }

def analizarProblemas(errores):
    tiposError = defaultdict(int)
    for error in errores:
        tipoError = error.get('error', 'Desconocido').split(':')[0]
        tiposError[tipoError] += 1
    return tiposError

def generarInforme(estadisticas, errores):
    tiposError = analizarProblemas(errores)
    
    contenido = ""
    contenido += "="*60 + "\n"
    contenido += "INFORME DE DATOS - RESUMEN EJECUTIVO\n"
    contenido += "="*60 + "\n\n"
    
    contenido += "ESTADISTICAS GENERALES:\n"
    contenido += "-" * 60 + "\n"
    contenido += f"Numero total de registros: {estadisticas['numRegistros']}\n"
    contenido += f"Total ingresos: ${estadisticas['totalIngresos']:,.2f}\n"
    contenido += f"Promedio por transaccion: ${estadisticas['totalIngresos']/max(1, estadisticas['numRegistros']):,.2f}\n\n"
    
    contenido += "ANALISIS DE CLIENTES:\n"
    contenido += "-" * 60 + "\n"
    cliente, total = estadisticas['clienteMayor']
    contenido += f"Cliente con mayor gasto: {cliente}\n"
    contenido += f"Monto total: ${total:,.2f}\n"
    contenido += f"Porcentaje del total: {(total/max(1, estadisticas['totalIngresos'])*100):.1f}%\n\n"
    
    contenido += "ANALISIS TEMPORAL:\n"
    contenido += "-" * 60 + "\n"
    mes, numMov = estadisticas['mesMasActivo']
    contenido += f"Mes con mas actividad: {mes}\n"
    contenido += f"Numero de movimientos: {numMov}\n\n"
    
    contenido += "PROBLEMAS ENCONTRADOS Y SOLUCIONES:\n"
    contenido += "-" * 60 + "\n"
    
    if errores:
        contenido += f"Total de errores detectados: {len(errores)}\n\n"
        contenido += "Tipos de errores:\n"
        for tipoError, cantidad in sorted(tiposError.items(), key=lambda x: x[1], reverse=True):
            contenido += f"  • {tipoError}: {cantidad} ocurrencias\n"
        
        contenido += "\nSOLUCIONES APLICADAS:\n"
        contenido += "  ✓ Espacios innecesarios eliminados\n"
        contenido += "  ✓ Importes convertidos a formato numerico\n"
        contenido += "  ✓ Fechas unificadas a formato YYYY-MM-DD\n"
        contenido += "  ✓ Registros duplicados removidos\n"
        contenido += "  ✓ Campo categoriaImporte calculado\n"
        contenido += "  ✓ Capitalizacion de nombres corregida\n"
    else:
        contenido += "No se encontraron errores en los datos.\n"
        contenido += "\nOPERACIONES REALIZADAS:\n"
        contenido += "  ✓ Validacion de integridad completada\n"
        contenido += "  ✓ Limpieza de datos ejecutada\n"
        contenido += "  ✓ Normalizacion de formatos realizada\n"
    
    contenido += "\n" + "="*60 + "\n"
    contenido += f"Informe generado: {datosGeneral()}\n"
    contenido += "="*60 + "\n"
    
    return contenido

def datosGeneral():
    from datetime import datetime
    return datetime.now().strftime("%d/%m/%Y %H:%M:%S")

print("Generando informe...\n")

datos = cargarDatos()
errores = cargarErrores()
estadisticas = calcularEstadisticas(datos)

informe = generarInforme(estadisticas, errores)

print(informe)

try:
    with open('exports/informe.txt', 'w', encoding='utf-8') as archivo:
        archivo.write(informe)
    print("✓ Informe guardado en: exports/informe.txt")
except Exception as e:
    print(f"❌ Error al guardar informe: {e}")
