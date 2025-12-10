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

/**
 * Implementación JPA de {@link EstadiaDAO}.
 * Gestiona la persistencia de las estadías y ofrece filtrado por fechas.
 */
@Repository
public class EstadiaDAOJPA implements EstadiaDAO {
    private EstadiaRepository estadiaRepository;


    @Autowired
    public EstadiaDAOJPA(EstadiaRepository estadiaRepository) {this.estadiaRepository = estadiaRepository;}
    /**
     * Persiste una nueva estadía.
     * @param estadia La entidad estadía a guardar.
     */
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

    /**
     * Lista todas las estadías registradas en la base de datos.
     * @return Lista completa de estadías.
     */
    @Override
    public List<Estadia> listar() {
        return estadiaRepository.findAll();
    }

    /**
     * Lista las estadías que se solapan con un rango de fechas específico.
     * El filtrado se realiza en memoria (Java Stream) tras obtener todos los registros.
     *
     * @param fechaInicio Fecha de inicio del rango a consultar.
     * @param fechaFin Fecha de fin del rango a consultar.
     * @return Lista de estadías que coinciden o se solapan con el rango.
     */
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

    /**
     * Método auxiliar para determinar si una estadía cae dentro o se solapa con un rango de fechas.
     * Verifica si la fecha de inicio o fin de la estadía está dentro del rango solicitado,
     * o si la estadía engloba completamente al rango solicitado.
     *
     * @param estadia La estadía a evaluar.
     * @param fechaInicio Inicio del rango buscado.
     * @param fechaFin Fin del rango buscado.
     * @return {@code true} si hay solapamiento, {@code false} en caso contrario.
     */
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
