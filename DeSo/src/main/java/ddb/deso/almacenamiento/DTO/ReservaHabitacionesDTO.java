package ddb.deso.almacenamiento.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReservaHabitacionesDTO {
    private ReservaDTO reservaDTO;
    private List<Long> listaIDHabitaciones;
}
