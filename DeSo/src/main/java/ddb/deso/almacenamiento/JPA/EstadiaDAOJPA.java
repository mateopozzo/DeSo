package ddb.deso.almacenamiento.JPA;

import ddb.deso.almacenamiento.DAO.EstadiaDAO;
import ddb.deso.alojamiento.Huesped;
import ddb.deso.habitaciones.Estadia;
import ddb.deso.repository.EstadiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class EstadiaDAOJPA implements EstadiaDAO {
    private EstadiaRepository estadiaRepository;

    @Autowired
    public EstadiaDAOJPA(EstadiaRepository estadiaRepository) {this.estadiaRepository = estadiaRepository;}

    @Override
    public void crear(Estadia estadia) {
        estadiaRepository.save(estadia);
    }

    @Override
    public void actualizar(Estadia estadia) {

    }

    @Override
    public void eliminar(Estadia estadia) {

    }

    @Override
    public List<Estadia> listar() {
        return List.of();
    }

    @Override
    public List<Estadia> listarPorFecha(LocalDate fecha) {
        return List.of();
    }

    @Override
    public List<Estadia> listarPorHuesped(Huesped huesped) {
        return List.of();
    }

    @Override
    public List<Estadia> listarPorFechayHuesped(LocalDate fecha, Huesped huesped) {
        return List.of();
    }
}
