package ddb.deso.almacenamiento.DTO;

import ddb.deso.EstadoHab;
import ddb.deso.TipoHab;
import ddb.deso.service.habitaciones.Estadia;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO destinado a la comunicación de reservas y estadias en su rango de fecha
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

    TipoHab tipo;
    Long idHabitacion;
    LocalDate fecha_inicio;
    LocalDate fecha_fin;
    EstadoHab estado;
}
