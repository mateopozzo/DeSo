package ddb.deso.almacenamiento.DTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data

public class EstadiaDTO {
    Long idHabitacion;
    Long idReserva;
    LocalDate fechaInicio, fechaFin;
    CriteriosBusq encargado;
    List<CriteriosBusq> listaInvitados;
}
