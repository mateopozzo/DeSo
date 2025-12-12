package ddb.deso.almacenamiento.DTO;

import ddb.deso.negocio.EstadoHab;
import ddb.deso.negocio.TipoHab;
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
