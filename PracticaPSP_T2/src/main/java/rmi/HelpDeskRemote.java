package rmi; 

import java.rmi.Remote; // Importa Remote, obligatorio para interfaces RMI
import java.rmi.RemoteException; // Importa RemoteException para manejar errores remotos
import java.util.List; // Importa List para devolver listas de datos
import modelo.Ticket; // Importa la clase Ticket del modelo

/*
 * Interfaz RMI HelpDeskRemote
 * 
 * Esta interfaz define los métodos que el CLIENTE
 * puede llamar al SERVIDOR de forma remota.
 * 
 *punto de unión entre cliente y servidor.
 * 
 * IMPORTANTE:
 * - Extiende Remote (en RMI)
 * - Todos los métodos lanzan RemoteException y pueden fallar por problemas. Ej red
 */
public interface HelpDeskRemote extends Remote { // Declaración de la interfaz remota

    /*
     * ---------------------------
     * FUNCIONALIDAD 
     * ---------------------------
     */

    /*
     * Permite crear un nuevo ticket en el sistema.
     * 
     * El cliente envía los datos básicos y el servidor
     * se encarga de crear el objeto Ticket.
     */
    void crearTicket(String cliente, String descripcion, String prioridad) // Método remoto para crear un ticket
            throws RemoteException; // Puede lanzar una excepción remota

    /*
     * Devuelve la lista completa de tickets.
     * 
     * Se usa para:
     * - Botón "Actualizar"
     * - Mostrar los tickets en la tabla
     */
    List<Ticket> listarTickets() throws RemoteException; // Método remoto que devuelve la lista de tickets

    /*
     * Registra un nuevo técnico en el sistema.
     * 
     * Este método se llama normalmente al iniciar
     * el servidor, no desde la interfaz.
     */
    void registrarTecnico(String nombre) throws RemoteException; // Método remoto para registrar un técnico

    /*
     * Asigna un ticket a un técnico.
     * 
     * Esta asignación puede ser:
     * - Manual (desde la interfaz)
     * - Automática (por el servidor)
     */
    void asignarTicket(int idTicket, String tecnico) // Método remoto para asignar un ticket
            throws RemoteException; // Puede lanzar excepción remota

    /*
     * Un técnico toma un ticket y empieza a trabajarlo.
     *
     * Cambia el estado del ticket a EN_PROCESO.
     */
    void tomarTicket(int idTicket, String tecnico) // Método remoto para que un técnico tome un ticket
            throws RemoteException; // Puede lanzar excepción remota

    /*
     * Marca un ticket como resuelto.
     * 
     * Cambia el estado del ticket a RESUELTO.
     */
    void resolverTicket(int idTicket) // Método remoto para resolver un ticket
            throws RemoteException; // Puede lanzar excepción remota

    /*
     * ---------------------------
     * EXTRAS FÁCILES
     * ---------------------------
     */

    /*
     * EXTRA 2: ESTADÍSTICAS
     * 
     * Devuelve un texto con información del sistema:
     * - tickets pendientes
     * - tickets en proceso
     * - tickets resueltos
     * 
     * Se muestra en el panel "Estado del sistema" de la interfaz
     */
    String obtenerEstadisticas() throws RemoteException; // Método remoto que devuelve estadísticas del sistema

    /*
     * EXTRA 3: HISTORIAL DE TICKET
     * 
     * Devuelve el historial de un ticket concreto.
     * 
     * Se usa en el botón "Ver detalle".
     */
    List<String> obtenerHistorialTicket(int idTicket) // Método remoto para obtener el historial de un ticket
            throws RemoteException; // Puede lanzar excepción remota
} 
