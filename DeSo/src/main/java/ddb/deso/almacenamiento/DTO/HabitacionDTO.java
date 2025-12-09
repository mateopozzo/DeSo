package ddb.deso.almacenamiento.DTO;

import ddb.deso.service.EstadoHab;
import ddb.deso.service.TipoHab;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitacionDTO {
    private Long nroHab;
    private TipoHab tipo_hab;
    private EstadoHab estado_hab;
}
