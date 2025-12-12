/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.negocio.habitaciones;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Representa una **reserva** de alojamiento.
 * <p>
 * Contiene el periodo de tiempo reservado (fechas de inicio y fin) y el estado
 * actual de dicha reserva (ej: 'Confirmada', 'Pendiente', 'Cancelada').
 * </p>
 */

@Getter
@Setter
@Entity
@Table(name="reserva")
@NoArgsConstructor
public class Reserva {
    /**
     * Identificador único y autoincremental de la reserva.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
            long idReserva;

    /**
     * Fecha de inicio de la reserva.
     */
    private LocalDate fecha_inicio;
    /**
     * Fecha de finalización de la reserva.
     */
    private LocalDate fecha_fin;
    /**
     * Estado actual de la reserva (ej: Confirmada, Pendiente, Cancelada).
     */
    private String estado;
    /**
     * Nombre de la persona que realiza la reserva.
     */
    private String nombre;
    /**
     * Apellido de la persona que realiza la reserva.
     */
    private String apellido;
    /**
     * Teléfono de contacto para la reserva.
     */
    private String telefono;

    /**
     * Lista de {@link Habitacion}es asociadas a esta reserva.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    List<Habitacion> listaHabitaciones;

    public Reserva(LocalDate fecha_inicio, LocalDate fecha_fin, String estado, String nombre, String apellido, String telefono) {
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.estado = estado;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.listaHabitaciones = new ArrayList<>();
    }

    public void agregarHabitacion(Habitacion habitacion){
        try {
            listaHabitaciones.add(habitacion);
        } catch(NullPointerException e){
            System.out.println("ERROR EN AGREGAR HABITACION, CLASE RESERVA");
            e.printStackTrace();
        }
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return idReserva == reserva.idReserva;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idReserva);
    }
}
