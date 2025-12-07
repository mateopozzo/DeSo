package ddb.deso.almacenamiento.JPA;

import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.service.alojamiento.*;
import ddb.deso.repository.AlojadoRepository;
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
 * Implementación de AlojadoDAO que utiliza Spring Data JPA para la persistencia
 * Actúa como un adaptador entre la interfaz DAO y los repositorios JPA
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
    public void crearAlojado(Alojado alojado) {
        if (alojado != null) {
            alojadoRepository.save(alojado);
        }
    }

    @Override
    public void actualizarAlojado(Alojado alojadoPrev, Alojado alojadoNuevo) {
        if (alojadoPrev != null && alojadoPrev.getDatos().getIdAlojado() != null) {
            alojadoRepository.save(alojadoPrev);
        }
    }

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
    }

    @Override
    public List<Alojado> listarAlojados() {
        // 1. Obtiene todas las entidades de la BD
        return alojadoRepository.findAll();
    }

    @Override
    public Alojado buscarPorDNI(String documento, TipoDoc tipo) {
        if (documento == null || tipo == null) return null;

        AlojadoID id = new AlojadoID(documento, tipo);

        Optional<Alojado> entidadOptional = alojadoRepository.findById(id);

        return entidadOptional.orElse(null);
    }

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

            // "JOIN" en la consulta para acceder a las tablas anidadas
            // Alojado -> DatosAlojado
            Join<Alojado, DatosAlojado> datosAlojado = root.join("datos");
            // DatosAlojado -> DatosPersonales
            Path<DatosPersonales> datosPersonales = datosAlojado.get("datos_personales");

            List<Predicate> predicates = new ArrayList<>();

            if (criterios.getApellido() != null && !criterios.getApellido().isBlank()) {
                predicates.add(cb.like(cb.lower(datosPersonales.get("apellido")), "%" + normalizar(criterios.getApellido().toLowerCase()) + "%"));
            }
            if (criterios.getNombre() != null && !criterios.getNombre().isBlank()) {
                String patron = normalizar(criterios.getNombre().toLowerCase()) + "%";
                predicates.add(cb.like(cb.lower(datosPersonales.get("nombre")), patron));
            }

            if (criterios.getNroDoc() != null && !criterios.getNroDoc().isBlank()) {
                predicates.add(cb.equal(datosAlojado.get("idAlojado").get("nroDoc"), criterios.getNroDoc()));
            }
            if (criterios.getTipoDoc() != null) {
                predicates.add(cb.equal(datosAlojado.get("idAlojado").get("tipoDoc"), criterios.getTipoDoc()));
            }

            // Combina todos los predicados con un "AND"
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return alojadoRepository.findAll(spec);
    }

    /**
     @param cadena: String que necesito normalizar
     @return una cadena normalizada sin tildes

     FUENTE: https://stackoverflow.com/questions/4122170/java-change-%C3%A1%C3%A9%C5%91%C5%B1%C3%BA-to-aeouu
     */

    private String normalizar (String cadena){
        return Normalizer.normalize(cadena, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    private boolean no_es_vacio (String contenido){
        boolean flag = (contenido==null || contenido.isEmpty());
        return !flag;
    }

}