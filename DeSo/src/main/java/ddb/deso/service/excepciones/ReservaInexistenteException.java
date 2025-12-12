package ddb.deso.gestores.excepciones;

/**
 * Excepción lanzada cuando se intenta operar sobre una {@code Reserva}
 * inexistente en la base de datos (por ejemplo, al cancelar una reserva por ID).
 */
public class ReservaInexistenteException extends RuntimeException {

    /**
     * Crea una excepción con un mensaje descriptivo.
     *
     * @param message mensaje de error.
     */
    public ReservaInexistenteException(String message) {
        super(message);
    }
}
