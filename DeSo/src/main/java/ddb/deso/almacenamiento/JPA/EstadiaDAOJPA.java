package ddb.deso.almacenamiento.JPA;

import ddb.deso.almacenamiento.DAO.EstadiaDAO;
import ddb.deso.service.alojamiento.Huesped;
import ddb.deso.service.habitaciones.Estadia;
import ddb.deso.repository.EstadiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
        return estadiaRepository.findAll();
    }

    @Override
    public List<Estadia> listarPorFecha(LocalDate fechaInicio, LocalDate fechaFin){
        return listar().stream()
                .filter(estadia -> enRango(estadia, fechaInicio, fechaFin))
                .collect(Collectors.toList());
    }

    @Override
    public List<Estadia> listarPorHuesped(Huesped huesped) {
        return List.of();
    }

    @Override
    public List<Estadia> listarPorFechayHuesped(LocalDate fecha, Huesped huesped) {
        return List.of();
    }

    boolean enRango(Estadia estadia, LocalDate fechaInicio, LocalDate fechaFin){
        LocalDate fechaEstadiaInicio = estadia.getFecha_inicio();
        LocalDate fechaEstadiaFin = estadia.getFecha_fin();
        if(fechaEstadiaInicio.isEqual(fechaInicio) || fechaEstadiaInicio.isEqual(fechaFin)){
            return true;
        }
        if(fechaEstadiaFin.isEqual(fechaInicio) || fechaEstadiaFin.isEqual(fechaFin)){
            return true;
        }
        if(fechaEstadiaInicio.isBefore(fechaFin) && fechaEstadiaInicio.isAfter(fechaInicio)){
            return true;
        }
        return fechaEstadiaFin.isBefore(fechaFin) && fechaEstadiaFin.isAfter(fechaInicio);
    }
}
