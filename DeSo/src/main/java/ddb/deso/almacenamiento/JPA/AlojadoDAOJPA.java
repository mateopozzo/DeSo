package ddb.deso.almacenamiento.JPA;

import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.alojamiento.*;
import ddb.deso.repository.AlojadoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación de AlojadoDAO que utiliza Spring Data JPA para la persistencia.
 * Actúa como un adaptador entre la interfaz DAO y los repositorios JPA.
 *
 * NOTA: Para que 'buscarHuespedDAO' funcione, AlojadoRepository DEBE extender
 * JpaSpecificationExecutor<Alojado, AlojadoID>
 *
 * Ejemplo:
 * public interface AlojadoRepository extends JpaRepository<Alojado, AlojadoID>, JpaSpecificationExecutor<Alojado> { }
 *
 */
@Repository
public class AlojadoDAOJPA implements AlojadoDAO {

    private final AlojadoRepository alojadoRepository;

    /**
     * Inyección de dependencias por constructor.
     * Spring se encargará de "inyectar" una instancia de AlojadoRepository aquí.
     */
    public AlojadoDAOJPA(AlojadoRepository alojadoRepository) {
        this.alojadoRepository = alojadoRepository;
    }

    @Override
    public void crearAlojado(AlojadoDTO alojado) {
        Alojado entidad = FactoryAlojado.createFromDTO(alojado);

        // 2. Persiste la entidad usando el repositorio
        if (entidad != null) {
            alojadoRepository.save(entidad);
        }
    }

    @Override
    public void actualizarAlojado(AlojadoDTO alojadoPrev, AlojadoDTO alojadoNuevo) {
        // 1. Convierte el DTO con los *nuevos datos* a una entidad
        Alojado entidadModificada = FactoryAlojado.createFromDTO(alojadoNuevo);

        // 2. save() de JPA actúa como un "merge":
        // Si la entidad tiene un ID que ya existe, la actualiza.
        // Si no, la inserta. (Asumimos que siempre tendrá ID)
        if (entidadModificada != null && entidadModificada.getDatos().getIdAlojado() != null) {
            alojadoRepository.save(entidadModificada);
        }
    }

    @Override
    public void eliminarAlojado(AlojadoDTO alojado) {
        // Es más eficiente construir el ID y usar deleteById
        if (alojado != null && alojado.getTipoDoc() != null && alojado.getNroDoc() != null) {
            AlojadoID id = new AlojadoID(alojado.getNroDoc(), alojado.getTipoDoc());

            // Solo intenta borrar si existe, para evitar excepciones
            if (alojadoRepository.existsById(id)) {
                alojadoRepository.deleteById(id);
            }
        }
    }

    @Override
    public List<AlojadoDTO> listarAlojados() {
        // 1. Obtiene todas las entidades de la BD
        List<Alojado> entidades = alojadoRepository.findAll();

        // 2. Convierte la lista de Entidades a una lista de DTOs
        //    (Esto asume que AlojadoDTO tiene un constructor que acepta Alojado)
        return entidades.stream()
                .map(AlojadoDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public AlojadoDTO buscarPorDNI(String documento, TipoDoc tipo) {
        if (documento == null || tipo == null) return null;

        // 1. Construye el ID compuesto
        AlojadoID id = new AlojadoID(documento, tipo);

        // 2. Busca por ID. Devuelve un Optional<Alojado>
        Optional<Alojado> entidadOptional = alojadoRepository.findById(id);

        // 3. Mapea el Optional:
        //    - Si la entidad está presente, la convierte a DTO.
        //    - Si no, devuelve null.
        return entidadOptional.map(AlojadoDTO::new)
                .orElse(null);
    }

    @Override
    public List<AlojadoDTO> buscarHuespedDAO(CriteriosBusq criterios) {

        // --- Optimización: Si los criterios incluyen el ID completo, usar la búsqueda por ID ---
        if (criterios.getNroDoc() != null && !criterios.getNroDoc().isBlank() && criterios.getTipoDoc() != null) {
            AlojadoDTO dto = this.buscarPorDNI(criterios.getNroDoc(), criterios.getTipoDoc());
            return (dto != null) ? List.of(dto) : List.of(); // Devuelve lista con 1 o 0 elementos
        }

        // --- Búsqueda dinámica con JPA Specifications ---
        // Esto crea una consulta SQL dinámica (WHERE ... AND ... AND ...)
        Specification<Alojado> spec = (root, query, cb) -> {

            // Necesitamos hacer "JOIN" en la consulta para acceder a las tablas anidadas
            // Alojado -> DatosAlojado
            Join<Alojado, DatosAlojado> datosAlojado = root.join("datos");
            // DatosAlojado -> DatosPersonales
            Join<DatosAlojado, DatosPersonales> datosPersonales = datosAlojado.join("datos_personales");

            List<Predicate> predicates = new ArrayList<>();

            // Añadir predicados (condiciones WHERE) solo si el criterio no es nulo/vacío
            if (criterios.getApellido() != null && !criterios.getApellido().isBlank()) {
                predicates.add(cb.like(cb.lower(datosPersonales.get("apellido")), "%" + criterios.getApellido().toLowerCase() + "%"));
            }
            if (criterios.getNombre() != null && !criterios.getNombre().isBlank()) {
                predicates.add(cb.like(cb.lower(datosPersonales.get("nombre")), "%" + criterios.getNombre().toLowerCase() + "%"));
            }

            // Los campos de ID están en la entidad DatosAlojado (en el EmbeddedId idAlojado)
            if (criterios.getNroDoc() != null && !criterios.getNroDoc().isBlank()) {
                predicates.add(cb.equal(datosAlojado.get("idAlojado").get("nroDoc"), criterios.getNroDoc()));
            }
            if (criterios.getTipoDoc() != null) {
                predicates.add(cb.equal(datosAlojado.get("idAlojado").get("tipoDoc"), criterios.getTipoDoc()));
            }

            // Combina todos los predicados con un "AND"
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        // 1. Ejecuta la búsqueda con la especificación dinámica
        //    (Requiere que AlojadoRepository extienda JpaSpecificationExecutor)
        List<Alojado> entidades = alojadoRepository.findAll(spec);

        // 2. Convierte los resultados a DTO
        return entidades.stream()
                .map(AlojadoDTO::new)
                .collect(Collectors.toList());
    }
}