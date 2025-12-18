package ddb.deso.almacenamiento.DTO;

import lombok.Data;


/**
 * DTO utilizado en CU05 para que Front - end consulte el estado de habitaciones
 */
@Data
public class ConsultarReservasDTO {
    /** id de alguna habitacion que se encuentra reservada */
    Long idHabitacion;
    /** Fecha inicio de la reserva de {@idHabitacion} */
    String fechaInicio;
    /** Fecha fin de la reserva de {@idHabitacion} */
    String fechaFin;
}
