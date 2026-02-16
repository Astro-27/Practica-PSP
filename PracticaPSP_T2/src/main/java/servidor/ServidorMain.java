package servidor; 

import java.rmi.Naming; // Importa Naming para registrar el objeto remoto
import java.rmi.registry.LocateRegistry; // Importa LocateRegistry para arrancar el registro RMI

/*
 * Clase ServidorMain
 * 
 *Esta clase es el PUNTO DE ARRANQUE del servidor.
 * 
 * Aquí se hace:
 * - Arrancar el registro RMI
 * - Crear el servidor HelpDesk
 * - Registrar técnicos
 * - Lanzar los hilos de técnicos
 */
public class ServidorMain { // Declaración de la clase principal del servidor

    public static void main(String[] args) { // Método main, punto de entrada del programa

        try { // Bloque try para controlar posibles errores

            /*
             * Arrancamos el registro RMI en el puerto 1099.
             * 
             * Es el puerto por defecto de RMI.
             */
            LocateRegistry.createRegistry(1099); // Inicia el registro RMI en el puerto 1099

            /*
             * Creamos el servidor HelpDesk.
             */
            HelpDeskImpl servidor = new HelpDeskImpl(); // Crea la instancia del servidor

            /*
             * Registramos el servidor con un nombre.
             * 
             * Ese nombre lo usará el cliente para conectarse.
             */
            Naming.rebind("HelpDesk", servidor); // Registra el servidor RMI con el nombre "HelpDesk"

            /*
             * ------------------------
             * REGISTRO DE TÉCNICOS
             * ------------------------
             * 
             * Registramos algunos técnicos de prueba.
             * 
             * Esto cumple el requisito del enunciado
             * de tener técnicos en el sistema.
             */
            servidor.registrarTecnico("Brayan"); // Registra el primer técnico
            servidor.registrarTecnico("Bladi"); // Registra el segundo técnico

            /*
             * ------------------------
             * CONCURRENCIA (HILOS)
             * ------------------------
             * 
             * Creamos al menos 2 hilos de técnicos,
             * como exige la práctica.
             */
            HiloTecnico h1 = new HiloTecnico(servidor, "Brayan"); // Crea el hilo del primer técnico
            HiloTecnico h2 = new HiloTecnico(servidor, "Bladi"); // Crea el hilo del segundo técnico

            /*
             * Arrancamos los hilos.
             */
            h1.start(); // Inicia el hilo del primer técnico
            h2.start(); // Inicia el hilo del segundo técnico

            System.out.println("Servidor HelpDesk iniciado correctamente."); // Mensaje de confirmación

        } catch (Exception e) { // Captura cualquier excepción
            System.out.println("Error al iniciar el servidor"); // Mensaje de error
            e.printStackTrace(); // Muestra el detalle del error
        }
    }
} 
