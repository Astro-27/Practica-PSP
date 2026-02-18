package cliente; // Indica que esta clase pertenece al paquete cliente

import java.rmi.Naming; // Importa la clase Naming para buscar objetos remotos RMI
import java.util.List; // Importa la interfaz List para manejar listas de objetos

import modelo.Ticket; // Importa la clase Ticket del paquete modelo
import rmi.HelpDeskRemote; // Importa la interfaz remota que define los métodos del servidor

/*
 * Clase ClienteRMI
 * 
 * Esta clase representa el CLIENTE del sistema.
 * 
 * Se encarga de:
 * - Conectarse al servidor RMI
 * - Llamar a los métodos remotos
 * 
 * La interfaz gráfica (Interfaz.java)
 * usa esta clase para comunicarse con el servidor.
 */
public class ClienteRMI { // Declaración de la clase ClienteRMI

    // Referencia a la interfaz remota del servidor
    private HelpDeskRemote servidor; // Variable que guarda la conexión con el servidor RMI

    /*
     * Constructor del cliente
     * 
     * Se conecta al servidor RMI usando el nombre "HelpDesk".
     */
    public ClienteRMI() { // Constructor de la clase ClienteRMI

        try { // Bloque try para controlar posibles errores
            servidor = (HelpDeskRemote) Naming.lookup("rmi://10.194.56.255/HelpDesk"); // Busca el objeto remoto en el servidor RMI y lo guarda
        } catch (Exception e) { // Captura cualquier excepción que ocurra
            System.out.println("Error al conectar con el servidor"); // Muestra un mensaje de error por consola
            e.printStackTrace(); // Imprime el detalle del error
        }
    }

    /*
     * ===============================
     * MÉTODOS QUE USA LA INTERFAZ
     * ===============================
     */

    /*
     * Crear un ticket desde la interfaz.
     */
    public void crearTicket(String cliente, String descripcion, String prioridad) { // Método para crear un ticket
        try { // Bloque try para manejar errores de comunicación
            servidor.crearTicket(cliente, descripcion, prioridad); // Llama al método remoto para crear el ticket
        } catch (Exception e) { // Captura posibles errores
            e.printStackTrace(); // Muestra el error por consola
        }
    }

    /*
     * Obtener la lista de tickets.
     * 
     * Se usa para llenar la tabla.
     */
    public List<Ticket> listarTickets() { // Método que devuelve la lista de tickets
        try { // Bloque try
            return servidor.listarTickets(); // Llama al servidor y devuelve la lista de tickets
        } catch (Exception e) { // Captura errores
            e.printStackTrace(); // Imprime el error
            return null; // Devuelve null si ocurre un fallo
        }
    }

    /*
     * Asignar un ticket a un técnico (manual).
     */
    public void asignarTicket(int idTicket, String tecnico) { // Método para asignar un ticket a un técnico
        try { // Bloque try
            servidor.asignarTicket(idTicket, tecnico); // Llama al método remoto para asignar el ticket
        } catch (Exception e) { // Captura errores
            e.printStackTrace(); // Muestra el error
        }
    }

    /*
     * Tomar un ticket.
     */
    public void tomarTicket(int idTicket, String tecnico) { // Método para que un técnico tome un ticket
        try { // Bloque try
            servidor.tomarTicket(idTicket, tecnico); // Llama al servidor para marcar el ticket como tomado
        } catch (Exception e) { // Captura errores
            e.printStackTrace(); // Imprime el error
        }
    }

    /*
     * Resolver un ticket.
     */
    public void resolverTicket(int idTicket) { // Método para resolver un ticket
        try { // Bloque try
            servidor.resolverTicket(idTicket); // Llama al servidor para marcar el ticket como resuelto
        } catch (Exception e) { // Captura errores
            e.printStackTrace(); // Muestra el error
        }
    }

    /*
     * ===============================
     * -----------EXTRAS------------------
     * ===============================
     */

    /*
     * EXTRA: ESTADÍSTICAS DEL SISTEMA
     * 
     * Devuelve un texto con el estado general.
     */
    public String obtenerEstadisticas() { // Método que obtiene estadísticas del sistema
        try { // Bloque try
            return servidor.obtenerEstadisticas(); // Llama al servidor y devuelve las estadísticas
        } catch (Exception e) { // Captura errores
            e.printStackTrace(); // Imprime el error
            return ""; // Devuelve una cadena vacía si hay fallo
        }
    }

    /*
     * EXTRA: HISTORIAL DE UN TICKET
     * 
     * Se usa en el botón "Ver detalle".
     */
    public List<String> obtenerHistorialTicket(int idTicket) { // Método que obtiene el historial de un ticket
        try { // Bloque try
            return servidor.obtenerHistorialTicket(idTicket); // Llama al servidor para obtener el historial
        } catch (Exception e) { // Captura errores
            e.printStackTrace(); // Imprime el error
            return null; // Devuelve null si ocurre un problema
        }
    }
}
