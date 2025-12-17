package ddb.deso.almacenamiento.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de entrada para el CU06 - Cancelar Reserva.
 * Contiene los criterios de búsqueda ingresados por el conserje.
 *
 * <p>Regla del CU: el {@code apellido} es obligatorio (excluyente). El {@code nombre} es opcional.</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuscarReservaDTO {
    private String apellido; // obligatorio
    private String nombre;   // opcional
}
