import tkinter as tk
from tkinter import ttk, messagebox, scrolledtext
import os
import sys
from datetime import datetime

dirProyecto = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
os.chdir(dirProyecto)

class InterfazGrafica:
    def __init__(self, ventana):
        self.ventana = ventana
        self.ventana.title("Sistema de Análisis de Datos")
        self.ventana.geometry("700x550")
        self.ventana.resizable(False, False)
        
        estilo = ttk.Style()
        estilo.theme_use('clam')
        
        self.crearInterfaz()
    
    def crearInterfaz(self):
        marco_titulo = ttk.Frame(self.ventana)
        marco_titulo.pack(pady=15)
        
        titulo = ttk.Label(marco_titulo, text="SISTEMA DE ANÁLISIS DE DATOS", 
                          font=("Arial", 16, "bold"))
        titulo.pack()
        
        subtitulo = ttk.Label(marco_titulo, text="Gestión de datos y reportes",
                             font=("Arial", 10))
        subtitulo.pack()
        
        marco_botones = ttk.Frame(self.ventana)
        marco_botones.pack(pady=10)
        
        botones = [
            ("Validar Datos", self.validar, "blue"),
            ("Limpiar Datos", self.limpiar, "green"),
            ("Ver Consultas", self.consultas, "purple"),
            ("Generar Informe", self.informe, "orange"),
            ("Inicializar BD", self.baseDatos, "red"),
        ]
        
        for texto, comando, color in botones:
            btn = ttk.Button(marco_botones, text=texto, command=comando, width=18)
            btn.pack(pady=5)
        
        marco_salida = ttk.Frame(self.ventana)
        marco_salida.pack(pady=10, padx=10, fill=tk.BOTH, expand=True)
        
        etiqueta_salida = ttk.Label(marco_salida, text="Registro de ejecución:", 
                                   font=("Arial", 10, "bold"))
        etiqueta_salida.pack(anchor="w")
        
        self.textSalida = scrolledtext.ScrolledText(marco_salida, height=12, width=80,
                                                    font=("Courier", 9))
        self.textSalida.pack(fill=tk.BOTH, expand=True, pady=5)
        
        marco_inferior = ttk.Frame(self.ventana)
        marco_inferior.pack(pady=5)
        
        ttk.Button(marco_inferior, text="Limpiar log", 
                  command=self.limpiarLog, width=15).pack(side=tk.LEFT, padx=5)
        ttk.Button(marco_inferior, text="Salir", 
                  command=self.ventana.quit, width=15).pack(side=tk.LEFT, padx=5)
    
    def escribirLog(self, mensaje):
        self.textSalida.insert(tk.END, f"{datetime.now().strftime('%H:%M:%S')} - {mensaje}\n")
        self.textSalida.see(tk.END)
        self.ventana.update()
    
    def limpiarLog(self):
        self.textSalida.delete("1.0", tk.END)
    
    def ejecutarScript(self, archivo):
        try:
            self.escribirLog(f"⏳ Ejecutando {archivo}...")
            with open(archivo, 'r', encoding='utf-8') as f:
                exec(f.read(), {'__name__': '__main__'})
            self.escribirLog(f"✓ {archivo} completado\n")
        except Exception as e:
            self.escribirLog(f"❌ Error: {e}\n")
            messagebox.showerror("Error", f"Error al ejecutar {archivo}:\n{str(e)}")
    
    def validar(self):
        self.escribirLog("Iniciando validación...")
        self.ejecutarScript('src/Validación.py')
    
    def limpiar(self):
        self.escribirLog("Iniciando limpieza...")
        self.ejecutarScript('src/Limpieza.py')
    
    def consultas(self):
        self.escribirLog("Iniciando consultas...")
        self.ejecutarScript('src/consultas.py')
    
    def informe(self):
        self.escribirLog("Generando informe...")
        self.ejecutarScript('src/informes.py')
    
    def baseDatos(self):
        self.escribirLog("Inicializando base de datos...")
        self.ejecutarScript('data/insertar_datos.py')

if __name__ == "__main__":
    ventana = tk.Tk()
    app = InterfazGrafica(ventana)
    ventana.mainloop()
