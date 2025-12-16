package ddb.deso.almacenamiento.DAO;
import java.util.List;

import ddb.deso.negocio.TipoDoc;
import ddb.deso.negocio.alojamiento.Alojado;
import ddb.deso.almacenamiento.DTO.CriteriosBusq;

/**
 * Contrato de persistencia para la entidad {@link Alojado}.
 * Define operaciones CRUD y consultas especializadas sobre el repositorio de datos.
 *
 * @author mat
 */
public interface AlojadoDAO {

    /**
     * Persiste una nueva instancia de Alojado en el repositorio.
     *
     * @param alojado Entidad con los datos a registrar. No debe ser {@code null}.
     */
    void crearAlojado(Alojado alojado);

    /**
     * Actualiza el estado de persistencia de un Alojado.
     *
     * @param alojadoPrev Referencia al estado anterior (utilizado para validación o concurrencia).
     * @param alojadoNuevo Entidad con el estado actualizado a persistir.
     */
    void actualizarAlojado(Alojado alojadoPrev, Alojado alojadoNuevo);

    /**
     * Elimina el registro asociado a la entidad proporcionada.
     *
     * @param alojado Entidad a eliminar del repositorio.
     */
    void eliminarAlojado(Alojado alojado);

    /**
     * Recupera una lista de Alojados que coinciden con los filtros proporcionados.
     *
     * @param criterios DTO conteniendo los filtros de búsqueda (e.g., atributos parciales).
     * @return Lista de coincidencias o lista vacía si no existen resultados.
     */
    List<Alojado> buscarAlojado(CriteriosBusq criterios);

    List<Alojado> buscarAlojado(long idEstadia);

    /**
     * Recupera la totalidad de los registros de Alojados activos.
     *
     * @return Lista completa de entidades persistidas.
     */
    List<Alojado> listarAlojados();

    /**
     * Busca una entidad única por su clave de negocio compuesta (Documento + Tipo).
     *
     * @param documento Número de documento identificador.
     * @param tipo Enumeración del tipo de documento {@link TipoDoc}.
     * @return La entidad {@link Alojado} encontrada o {@code null} si no existe.
     */
    Alojado buscarPorDNI(String documento, TipoDoc tipo);

    /**
     * Transiciona el estado de un registro existente a la categoría de Huésped
     * mediante su identificación.
     *
     * @param nroDoc Número de documento identificador.
     * @param tipoDoc Cadena de caracteres representando el tipo de documento.
     */
    void promoverAHuesped(String nroDoc, String tipoDoc);
}
