package ddb.deso.almacenamiento.DTO;

import ddb.deso.habitaciones.Habitacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ReservaDTO {
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    //Proposicion de cambio, es inutil (e imposible) guardar alojados sin documento
//    private String nombre;
//    private String apellido;
//    private String telefono;
    private String estado;
}
