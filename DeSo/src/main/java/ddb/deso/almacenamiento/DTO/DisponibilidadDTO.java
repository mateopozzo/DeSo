package ddb.deso.almacenamiento.DTO;

import ddb.deso.negocio.EstadoHab;
import ddb.deso.negocio.TipoHab;
import ddb.deso.negocio.habitaciones.Estadia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO destinado a la comunicación de {@link ddb.deso.negocio.habitaciones.Reserva} o {@link Estadia} en su rango de fecha
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadDTO {
    public DisponibilidadDTO(Estadia estadia) {
        this.tipo = estadia.getHabitacion().getTipo_hab();
        this.idHabitacion = estadia.getHabitacion().getNroHab();
        this.fecha_inicio = estadia.getFecha_inicio();
        this.fecha_fin = estadia.getFecha_fin();
        this.estado = EstadoHab.OCUPADA;
    }

    TipoHab tipo;   // tipo de habitacion
    Long idHabitacion; // id unico de la habitacion
    LocalDate fecha_inicio; // fecha de inicio de la estadia/reserva
    LocalDate fecha_fin; // fecha de fin
    EstadoHab estado; // estadodo en el que se encuentra la habitacion en el rango de fechas
}
