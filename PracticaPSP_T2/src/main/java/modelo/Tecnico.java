package modelo; 

import java.io.Serializable; // Importa Serializable para permitir enviar objetos por RMI

/*
 * Clase Tecnico
 * 
 * Representa a un técnico que atiende tickets en el sistema.
 * 
 * Forma parte del MODELO del proyecto.
 * 
 * Implementa Serializable porque los técnicos
 * pueden enviarse entre cliente y servidor con RMI.
 */
public class Tecnico implements Serializable { // Declaración de la clase Tecnico que implementa Serializable

    // Nombre del técnico
    private String nombre; // Variable que almacena el nombre del técnico

    // Indica si el técnico está ocupado o libre
    private boolean ocupado; // Variable que indica si el técnico está atendiendo un ticket

    /*
     * Constructor del técnico
     * 
     * Cuando se crea un técnico:
     * - Tiene un nombre
     * - Empieza libre (no ocupado)
     */
    public Tecnico(String nombre) { // Constructor que recibe el nombre del técnico
        this.nombre = nombre; // Asigna el nombre recibido a la variable nombre
        this.ocupado = false; // Inicializa el técnico como libre
    }

    // -----------------------
    // GETTERS Y SETTERS
    // -----------------------

    public String getNombre() { // Método que devuelve el nombre del técnico
        return nombre; // Retorna el nombre del técnico
    }

    public boolean isOcupado() { // Método que indica si el técnico está ocupado
        return ocupado; // Devuelve el estado de ocupación del técnico
    }

    /*
     * Marca al técnico como ocupado.
     * 
     * Se usa cuando el técnico toma un ticket.
     */
    public void ocupar() { // Método para marcar al técnico como ocupado
        ocupado = true; // Cambia el estado a ocupado
    }

    /*
     * Marca al técnico como libre.
     * 
     * Se usa cuando el técnico termina un ticket.
     */
    public void liberar() { // Método para marcar al técnico como libre
        ocupado = false; // Cambia el estado a libre
    }
} 
