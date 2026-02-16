package servidor; // Indica que esta clase pertenece al paquete servidor

import java.rmi.RemoteException; // Importa RemoteException para métodos RMI
import java.rmi.server.UnicastRemoteObject; // Importa UnicastRemoteObject para crear el objeto remoto
import java.util.ArrayList; // Importa ArrayList para las listas
import java.util.List; // Importa List para manejar listas

import modelo.EstadoTicket; // Importa el enum EstadoTicket
import modelo.Tecnico; // Importa la clase Tecnico
import modelo.Ticket; // Importa la clase Ticket
import rmi.HelpDeskRemote; // Importa la interfaz remota

/*
 * Clase HelpDeskImpl
 * 
 * Esta clase es el SERVIDOR del sistema.
 * 
 * Implementa la interfaz HelpDeskRemote,
 * por lo que contiene la lógica real del sistema.
 *
 */
public class HelpDeskImpl extends UnicastRemoteObject implements HelpDeskRemote { // Declaración del servidor RMI

    // Lista de tickets del sistema
    private List<Ticket> tickets; // Almacena todos los tickets creados

    // Lista de técnicos registrados
    private List<Tecnico> tecnicos; // Almacena los técnicos del sistema

    // Contador para generar IDs únicos
    private int contadorTickets; // Se usa para asignar IDs incrementales a los tickets

    /*
     * Constructor del servidor
     * 
     * Inicializa las listas y el contador.
     */
    public HelpDeskImpl() throws RemoteException { // Constructor del servidor
        tickets = new ArrayList<>(); // Inicializa la lista de tickets
        tecnicos = new ArrayList<>(); // Inicializa la lista de técnicos
        contadorTickets = 1; // Inicializa el contador de tickets
    }

    /*
     * -------------------------
     * MÉTODOS OBLIGATORIOS
     * -------------------------
     */

    @Override
    public synchronized void crearTicket(String cliente, String descripcion, String prioridad)
            throws RemoteException { // Método remoto para crear un ticket

        Ticket t = new Ticket(contadorTickets, cliente, descripcion, prioridad); // Crea un nuevo ticket
        contadorTickets++; // Incrementa el contador para el siguiente ticket
        tickets.add(t); // Añade el ticket a la lista
    }

    @Override
    public synchronized List<Ticket> listarTickets() throws RemoteException { // Devuelve la lista de tickets
        return tickets; // Retorna la lista completa
    }

    @Override
    public synchronized void registrarTecnico(String nombre) throws RemoteException { // Registra un técnico
        tecnicos.add(new Tecnico(nombre)); // Crea y añade un nuevo técnico
    }

    @Override
    public synchronized void asignarTicket(int idTicket, String tecnico)
                 throws RemoteException { // Asigna un ticket a un técnico

        // PRIMERO: comprobar si hay técnicos libres
        boolean hayLibre = false; // Variable para saber si hay técnicos disponibles
        for (Tecnico tec : tecnicos) { // Recorre la lista de técnicos
            if (!tec.isOcupado()) { // Si encuentra uno libre
                hayLibre = true; // Marca que hay un técnico libre
            }
        }

        // Si no hay técnicos libres, se registra en el historial y se sale
        if (!hayLibre) {
            for (Ticket t : tickets) { // Recorre los tickets
                if (t.getId() == idTicket) { // Busca el ticket correspondiente
                    t.addHistorial("No hay técnicos disponibles por el momento"); // Añade mensaje al historial
                }
            }
            return; // Sale del método
        }

        // SEGUNDO: buscar el ticket y el técnico
        for (Ticket t : tickets) { // Recorre los tickets
            if (t.getId() == idTicket) { // Encuentra el ticket

                for (Tecnico tec : tecnicos) { // Recorre los técnicos
                    if (tec.getNombre().equals(tecnico)) { // Encuentra el técnico

                        // Si el técnico está ocupado
                        if (tec.isOcupado()) {
                            t.addHistorial("No se pudo asignar: técnico ocupado"); // Registra el error
                            return; // Sale del método
                        }

                        // Técnico libre → se asigna
                        t.setTecnicoAsignado(tecnico); // Asigna el técnico al ticket
                        t.addHistorial("Técnico asignado: " + tecnico); // Añade mensaje al historial
                        return; // Sale del método
                    }
                }

                // Si el técnico no existe
                t.addHistorial("No se pudo asignar: técnico no registrado"); // Registra el error
                return; // Sale del método
            }
        }
    }

