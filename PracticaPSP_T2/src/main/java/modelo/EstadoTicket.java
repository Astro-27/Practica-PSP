package modelo; 

/*
 * Enum que representa los posibles estados de un ticket.
 * 
 * Esta clase es parte del MODELO del proyecto.
 * No tiene lógica, solo valores.
 * 
 * Estados usados en toda la aplicación:
 * - PENDIENTE: ticket recién creado
 * - EN_PROCESO: un técnico lo está atendiendo
 * - RESUELTO: el ticket ya está cerrado
 */
public enum EstadoTicket { // Declaración del enum EstadoTicket

    PENDIENTE, // Estado inicial cuando se crea un ticket
    EN_PROCESO, // Estado cuando un técnico está trabajando en el ticket
    RESUELTO // Estado final cuando el ticket se ha cerrado

} // Fin del enum EstadoTicket
