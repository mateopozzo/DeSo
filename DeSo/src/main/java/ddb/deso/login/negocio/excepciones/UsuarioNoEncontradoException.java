package ddb.deso.login.negocio.excepciones;

public class UsuarioNoEncontradoException extends Exception {
    public UsuarioNoEncontradoException(String nombre) { 
        super("No existe usuario: " + nombre); 
    }
}
