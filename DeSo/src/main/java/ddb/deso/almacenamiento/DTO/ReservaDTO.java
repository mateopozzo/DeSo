package ddb.deso.almacenamiento.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


/**
 * DTO que contiene la informacion de una {@link ddb.deso.negocio.habitaciones.Reserva}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private String nombre;
    private String apellido;
    private String telefono;
    private String estado;
}
