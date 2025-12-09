package ddb.deso.almacenamiento.DTO;

import lombok.Data;

@Data
public class ConsultarReservasDTO {
    Long idHabitacion;
    String fechaInicio;
    String fechaFin;
}
