package ddb.deso.login.excepciones;

/**
 * Excepción que indica que la contraseña ingresada no coincide
 * con la registrada para el usuario.
 */
public class CredencialesInvalidasException extends Exception {
    public CredencialesInvalidasException() { 
        super("Credenciales inválidas");
    }
}
