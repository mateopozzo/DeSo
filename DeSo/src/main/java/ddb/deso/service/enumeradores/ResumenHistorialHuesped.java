package ddb.deso.service.enumeradores;

/**
 * Enumerador que informa el estado del historial de un huésped en el sistema.
 */
public enum ResumenHistorialHuesped {
    /** Tuvo alguna estadía en el hotel. */
    SE_ALOJO,

    /** No tuvo ninguna estadía, pero sus datos están persistidos. */
    NO_SE_ALOJO,

    /** Sus datos no están presentes en la base del sistema. */
    NO_PERSISTIDO
}
