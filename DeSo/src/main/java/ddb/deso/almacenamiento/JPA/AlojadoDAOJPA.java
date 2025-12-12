package ddb.deso.almacenamiento.JPA;

import ddb.deso.negocio.TipoDoc;
import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DTO.CriteriosBusq;
import ddb.deso.negocio.alojamiento.*;
import ddb.deso.repository.AlojadoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Path;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementación JPA de {@link AlojadoDAO}.
 * Utiliza {@link Specification} para construcción dinámica de consultas SQL
 * y maneja la normalización de caracteres para búsquedas insensibles a acentos.
 */
@Repository
public class AlojadoDAOJPA implements AlojadoDAO {

    private final AlojadoRepository alojadoRepository;
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Inyección de dependencias por constructor.
     * Spring se encargará de "inyectar" una instancia de AlojadoRepository aquí.
     */
    public AlojadoDAOJPA(AlojadoRepository alojadoRepository) {
        this.alojadoRepository = alojadoRepository;
    }

    /**
     * Guarda un nuevo alojado en la base de datos.
     * @param alojado Entidad Alojado a persistir.
     */
    @Override
    public void crearAlojado(Alojado alojado) {
        if (alojado != null) {
            alojadoRepository.save(alojado);
        }
    }

    /**
     * Actualiza un alojado existente.
     * @param alojadoPrev El alojado con los datos originales (usado para verificar ID).
     * @param alojadoNuevo El objeto con los nuevos datos (actualmente no se usa explícitamente en la lógica, se guarda el prev).
     */
    @Override
    public void actualizarAlojado(Alojado alojadoPrev, Alojado alojadoNuevo) {
        if (alojadoPrev != null && alojadoPrev.getDatos().getIdAlojado() != null) {
            alojadoRepository.save(alojadoPrev);
        }
    }

    /**
     * Elimina un alojado de la base de datos.
     * Construye el ID compuesto {@link AlojadoID} a partir de los datos del objeto
     * y verifica su existencia antes de eliminar.
     *
     * @param alojado El objeto alojado que contiene los datos clave (tipo y nro doc) para la eliminación.
     */
    @Override
    public void eliminarAlojado(Alojado alojado) {
        // Es más eficiente construir el ID y usar deleteById
        if (alojado != null && alojado.getDatos().getTipoDoc() != null && alojado.getDatos().getNroDoc() != null) {
            AlojadoID id = new AlojadoID(alojado.getDatos().getNroDoc(), alojado.getDatos().getTipoDoc());
            // Solo intenta borrar si existe, para evitar excepciones
            if (alojadoRepository.existsById(id)) {
                alojadoRepository.deleteById(id);
            }
        }
        alojadoRepository.flush();
    }

    @Override
    public List<Alojado> listarAlojados() {
        // 1. Obtiene todas las entidades de la BD
        return alojadoRepository.findAll();
    }

    /**
     * Busca un alojado específico por su clave primaria compuesta.
     * @param documento Número de documento.
     * @param tipo Tipo de documento.
     * @return El alojado encontrado o {@code null}.
     */
    @Override
    public Alojado buscarPorDNI(String documento, TipoDoc tipo) {
        if (documento == null || tipo == null) return null;

        AlojadoID id = new AlojadoID(documento, tipo);

        Optional<Alojado> entidadOptional = alojadoRepository.findById(id);

        return entidadOptional.orElse(null);
    }

    /**
     * Ejecuta la lógica para promover un Alojado a Huésped en la base de datos.
     * Realiza un flush y limpia el EntityManager para asegurar consistencia.
     * @param nroDoc Número de documento del alojado.
     * @param tipoDoc Tipo de documento del alojado.
     */
    @Override
    public void promoverAHuesped(String nroDoc, String tipoDoc) {
        alojadoRepository.promoverAHuesped(nroDoc, tipoDoc);
        alojadoRepository.flush();
        entityManager.clear();
    }

    /**
     * Realiza una búsqueda dinámica de alojados basada en múltiples criterios.
     * Si se proporciona documento y tipo, realiza búsqueda directa por ID.
     * De lo contrario, construye una consulta dinámica (Specification) que permite
     * buscar por nombre y apellido (insensible a mayúsculas y acentos) usando la función 'unaccent'.
     *
     * @param criterios Objeto DTO con los filtros de búsqueda (nombre, apellido, dni, tipo).
     * @return Lista de alojados que cumplen con los criterios.
     */
    @Override
    public List<Alojado> buscarAlojado(CriteriosBusq criterios) {

        // Si el Documento esta entero se aprovecha
        if (criterios.getNroDoc() != null && !criterios.getNroDoc().isBlank() && criterios.getTipoDoc() != null) {
            Alojado alojado = this.buscarPorDNI(criterios.getNroDoc(), criterios.getTipoDoc());
            return (alojado != null) ? List.of(alojado) : List.of(); // Devuelve lista con 1 o 0 elementos
        }

        // Búsqueda dinámica con JPA Specifications
        // Esto crea una consulta SQL dinámica
        Specification<Alojado> spec = (root, query, cb) -> {

            // Alojado join DatosAlojado
            Join<Alojado, DatosAlojado> datosAlojado = root.join("datos");
            // DatosAlojado join DatosPersonales
            Path<DatosPersonales> datosPersonales = datosAlojado.get("datos_personales");

            List<Predicate> predicates = new ArrayList<>();

            if (criterios.getApellido() != null && !criterios.getApellido().isBlank()) {
                String patron = normalizar(criterios.getApellido()).toLowerCase() + "%";
                predicates.add(cb.like(cb.function(
                        "unaccent",  String.class,
                        cb.lower(datosPersonales.get("apellido"))), patron));
            }
            if (criterios.getNombre() != null && !criterios.getNombre().isBlank()) {
                String patron = normalizar(criterios.getNombre()).toLowerCase() + "%";
                predicates.add(cb.like(cb.function(
                        "unaccent", String.class,
                        cb.lower(datosPersonales.get("nombre"))), patron));
            }

            if (criterios.getNroDoc() != null && !criterios.getNroDoc().isBlank()) {
                predicates.add(cb.equal(datosAlojado.get("idAlojado").get("nroDoc"), criterios.getNroDoc()));
            }
            if (criterios.getTipoDoc() != null) {
                predicates.add(cb.equal(datosAlojado.get("idAlojado").get("tipoDoc"), criterios.getTipoDoc()));
            }

            // Combina predicados con ans
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return alojadoRepository.findAll(spec);
    }

    /**
     * Normaliza una cadena de texto eliminando acentos y diacríticos.
     * Utiliza la forma de normalización NFD y expresiones regulares ASCII.
     *
     * @param cadena String que se necesita normalizar.
     * @return Una cadena normalizada sin tildes.
     * @see <a href="https://stackoverflow.com/questions/4122170/java-change-%C3%A1%C3%A9%C5%91%C5%B1%C3%BA-to-aeouu">Fuente Original</a>
     */

    private String normalizar (String cadena){
        return Normalizer.normalize(cadena, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    private boolean no_es_vacio (String contenido){
        boolean flag = (contenido==null || contenido.isEmpty());
        return !flag;
    }

}