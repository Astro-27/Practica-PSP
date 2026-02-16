package servidor; 

import modelo.EstadoTicket; // Importa el enum EstadoTicket
import modelo.Ticket; // Importa la clase Ticket

/*
 * Clase HiloTecnico
 * 
 * Representa un técnico que trabaja en paralelo
 * atendiendo tickets del sistema.
 * 
 *  EXTENDS Thread 
 * 
 */
public class HiloTecnico extends Thread { // Declaración de la clase HiloTecnico como hilo

    // Indica si el hilo sigue activo
    private boolean activo; // Controla la ejecución del hilo

    // Referencia al servidor para acceder a los tickets
    private HelpDeskImpl servidor; // Permite al hilo interactuar con el servidor

    // Nombre del técnico (para identificarlo)
    private String nombreTecnico; // Guarda el nombre del técnico asociado al hilo

    /*
     * Constructor del hilo técnico
     * 
     * Recibe:
     * - el servidor
     * - el nombre del técnico
     */
    public HiloTecnico(HelpDeskImpl servidor, String nombreTecnico) { // Constructor del hilo
        this.servidor = servidor; // Guarda la referencia al servidor
        this.nombreTecnico = nombreTecnico; // Asigna el nombre del técnico
        this.activo = true; // Marca el hilo como activo
    }

    /*
     * Método run()
     * 
     * Aquí está la lógica del hilo.
     * 
     * El hilo:
     * - busca tickets pendientes
     * - los procesa
     * - duerme un poco
     */
    @Override
    public void run() { // Método que se ejecuta al iniciar el hilo

        while (activo) { // El hilo se ejecuta mientras esté activo

            try {
                // Buscamos un ticket pendiente
                Ticket t = servidor.obtenerTicketPendiente(); // Solicita un ticket pendiente al servidor

                if (t != null) { // Si se ha encontrado un ticket

                    // El técnico toma el ticket
                    servidor.tomarTicket(t.getId(), nombreTecnico); // Marca el ticket como tomado por este técnico

                    // Simulamos el tiempo de trabajo del técnico
                    Thread.sleep(3000); // El hilo duerme simulando el tiempo de resolución

                    // El técnico resuelve el ticket
                    servidor.resolverTicket(t.getId()); // Marca el ticket como resuelto
                } else {
                    // Si no hay tickets pendientes, el técnico espera
                    Thread.sleep(2000); // El hilo duerme antes de volver a comprobar
                }

            } catch (Exception e) {
                // Excepción ignorada para no detener el hilo
            }
        }
    }

    /*
     * Permite detener el hilo de forma controlada.
     */
    public void detener() { // Método para detener el hilo
        activo = false; // Cambia el estado para salir del bucle while
    }
} 
