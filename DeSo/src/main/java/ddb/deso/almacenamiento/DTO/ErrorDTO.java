package ddb.deso.almacenamiento.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO genérico para respuestas de error en la API REST.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {
    private String message;
}
