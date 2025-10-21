package ddb.deso.login.negocio;

import java.util.Optional;

import ddb.deso.login.Usuario;
import ddb.deso.login.dao.UsuarioDAO;
import ddb.deso.login.negocio.excepciones.CredencialesInvalidasException;
import ddb.deso.login.negocio.excepciones.UsuarioNoEncontradoException;

public class GestorAutenticacion {
    private final UsuarioDAO usuarioDAO;

    public GestorAutenticacion(UsuarioDAO usuarioDAO) { 
        this.usuarioDAO = usuarioDAO; 
    }

    public Usuario autenticar(String nombre, String password) throws UsuarioNoEncontradoException, CredencialesInvalidasException {
        
        String n = (nombre == null) ? "" : nombre.trim();
        String p = (password == null) ? "" : password;

        Optional<Usuario> usuarioEncontrado  = usuarioDAO.buscarPorNombre(n);
        if (usuarioEncontrado .isEmpty()) {
            throw new UsuarioNoEncontradoException("No existe usuario: " + n);
        }

        Usuario u = usuarioEncontrado .get();
        if (!u.coincidePasswordCon(p)) {
            throw new CredencialesInvalidasException();
        }
        return u;
    }
}
