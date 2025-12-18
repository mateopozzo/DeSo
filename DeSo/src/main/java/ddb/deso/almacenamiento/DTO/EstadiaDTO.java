package ddb.deso.almacenamiento.DTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

import ddb.deso.negocio.habitaciones.Estadia;


/**
 * DTO de la clase {@link Estadia}
 */
@Data
public class EstadiaDTO {
    Long idEstadia;
    Long idHabitacion;
    Long idReserva;
    LocalDate fechaInicio, fechaFin;
    CriteriosBusq encargado;
    List<CriteriosBusq> listaInvitados;

    public EstadiaDTO() { }

    /**
     * Constructor en base a una entidad {@code estadia} que existe en el sistema
     * @param estadia
     */
    public EstadiaDTO(Estadia estadia) {
        this.idEstadia = estadia.getIdEstadia();
        this.fechaInicio = estadia.getFecha_inicio();
        this.fechaFin = estadia.getFecha_fin();
    }
    
}
