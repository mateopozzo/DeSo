package ddb.deso.almacenamiento.DAO;
import java.util.List;

import ddb.deso.TipoDoc;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.CriteriosBusq;

/**
 * Interfaz DAO (Data Access Object) que define las operaciones de acceso a datos
 * para la entidad {@link Alojado}.
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
     * @param alojado objeto {@link Alojado} que contiene la información del huésped a registrar.
     */
    void crearAlojado(Alojado alojado);

    /**
     * Actualiza los datos de un huésped existente en el sistema.
     *
     * @param alojadoPrev datos actuales del huésped antes de la modificación.
     * @param alojadoNuevo nuevos datos a actualizar.
     */
    void actualizarAlojado(Alojado alojadoPrev, Alojado alojadoNuevo);

     /**
     * Elimina un huésped del almacenamiento persistente.
     *
     * @param alojado objeto {@link Alojado} que representa al huésped a eliminar.
     */
    void eliminarAlojado(Alojado alojado);

     /**
     * Busca huéspedes que cumplan con los criterios especificados.
     *
     * @param criterios objeto {@link CriteriosBusq} que define los parámetros de búsqueda (apellido, documento, etc.).
     * @return una lista de {@link Alojado} que cumplen con los criterios.
     */
    List<Alojado> buscarAlojadoDAO(CriteriosBusq criterios);

     /**
     * Obtiene una lista completa de todos los huéspedes alojados registrados.
     *
     * @return lista de {@link Alojado}.
     */
    List<Alojado> listarAlojados();

    /**
     * Busca un huésped específico según su tipo y número de documento.
     *
     * @param documento número de documento a buscar.
     * @param tipo tipo de documento (por ejemplo, DNI, Pasaporte, etc.).
     * @return el {@link Alojado} correspondiente si se encuentra, o {@code null} en caso contrario.
     */
    Alojado buscarPorDNI(String documento, TipoDoc tipo);
}
