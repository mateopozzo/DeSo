package ddb.deso.almacenamiento.JPA;

import ddb.deso.TipoHab;
import ddb.deso.almacenamiento.DAO.HabitacionDAO;
import ddb.deso.habitaciones.Habitacion;
import ddb.deso.repository.HabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class HabitacionDAOJPA implements HabitacionDAO {

    private final HabitacionRepository habitacionRepository;

    HabitacionDAOJPA(HabitacionRepository habitacionRepository) {
        this.habitacionRepository = habitacionRepository;
    }

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

    @Override
    public List<Habitacion> listar() {
        return habitacionRepository.findAll();
    }

    @Override
    public List<Habitacion> listarPorTipo(TipoHab tipoHabitacion) {
        return List.of();
    }

    @Override
    public Habitacion buscarPorNumero(Long numero) {
        Optional<Habitacion> hab = habitacionRepository.findById(numero);
        return hab.orElse(null);
    }
}
