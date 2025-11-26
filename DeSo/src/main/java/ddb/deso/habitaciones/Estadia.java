/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.habitaciones;


import ddb.deso.alojamiento.DatosCheckIn;
import ddb.deso.alojamiento.DatosCheckOut;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import ddb.deso.habitaciones.Habitacion;


/**
 * Define el **período de tiempo** que un huésped pasa en el hotel.
 * <p>
 * Representa un simple rango de fechas (fecha de inicio y fecha de fin),
 * siendo un componente fundamental de una {@link Reserva}.
 * </p>
 *
 * @see ddb.deso.habitaciones.Reserva
 */

@Entity
@Table(name="estadia")
@Data
@NoArgsConstructor
public class Estadia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    long idReserva;

    @ManyToMany(fetch = FetchType.LAZY)
    List<Habitacion> listaHabitaciones;

    @ManyToMany(fetch = FetchType.LAZY)
    List<Servicio> listaServicios;

    @OneToOne(fetch = FetchType.LAZY)
    DatosCheckIn datosCheckIn;
    @OneToOne(fetch = FetchType.LAZY)
    DatosCheckOut datosCheckOut;

    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;

    public Estadia(LocalDate fecha_inicio, LocalDate fecha_fin) {
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
    }
    
    
}
