/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.service.habitaciones;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
    //  Cambiar para que contenga nombre, apellido y telefono del que reserva
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    long idReserva;

    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private String estado;
    private String nombre;
    private String apellido;
    private String telefono;

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
