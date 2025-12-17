package ddb.deso.service.excepciones;

public class ReservaInvalidaException extends RuntimeException {
    public ReservaInvalidaException(String message) {
        super(message);
    }
}
