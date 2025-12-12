package ddb.deso.almacenamiento.DTO;

import ddb.deso.negocio.TipoHab;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO auxiliar para representar una habitación asociada a una reserva,
 * usado en la grilla del CU06.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabitacionReservaDTO {
    private Long numeroHabitacion;
    private TipoHab tipoHabitacion;
}