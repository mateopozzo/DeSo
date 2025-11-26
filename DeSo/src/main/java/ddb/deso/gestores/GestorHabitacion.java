package ddb.deso.gestores;

import java.time.LocalDate;
import java.util.List;

import ddb.deso.almacenamiento.DAO.*;
import ddb.deso.habitaciones.Estadia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ddb.deso.alojamiento.DatosCheckIn;
import ddb.deso.alojamiento.Huesped;
import ddb.deso.alojamiento.Invitado;
import ddb.deso.gestores.excepciones.HabitacionInexistenteException;
import ddb.deso.habitaciones.Habitacion;
import ddb.deso.habitaciones.Reserva;

@Service
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
    public void setReservaDAO(ReservaDAO reservaDAO) {this.reservaDAO = reservaDAO;}

    @Autowired
    public void setEstadiaDao(EstadiaDAO estadiaDAO) {this.estadiaDAO = estadiaDAO;}

    @Autowired
    public void setAlojadoDAO(AlojadoDAO alojadoDAO) {this.alojadoDAO = alojadoDAO;}

    @Autowired
    public void setCheckInDAO(DatosCheckInDAO checkInDAO) {this.checkInDAO = checkInDAO;}


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


    public void ocuparHabitacion(List<Habitacion> habitaciones, Huesped huesped, List<Invitado> invitados, LocalDate fechaInicio, LocalDate fechaFin ) {
        DatosCheckIn checkIn = new DatosCheckIn(fechaInicio);
        checkIn.setAlojado(huesped.getDatos());
        checkInDAO.crearDatosCheckIn(checkIn);

        huesped.getDatos().nuevoCheckIn(checkIn);
        alojadoDAO.crearAlojado(huesped);
        for(var id : invitados) {
            id.getDatos().nuevoCheckIn(checkIn);
            alojadoDAO.crearAlojado(id);
        }

        Estadia estadia = new Estadia();
        estadia.setFecha_inicio(fechaInicio);
        estadia.setFecha_fin(fechaFin);
        estadia.setDatosCheckIn(checkIn);
        estadia.setListaHabitaciones(habitaciones);
        estadiaDAO.crear(estadia);



    }
}
