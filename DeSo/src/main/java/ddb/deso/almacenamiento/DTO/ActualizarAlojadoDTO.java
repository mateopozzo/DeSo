package ddb.deso.almacenamiento.DTO;

/**
 * Estructura de intercambio de datos dentre Front-end y {@link ddb.deso.controller.AlojadoController}.
 * Para la actualizacion de registros {@link ddb.deso.negocio.alojamiento.Alojado}
 * Contiene los datos del {@link AlojadoDTO} que ese desea modificar, y una segunda entidad, modificada
 */
public class ActualizarAlojadoDTO {
    public AlojadoDTO pre;
    public AlojadoDTO post;
}
