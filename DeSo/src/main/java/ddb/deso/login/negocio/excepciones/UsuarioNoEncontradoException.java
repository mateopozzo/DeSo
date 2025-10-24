package ddb.deso.login.negocio.excepciones;

/**
 * Excepción que indica que no existe un usuario con el nombre especificado.
 */
public class UsuarioNoEncontradoException extends Exception {
    public UsuarioNoEncontradoException(String nombre) { 
        super("No existe usuario: " + nombre); 
    }
}
