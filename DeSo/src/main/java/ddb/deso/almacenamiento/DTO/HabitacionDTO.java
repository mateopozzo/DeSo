package ddb.deso.almacenamiento.DTO;

import ddb.deso.EstadoHab;
import ddb.deso.TipoHab;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionDTO {
    private int nroHab;
    private TipoHab tipo_hab;
    private EstadoHab estado_hab;
}
