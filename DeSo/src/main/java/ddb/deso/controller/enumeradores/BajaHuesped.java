package ddb.deso.controller.enumeradores;

/**
 * Enumerador de respuesta a front sobre estado de eliminacion
 */
public enum BajaHuesped {
    /**
     * Valor de retorno cuando el huesped no se dio de baja por tener estadias asociadas
     */
    OPERACION_PROHIBIDA,    // 0
    /**
     * Valor de retorno si el alojado se pudo dar de baja satisfactoriamente
     */
    DADO_DE_BAJA            // 1
}
