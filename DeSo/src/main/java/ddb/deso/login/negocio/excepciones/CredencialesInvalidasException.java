package ddb.deso.login.negocio.excepciones;

public class CredencialesInvalidasException extends Exception {
    public CredencialesInvalidasException() { 
        super("Credenciales inválidas");
    }
}
