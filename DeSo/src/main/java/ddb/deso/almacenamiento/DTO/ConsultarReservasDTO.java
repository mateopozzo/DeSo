package ddb.deso.almacenamiento.DTO;

import lombok.Data;

@Data
public class ConsultarReservasDTO {
    Long IDHabitacion;
    String fechaInicio;
    String fechaFin;
}
