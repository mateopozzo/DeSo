package ddb.deso.service;

import java.util.Optional;

import ddb.deso.almacenamiento.DAO.UsuarioDAO;
import ddb.deso.negocio.login.Sesion;
import ddb.deso.negocio.login.Usuario;
import ddb.deso.negocio.login.excepciones.CredencialesInvalidasException;
import ddb.deso.negocio.login.excepciones.UsuarioNoEncontradoException;

/**
 * Clase encargada de gestionar el proceso de autenticación de usuarios.
 * Se comunica con la capa de acceso a datos (DAO) para verificar credenciales.
 */
public class GestorAutenticacion {
    private final UsuarioDAO usuarioDAO;

    public GestorAutenticacion(UsuarioDAO usuarioDAO) { 
        this.usuarioDAO = usuarioDAO; 
    }

    /**
     Verifica las credenciales ingresadas por el usuario.
     <p>
     Si el usuario no existe o la contraseña no coincide, lanza
     excepciones específicas de negocio.

     @param nombre Nombre del usuario que intenta autenticarse.
     @param password Contraseña ingresada.
     @return El objeto {@link Usuario} correspondiente si la autenticación es exitosa.
     @throws UsuarioNoEncontradoException Si el nombre no existe en el sistema.
     @throws CredencialesInvalidasException Si la contraseña no coincide.
     */

    public Usuario autenticar(String nombre, String password) throws UsuarioNoEncontradoException, CredencialesInvalidasException {
        
        String n = (nombre == null) ? "" : nombre.trim();
        String p = (password == null) ? "" : password;

        Optional<Usuario> usuarioEncontrado  = usuarioDAO.buscarPorNombre(n);
        if (usuarioEncontrado.isEmpty()) {
            throw new UsuarioNoEncontradoException(n);
        }

        Usuario u = usuarioEncontrado.get();

        if (!u.coincidePasswordCon(p)) {
            throw new CredencialesInvalidasException();
        }

        Sesion.iniciarSesion(u);
        return Sesion.getUsuarioLogueado();
    }
}
