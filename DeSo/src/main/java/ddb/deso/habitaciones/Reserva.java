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

import java.util.Date;


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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    long idReserva;

    @Getter
    private Date fecha_inicio;
    private Date fecha_fin;
    private String estado;

    public Reserva(Date fecha_inicio, Date fecha_fin, String estado) {
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.estado = estado;
    }

}
