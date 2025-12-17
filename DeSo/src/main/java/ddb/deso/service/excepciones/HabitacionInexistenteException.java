package ddb.deso.service.excepciones;

public class HabitacionInexistenteException extends RuntimeException {

    public HabitacionInexistenteException() {
    }

    public HabitacionInexistenteException(String message) {
        super(message);
    }

    public HabitacionInexistenteException(Throwable cause) {
        super(cause);
    }

    public HabitacionInexistenteException(String message, Throwable cause) {
        super(message, cause);
    }
}
