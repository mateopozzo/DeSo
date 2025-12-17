package ddb.deso.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ddb.deso.almacenamiento.DTO.ErrorDTO;
import ddb.deso.almacenamiento.DTO.LoginRequestDTO;
import ddb.deso.almacenamiento.DTO.LoginResponseDTO;
import ddb.deso.negocio.login.Usuario;
import ddb.deso.negocio.login.excepciones.CredencialesInvalidasException;
import ddb.deso.negocio.login.excepciones.UsuarioNoEncontradoException;
import ddb.deso.service.GestorAutenticacion;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

/**
 * Controller REST para el CU01 (Autenticar Usuario).
 *
 * <p>Expone endpoints para:
 * <ul>
 *   <li>Iniciar sesión (login)</li>
 *   <li>Cerrar sesión (logout)</li>
 *   <li>Consultar usuario autenticado (me)</li>
 * </ul>
 *
 * <p>La sesión del usuario se mantiene mediante {@link HttpSession}
 * (sesión por cliente), evitando el uso de un Singleton global.</p>
 */
@RestController
public class UsuarioController {

    private final GestorAutenticacion gestorAutenticacion;

    public UsuarioController(GestorAutenticacion gestorAutenticacion) {
        this.gestorAutenticacion = gestorAutenticacion;
    }

    /**
     * Autentica al usuario y crea una sesión web si las credenciales son válidas.
     *
     * @param dto credenciales de login.
     * @param session sesión HTTP asociada al cliente.
     * @return datos del usuario autenticado.
     * @throws CredencialesInvalidasException si el usuario o contraseña no son válidos.
     */
    @PostMapping("/api/auth/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto,
                                                  HttpSession session)
            throws CredencialesInvalidasException, UsuarioNoEncontradoException {

        Usuario u = gestorAutenticacion.autenticar(dto.getNombre(), dto.getPassword());

        // “Sesión” web (por navegador/cliente)
        session.setAttribute("USER_ID", u.getId());
        session.setAttribute("USER_NOMBRE", u.getNombre());
        session.setAttribute("USER_PERMISOS", u.getPermisos());

        return ResponseEntity.ok(new LoginResponseDTO(u.getId(), u.getNombre(), u.getPermisos()));
    }

    /**
     * Finaliza la sesión del usuario.
     *
     * @param session sesión HTTP.
     * @return 204 No Content.
     */
    @PostMapping("/api/auth/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.noContent().build();
    }

    /**
     * Devuelve el usuario actualmente autenticado en la sesión.
     *
     * @param session sesión HTTP.
     * @return 200 con usuario si hay sesión, o 401 si no hay usuario autenticado.
     */
    @GetMapping("/api/auth/me")
    public ResponseEntity<LoginResponseDTO> me(HttpSession session) {
        Long id = (Long) session.getAttribute("USER_ID");
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String nombre = (String) session.getAttribute("USER_NOMBRE");
        Integer permisos = (Integer) session.getAttribute("USER_PERMISOS");

        return ResponseEntity.ok(new LoginResponseDTO(id, nombre, permisos));
    }

    /**
     * Maneja errores de autenticación devolviendo el mensaje requerido por el CU01.
     */
    @ExceptionHandler({ UsuarioNoEncontradoException.class, CredencialesInvalidasException.class })
    public ResponseEntity<ErrorDTO> authError() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDTO("El usuario o la contraseña no son válidos"));
    }

    /**
     * Maneja errores de validación del DTO (campos vacíos/null) sin filtrar detalles.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> dtoInvalido() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDTO("El usuario o la contraseña no son válidos"));
    }
}
