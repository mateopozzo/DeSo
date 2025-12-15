package ddb.deso.service;

import org.springframework.stereotype.Service;

import ddb.deso.almacenamiento.DAO.UsuarioDAO;
import ddb.deso.negocio.login.Usuario;
import ddb.deso.negocio.login.excepciones.CredencialesInvalidasException;

/**
 * Gestor del CU01 (Autenticar Usuario).
 *
 * <p>Responsable de verificar las credenciales ingresadas por el actor contra la fuente
 * de datos (BD vía {@link UsuarioDAO}).</p>
 *
 * <p><b>Decisión de diseño:</b> este caso de uso no crea ni modifica usuarios, por lo que
 * se asume que las credenciales persistidas en la base cumplen la política de contraseñas.
 * El gestor únicamente valida existencia de usuario y coincidencia de contraseña.</p>
 *
 * <p><b>Regla del CU01:</b> ante cualquier falla de autenticación (usuario inexistente o
 * contraseña incorrecta) se devuelve un mensaje único: “El usuario o la contraseña no son válidos”.
 * Para ello se lanza siempre {@link CredencialesInvalidasException} sin filtrar el motivo.</p>
 */
@Service
public class GestorAutenticacion {

    private final UsuarioDAO usuarioDAO;

    /**
     * Crea el gestor de autenticación.
     *
     * @param usuarioDAO DAO de usuarios (inyección por Spring).
     */
    public GestorAutenticacion(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Autentica un usuario en base a nombre y contraseña.
     *
     * @param nombre nombre de usuario ingresado (puede venir con espacios).
     * @param password contraseña ingresada.
     * @return {@link Usuario} autenticado si las credenciales son válidas.
     * @throws CredencialesInvalidasException si el usuario no existe o la contraseña no coincide.
     */
    public Usuario autenticar(String nombre, String password) throws CredencialesInvalidasException {
        String n = (nombre == null) ? "" : nombre.trim();
        String p = (password == null) ? "" : password;

        var usuarioOpt = usuarioDAO.buscarPorNombre(n);

        if (usuarioOpt.isEmpty()) throw new CredencialesInvalidasException();

        Usuario u = usuarioOpt.get();
        if (!u.coincidePasswordCon(p)) throw new CredencialesInvalidasException();

        return u;
    }
}