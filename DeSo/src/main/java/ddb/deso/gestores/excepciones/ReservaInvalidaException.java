package ddb.deso.gestores.excepciones;

public class ReservaInvalidaException extends RuntimeException {
    public ReservaInvalidaException(String message) {
        super(message);
    }
}
