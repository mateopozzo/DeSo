package ddb.deso.gestores;

import java.time.LocalDate;
import java.util.List;

import ddb.deso.almacenamiento.DAO.*;
import ddb.deso.alojamiento.*;
import ddb.deso.habitaciones.Estadia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ddb.deso.gestores.excepciones.HabitacionInexistenteException;
import ddb.deso.habitaciones.Habitacion;
import ddb.deso.habitaciones.Reserva;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GestorHabitacion {

    private ReservaDAO reservaDAO;
    private HabitacionDAO habitacionDAO;
    private EstadiaDAO estadiaDAO;
    private AlojadoDAO alojadoDAO;
    private DatosCheckInDAO checkInDAO;


    @Autowired
    public GestorHabitacion(ReservaDAO reservaDAO, HabitacionDAO habitacionDAO, EstadiaDAO estadiaDAO, AlojadoDAO alojadoDAO, DatosCheckInDAO checkInDAO) {
        this.reservaDAO = reservaDAO;
        this.habitacionDAO = habitacionDAO;
        this.estadiaDAO = estadiaDAO;
        this.alojadoDAO = alojadoDAO;
        this.checkInDAO = checkInDAO;
    }

    @Autowired
    public void setHabitacionDAO(HabitacionDAO habitacionDAO) {
        this.habitacionDAO = habitacionDAO;
    }

    @Autowired
    public void setReservaDAO(ReservaDAO reservaDAO) {
        this.reservaDAO = reservaDAO;
    }

    @Autowired
    public void setEstadiaDao(EstadiaDAO estadiaDAO) {
        this.estadiaDAO = estadiaDAO;
    }

    @Autowired
    public void setAlojadoDAO(AlojadoDAO alojadoDAO) {
        this.alojadoDAO = alojadoDAO;
    }

    @Autowired
    public void setCheckInDAO(DatosCheckInDAO checkInDAO) {
        this.checkInDAO = checkInDAO;
    }


    public List<Reserva> listarReservas() {

        return reservaDAO.listar();

    }

    public List<Reserva> listarReservas(LocalDate fechaInicio, LocalDate fechaFin) {

        return reservaDAO.listarPorFecha(fechaInicio, fechaFin);

    }

    public List<Estadia> listarEstadias() {

        return estadiaDAO.listar();

    }

    public List<Estadia> listarEstadias(LocalDate fechaInicio, LocalDate fechaFin){

        return estadiaDAO.listarPorFecha(fechaInicio, fechaFin);

    }


    public void crearReserva(Reserva reserva, List<Long> listaIDHabitaciones) {

        for(var id : listaIDHabitaciones) {
            Habitacion habitacion = habitacionDAO.buscarPorNumero(id);
            if(habitacion != null) {
                reserva.agregarHabitacion(habitacion);
            } else {
                throw new HabitacionInexistenteException("Habitacion " + id + " no encontrada");
            }
        }

        reservaDAO.crearReserva(reserva);

        return;

    }


    public void ocuparHabitacion(Long IDHabitacion, CriteriosBusq criteriosHuesped, List<CriteriosBusq> criteriosinvitados, LocalDate fechaInicio, LocalDate fechaFin ) {

        Huesped huesped = (Huesped) alojadoDAO.buscarAlojado(criteriosHuesped).getFirst();
        List<Alojado> alojados = criteriosinvitados.stream()
                .map(criteriosBusq -> alojadoDAO.buscarAlojado(criteriosBusq).getFirst())
                .toList();
        Habitacion habitacion = habitacionDAO.buscarPorNumero(IDHabitacion);

        DatosCheckIn checkIn = new DatosCheckIn(fechaInicio);
        checkIn.setAlojado(huesped.getDatos());
        huesped.getDatos().nuevoCheckIn(checkIn);
        checkInDAO.crearDatosCheckIn(checkIn);
//        alojadoDAO.crearAlojado(huesped);     spring actualiza solo
        for(var id : alojados) {
            id.getDatos().nuevoCheckIn(checkIn);
//            alojadoDAO.crearAlojado(id);
        }

        Estadia estadia = new Estadia();
        estadia.setFecha_inicio(fechaInicio);
        estadia.setFecha_fin(fechaFin);
        estadia.setDatosCheckIn(checkIn);
        estadia.setHabitacion(habitacion);
        estadiaDAO.crear(estadia);
    }
}
