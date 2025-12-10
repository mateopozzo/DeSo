/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.service.habitaciones;


import ddb.deso.service.alojamiento.DatosCheckIn;
import ddb.deso.service.alojamiento.DatosCheckOut;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import lombok.Setter;


/**
 * Define el **período de tiempo** que un huésped pasa en el hotel.
 * <p>
 * Representa un simple rango de fechas (fecha de inicio y fecha de fin),
 * siendo un componente fundamental de una {@link Reserva}.
 * </p>
 *
 * @see Reserva
 */

@Entity
@Table(name="estadia")
@Getter
@Setter
@NoArgsConstructor
public class Estadia {

    /**
     * Identificador único y autoincremental de la estadía.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    long idEstadia;

    /**
     * La {@link Habitacion} asociada a esta estadía.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    Habitacion habitacion;

    /**
     * Lista de {@link Servicio}s consumidos durante esta estadía.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    List<Servicio> listaServicios;

    /**
     * Registro de Check-In asociado al inicio de esta estadía.
     */
    @OneToOne(fetch = FetchType.LAZY)
    DatosCheckIn datosCheckIn;
    /**
     * Registro de Check-Out asociado al fin de esta estadía.
     */
    @OneToOne(fetch = FetchType.LAZY)
    DatosCheckOut datosCheckOut;
    /**
     * La {@link Reserva} de la cual forma parte esta estadía (opcional si es un walk-in).
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Reserva reserva;

    /**
     * Fecha de inicio de la estadía.
     */
    private LocalDate fecha_inicio;
    /**
     * Fecha de finalización de la estadía.
     */
    private LocalDate fecha_fin;

    public Estadia(LocalDate fecha_inicio, LocalDate fecha_fin) {
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Estadia estadia = (Estadia) o;
        return idEstadia == estadia.idEstadia;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idEstadia);
    }
}
