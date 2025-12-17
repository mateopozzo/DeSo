package ddb.deso.almacenamiento.DTO;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para el CU06 (grilla de reservas encontradas).
 *
 * <p>Representa una reserva completa con su lista de habitaciones, de modo que
 * la cancelación se realice a nivel de reserva (no por habitación).</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaGrillaDTO {
    private Long idReserva;

    private String apellido;
    private String nombre;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private List<HabitacionReservaDTO> habitaciones;
}
