package ddb.deso.almacenamiento.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO que asocia una reserva con las habitaciones que toma
 * <p>
 *     Actualmente en el sistema una reserva puede tomar una sola habitacion, pero este comportamiento se podría
 *     extender refactorizando el CU05 desde el front
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor
public class ReservaHabitacionesDTO {
    private ReservaDTO reservaDTO;
    private List<Long> listaIDHabitaciones;
}
