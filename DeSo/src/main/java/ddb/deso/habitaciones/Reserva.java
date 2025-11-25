/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.habitaciones;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


/**
 * Representa una **reserva** de alojamiento.
 * <p>
 * Contiene el periodo de tiempo reservado (fechas de inicio y fin) y el estado
 * actual de dicha reserva (ej: 'Confirmada', 'Pendiente', 'Cancelada').
 * </p>
 */

@Data
@Entity
@Table(name="reserva")
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    //  Cambiar para que contenga nombre, apellido y telefono del que reserva

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    long idReserva;

    @Getter
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private String estado;
    private String nombre;
    private String apellido;
    private String telefono;

    @ManyToMany(fetch = FetchType.LAZY)
    List<Habitacion> listaHabitaciones;

    public Reserva(LocalDate fecha_inicio, LocalDate fecha_fin, String estado) {
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.estado = estado;
    }

    public Reserva(LocalDate fecha_inicio, LocalDate fecha_fin){
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
    }

    public void agregarHabitacion(Habitacion habitacion){
        listaHabitaciones.add(habitacion);
    }

}
