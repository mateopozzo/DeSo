package ddb.deso.almacenamiento.JPA;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Repository;

import ddb.deso.almacenamiento.DAO.ReservaDAO;
import ddb.deso.negocio.habitaciones.Reserva;
import ddb.deso.repository.ReservaRepository;


/**
 * Implementación JPA de {@link ReservaDAO}.
 * Encargada de la persistencia de Reservas y filtrado por fechas.
 */
@Repository
public class ReservaDAOJPA implements ReservaDAO {

    private final ReservaRepository reservaRepository;

    public ReservaDAOJPA(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    /**
     * Guarda una nueva reserva.
     * @param reserva La reserva a persistir. Si es null, retorna sin acciones.
     */
    @Override
    public void crearReserva(Reserva reserva) {
        if(reserva == null) {return;}
        reservaRepository.save(reserva);
    }

    /**
     * Actualiza una reserva existente.
     *
     * <p>En JPA, {@code save} funciona tanto para crear como para actualizar
     * (si el entity tiene ID persistido).</p>
     *
     * @param reserva reserva a actualizar.
     */
   @Override
    public void actualizar(Reserva reserva) {
        if(reserva == null) { return; }
        reservaRepository.save(reserva);
    }

    /**
     * Busca reservas por apellido (obligatorio) y opcionalmente por nombre.
     *
     * @param apellido apellido (obligatorio).
     * @param nombre nombre (opcional).
     * @return lista de reservas encontradas.
     */
    @Override
    public List<Reserva> buscarPorApellidoNombre(String apellido, String nombre) {
        if (apellido == null) return List.of();

        if (nombre == null || nombre.isBlank()) {
            return reservaRepository.findByApellidoStartingWithIgnoreCase(apellido);
        }
        return reservaRepository.findByApellidoStartingWithIgnoreCaseAndNombreStartingWithIgnoreCase(apellido, nombre);
    }


    @Override
    public void eliminar(Reserva reserva) {

    }

    /**
     * Obtiene todas las reservas del sistema.
     * @return Lista completa de reservas.
     */
    @Override
    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    /**
     * Busca una reserva por su identificador único.
     * @param ID Identificador de la reserva.
     * @return La reserva encontrada o {@code null} si no existe o el ID es nulo.
     */
    @Override
    public Reserva buscarPorID(Long ID) {
        if(ID == null) {return null;}
        var reserva = reservaRepository.findById(ID);
        return reserva.orElse(null);

    }

    /**
     * Lista las reservas que se solapan con un rango de fechas dado.
     * Utiliza un filtrado en memoria (Stream) sobre todas las reservas existentes.
     *
     * @param fechaInicio Inicio del rango.
     * @param fechaFin Fin del rango.
     * @return Lista de reservas filtradas.
     */
    @Override
    public List<Reserva> listarPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        var listaReservas = listar();
        listaReservas = listaReservas.stream().
                filter(una_res -> enRango(una_res, fechaInicio, fechaFin)).
                toList();
        return listaReservas;
    }

    /**
     * Comprueba si las fechas de una reserva se superponen con un rango específico.
     *
     * @param reserva La reserva a verificar.
     * @param fechaInicio Inicio del rango de comparación.
     * @param fechaFin Fin del rango de comparación.
     * @return {@code true} si hay superposición, {@code false} si no.
     */
    boolean enRango(Reserva reserva, LocalDate fechaInicio, LocalDate fechaFin){
        LocalDate fechaResInicio = reserva.getFecha_inicio();
        LocalDate fechaResFin = reserva.getFecha_fin();
        if(fechaResInicio.isEqual(fechaInicio) || fechaResInicio.isEqual(fechaFin)){
            return true;
        }
        if(fechaResFin.isEqual(fechaInicio) || fechaResFin.isEqual(fechaFin)){
            return true;
        }
        if(fechaResInicio.isBefore(fechaFin) && fechaResInicio.isAfter(fechaInicio)){
            return true;
        }
        return fechaResFin.isBefore(fechaFin) && fechaResFin.isAfter(fechaInicio);
    }
}

