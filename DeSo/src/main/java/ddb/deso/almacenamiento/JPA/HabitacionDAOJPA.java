package ddb.deso.almacenamiento.JPA;

import ddb.deso.service.TipoHab;
import ddb.deso.almacenamiento.DAO.HabitacionDAO;
import ddb.deso.service.habitaciones.Habitacion;
import ddb.deso.repository.HabitacionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementación JPA de {@link HabitacionDAO}.
 * Gestiona la persistencia de las entidades {@link Habitacion} utilizando {@link HabitacionRepository}.
 */
@Repository
public class HabitacionDAOJPA implements HabitacionDAO {

    private final HabitacionRepository habitacionRepository;

    /**
     * Constructor para la inyección de dependencias.
     * @param habitacionRepository Repositorio Spring Data para Habitaciones.
     */
    HabitacionDAOJPA(HabitacionRepository habitacionRepository) {
        this.habitacionRepository = habitacionRepository;
    }

    /**
     * Guarda una nueva habitación en la base de datos.
     * @param habitacion La entidad habitación a persistir. Si es null, no realiza acción.
     */
    @Override
    public void crearHabitacion(Habitacion habitacion) {
        if(habitacion == null) return;
        habitacionRepository.save(habitacion);
    }

    @Override
    public void actualizarHabitacion(Habitacion habitacion) {

    }

    @Override
    public void eliminarHabitacion(Habitacion habitacion) {

    }

    /**
     * Recupera todas las habitaciones existentes.
     * @return Lista de todas las habitaciones.
     */
    @Override
    public List<Habitacion> listar() {
        return habitacionRepository.findAll();
    }

    @Override
    public List<Habitacion> listarPorTipo(TipoHab tipoHabitacion) {
        return List.of();
    }

    /**
     * Busca una habitación por su número identificador.
     * @param numero Número de la habitación (ID).
     * @return La habitación encontrada o {@code null} si no existe.
     */
    @Override
    public Habitacion buscarPorNumero(Long numero) {
        Optional<Habitacion> hab = habitacionRepository.findById(numero);
        return hab.orElse(null);
    }
}
