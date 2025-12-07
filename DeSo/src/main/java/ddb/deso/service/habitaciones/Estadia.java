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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    long idEstadia;

    @ManyToOne(fetch = FetchType.LAZY)
    Habitacion habitacion;

    @ManyToMany(fetch = FetchType.LAZY)
    List<Servicio> listaServicios;

    @OneToOne(fetch = FetchType.LAZY)
    DatosCheckIn datosCheckIn;
    @OneToOne(fetch = FetchType.LAZY)
    DatosCheckOut datosCheckOut;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private Reserva reserva;

    private LocalDate fecha_inicio;
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
