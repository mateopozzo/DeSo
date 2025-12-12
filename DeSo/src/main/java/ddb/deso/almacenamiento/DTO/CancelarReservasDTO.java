package ddb.deso.almacenamiento.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para solicitar la cancelación de una o varias reservas (CU06).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CancelarReservasDTO {
    private List<Long> idsReserva;
}
