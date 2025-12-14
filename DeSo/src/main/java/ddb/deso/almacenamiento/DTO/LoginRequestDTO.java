package ddb.deso.almacenamiento.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO de entrada para el CU01 (Autenticar Usuario).
 *
 * <p>Contiene las credenciales ingresadas por el actor (conserje).</p>
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDTO {
    
    /**
     * Nombre de usuario ingresado.
     */
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    /**
     * Contraseña ingresada.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