    @Override
    public synchronized void tomarTicket(int idTicket, String tecnico)
              throws RemoteException { // Método para que un técnico tome un ticket

        for (Ticket t : tickets) { // Recorre los tickets
            if (t.getId() == idTicket) { // Encuentra el ticket

                // Buscar el técnico
                for (Tecnico tec : tecnicos) { // Recorre los técnicos
                    if (tec.getNombre().equals(tecnico)) { // Encuentra el técnico

                        // Si el técnico está ocupado, no puede tomar más tickets
                        if (tec.isOcupado()) {
                            t.addHistorial("No se pudo tomar el ticket: técnico ocupado"); // Registra el error
                            return; // Sale del método
                        }

                        // Asignar técnico al ticket
                        t.setTecnicoAsignado(tecnico); // Guarda el técnico en el ticket

                        // Marcar al técnico como ocupado
                        tec.ocupar(); // Cambia el estado del técnico

                        // Cambiar el estado del ticket a EN_PROCESO
                        t.setEstado(EstadoTicket.EN_PROCESO); // Actualiza el estado

                        return; // Sale del método
                    }
                }

                // Si el técnico no existe
                t.addHistorial("Error: técnico no registrado"); // Registra el error
                return; // Sale del método
            }
        }
    }

    // Comprueba si hay técnicos libres
    private boolean hayTecnicosLibres() { // Método auxiliar
        for (Tecnico t : tecnicos) { // Recorre los técnicos
            if (!t.isOcupado()) { // Si encuentra uno libre
                return true; // Devuelve true
            }
        }
        return false; // Si no hay técnicos libres devuelve false
    }

    @Override
    public synchronized void resolverTicket(int idTicket)
            throws RemoteException { // Método para resolver un ticket

        for (Ticket t : tickets) { // Recorre los tickets
            if (t.getId() == idTicket) { // Encuentra el ticket

                // Cambia el estado del ticket a RESUELTO
                t.setEstado(EstadoTicket.RESUELTO); // Marca el ticket como resuelto

                // Libera al técnico asignado
                for (Tecnico tec : tecnicos) { // Recorre los técnicos
                    if (tec.getNombre().equals(t.getTecnicoAsignado())) { // Encuentra el técnico
                        tec.liberar(); // Marca al técnico como libre
                    }
                }

                return; // Sale del método
            }
        }
    }

    /*
     * -------------------------
     * MÉTODO PARA LOS HILOS
     * -------------------------
     */

    /*
     * Devuelve un ticket con estado PENDIENTE.
     * 
     * Este método es usado por los hilos de técnicos
     * para buscar trabajo.
     * 
     * synchronized:
     * - evita que dos técnicos cojan el mismo ticket
     */
    public synchronized Ticket obtenerTicketPendiente() { // Método usado por los hilos

        for (Ticket t : tickets) { // Recorre los tickets
            // Devuelve solo tickets pendientes que ya tengan técnico asignado
            if (t.getEstado() == EstadoTicket.PENDIENTE &&
                !t.getTecnicoAsignado().isEmpty()) {
                return t; // Devuelve el ticket encontrado
            }
        }

        return null; // Si no hay tickets pendientes devuelve null
    }

    /*
     * -------------------------
     * PUNTO-EXTRA 
     * -------------------------
     */

    @Override
    public synchronized String obtenerEstadisticas() throws RemoteException { // Devuelve estadísticas del sistema

        int pendientes = 0; // Contador de tickets pendientes
        int enProceso = 0; // Contador de tickets en proceso
        int resueltos = 0; // Contador de tickets resueltos
        int libres = 0; // Contador de técnicos libres

        for (Ticket t : tickets) { // Recorre los tickets
            if (t.getEstado() == EstadoTicket.PENDIENTE) {
                pendientes++; // Incrementa pendientes
            }
            if (t.getEstado() == EstadoTicket.EN_PROCESO) {
                enProceso++; // Incrementa en proceso
            }
            if (t.getEstado() == EstadoTicket.RESUELTO) {
                resueltos++; // Incrementa resueltos
            }
        }

        for (Tecnico t : tecnicos) { // Recorre los técnicos
            if (!t.isOcupado()) {
                libres++; // Incrementa técnicos libres
            }
        }

        // Devuelve un resumen del estado del sistema
        return "Pendientes: " + pendientes +
               " | En proceso: " + enProceso +
               " | Resueltos: " + resueltos +
               " | Técnicos libres: " + libres;
    }

    @Override
    public synchronized List<String> obtenerHistorialTicket(int idTicket)
            throws RemoteException { // Devuelve el historial de un ticket

        for (Ticket t : tickets) { // Recorre los tickets
            if (t.getId() == idTicket) { // Encuentra el ticket
                return t.getHistorial(); // Devuelve su historial
            }
        }

        return new ArrayList<>(); // Si no existe devuelve una lista vacía
    }
} 
