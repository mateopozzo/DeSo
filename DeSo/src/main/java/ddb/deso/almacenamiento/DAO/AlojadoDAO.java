package ddb.deso.almacenamiento.DAO;
import java.util.List;

import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.alojamiento.CriteriosBusq;

/**
 * Interfaz DAO (Data Access Object) que define las operaciones de acceso a datos
 * para la entidad {@link AlojadoDTO}.
 * <p>
 * Permite realizar las operaciones CRUD básicas y búsquedas según distintos
 * criterios relacionados con los huéspedes alojados.
 * </p>
 *
 * @author mat
 */
public interface AlojadoDAO {

    /**
     * Registra un nuevo huésped en el almacenamiento persistente.
     *
     * @param alojado objeto {@link AlojadoDTO} que contiene la información del huésped a registrar.
     */
    void crearAlojado(AlojadoDTO alojado);

    /**
     * Actualiza los datos de un huésped existente en el sistema.
     *
     * @param alojadoPrev datos actuales del huésped antes de la modificación.
     * @param alojadoNuevo nuevos datos a actualizar.
     */
    void actualizarAlojado(AlojadoDTO alojadoPrev, AlojadoDTO alojadoNuevo);

     /**
     * Elimina un huésped del almacenamiento persistente.
     *
     * @param alojado objeto {@link AlojadoDTO} que representa al huésped a eliminar.
     */
    void eliminarAlojado(AlojadoDTO alojado);

     /**
     * Busca huéspedes que cumplan con los criterios especificados.
     *
     * @param criterios objeto {@link CriteriosBusq} que define los parámetros de búsqueda (apellido, documento, etc.).
     * @return una lista de {@link AlojadoDTO} que cumplen con los criterios.
     */
    List<AlojadoDTO> buscarHuespedDAO(CriteriosBusq criterios);

     /**
     * Obtiene una lista completa de todos los huéspedes alojados registrados.
     *
     * @return lista de {@link AlojadoDTO}.
     */
    List<AlojadoDTO> listarAlojados();

    /**
     * Busca un huésped específico según su tipo y número de documento.
     *
     * @param documento número de documento a buscar.
     * @param tipo tipo de documento (por ejemplo, DNI, Pasaporte, etc.).
     * @return el {@link AlojadoDTO} correspondiente si se encuentra, o {@code null} en caso contrario.
     */
    AlojadoDTO buscarPorDNI(String documento, TipoDoc tipo);
}
