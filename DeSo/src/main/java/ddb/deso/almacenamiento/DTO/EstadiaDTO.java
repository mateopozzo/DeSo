package ddb.deso.almacenamiento.DTO;
import ddb.deso.alojamiento.CriteriosBusq;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data

public class EstadiaDTO {
    Long idHabitacion;
    LocalDate fechaInicio, fechaFin;
    CriteriosBusq encargado;
    List<CriteriosBusq> listaInvitados;
}
