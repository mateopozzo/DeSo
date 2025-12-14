package ddb.deso.almacenamiento.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para el CU01 (Autenticar Usuario).
 *
 * <p>Devuelve los datos mínimos del usuario autenticado, incluyendo permisos.</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private Long id;
    private String nombre;
    private Integer permisos;
}
