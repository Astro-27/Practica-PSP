package modelo; 

import java.io.Serializable; // Importa Serializable para poder enviar el objeto por RMI
import java.util.ArrayList; // Importa ArrayList para implementar la lista del historial
import java.util.List; // Importa List para manejar listas de forma genérica

/*
 * Clase Ticket
 * 
 * Representa una incidencia del sistema HelpDesk.
 */
public class Ticket implements Serializable { // Declaración de la clase Ticket que implementa Serializable

    private int id; // Identificador único del ticket
    private String cliente; // Nombre del cliente que crea el ticket
    private String descripcion; // Descripción del problema
    private String prioridad; // Prioridad del ticket
    private EstadoTicket estado; // Estado actual del ticket
    private String tecnicoAsignado; // Nombre del técnico asignado al ticket

    // Historial del ticket
    private List<String> historial; // Lista que guarda los eventos del ticket

    /*
     * Constructor del ticket
     */
    public Ticket(int id, String cliente, String descripcion, String prioridad) { // Constructor que recibe los datos básicos
         this.id = id; // Asigna el id recibido al ticket
         this.cliente = cliente; // Asigna el nombre del cliente
         this.descripcion = descripcion; // Asigna la descripción del problema
         this.prioridad = prioridad; // Asigna la prioridad del ticket

         // Estado inicial: pendiente
         this.estado = EstadoTicket.PENDIENTE; // Inicializa el estado como PENDIENTE

         // Aún no hay técnico asignado
         this.tecnicoAsignado = ""; // Inicializa el técnico asignado vacío

         // Inicializar historial
         historial = new ArrayList<>(); // Crea la lista del historial
         historial.add("Ticket creado (PENDIENTE)"); // Añade el primer mensaje al historial
    }

    // -------------------------
    // GETTERS
    // -------------------------

    public int getId() { // Devuelve el id del ticket
        return id; // Retorna el id
    }

    public String getCliente() { // Devuelve el nombre del cliente
        return cliente; // Retorna el cliente
    }

    public String getDescripcion() { // Devuelve la descripción del ticket
        return descripcion; // Retorna la descripción
    }

    public String getPrioridad() { // Devuelve la prioridad del ticket
        return prioridad; // Retorna la prioridad
    }

    public EstadoTicket getEstado() { // Devuelve el estado actual del ticket
        return estado; // Retorna el estado
    }

    public String getTecnicoAsignado() { // Devuelve el técnico asignado
        return tecnicoAsignado; // Retorna el nombre del técnico
    }

    public List<String> getHistorial() { // Devuelve el historial del ticket
        return historial; // Retorna la lista de mensajes
    }

    // -------------------------
    // SETTERS
    // -------------------------

    public void setEstado(EstadoTicket estado) { // Cambia el estado del ticket
        this.estado = estado; // Asigna el nuevo estado
        historial.add("Estado cambiado a " + estado); // Añade el cambio al historial
    }

    public void setTecnicoAsignado(String tecnicoAsignado) { // Asigna un técnico al ticket
        this.tecnicoAsignado = tecnicoAsignado; // Guarda el nombre del técnico
        historial.add("Técnico asignado: " + tecnicoAsignado); // Registra el cambio en el historial
    }

    
    public void addHistorial(String mensaje) { // Añade un mensaje personalizado al historial
        historial.add(mensaje); // Guarda el mensaje en la lista
    }
} 
