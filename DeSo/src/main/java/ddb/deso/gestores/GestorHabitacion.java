package ddb.deso.gestores;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ddb.deso.almacenamiento.DAO.*;
import ddb.deso.gestores.excepciones.ReservaInvalidaException;
import ddb.deso.service.alojamiento.Alojado;
import ddb.deso.service.alojamiento.CriteriosBusq;
import ddb.deso.service.alojamiento.DatosCheckIn;
import ddb.deso.service.alojamiento.Huesped;
import ddb.deso.service.habitaciones.Estadia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ddb.deso.gestores.excepciones.HabitacionInexistenteException;
import ddb.deso.service.habitaciones.Habitacion;
import ddb.deso.service.habitaciones.Reserva;
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

    public List<Habitacion> listarHabitaciones(){
        return habitacionDAO.listar();
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
        {   //  validaciones
            if (reserva == null)
                throw new ReservaInvalidaException("Reserva nula");

            if (reserva.getFecha_fin() == null || reserva.getFecha_inicio() == null)
                throw new ReservaInvalidaException("Fechas nulas");

            if (reserva.getFecha_inicio().isAfter(reserva.getFecha_fin()))
                throw new ReservaInvalidaException("Fechas invertidas");

            if (reserva.getNombre() == null || reserva.getNombre().isEmpty())
                throw new ReservaInvalidaException("No se asigna nombre");

            if (reserva.getTelefono() == null || reserva.getTelefono().isEmpty())
                throw new ReservaInvalidaException("No se asigna telefono");

            if (reserva.getApellido() == null || reserva.getApellido().isEmpty())
                throw new ReservaInvalidaException("No se asigna apellido");

            if (listaIDHabitaciones == null)
                throw new HabitacionInexistenteException("No se asignan habitaciones a la Reserva");
        }


        var listaEstadias = listarEstadias(reserva.getFecha_inicio(), reserva.getFecha_fin());
        var listaReservas = listarReservas(reserva.getFecha_inicio(), reserva.getFecha_fin());
        Set<Habitacion> habitacionesNoDisponibles = new HashSet<>();

        if(listaEstadias != null) for(var e : listaEstadias){
            habitacionesNoDisponibles.add(e.getHabitacion());
        }
        if(listaReservas != null) for(var r : listaReservas){
            habitacionesNoDisponibles.addAll(r.getListaHabitaciones());
        }

        for(var id : listaIDHabitaciones) {
            Habitacion habitacion = habitacionDAO.buscarPorNumero(id);
            if(habitacion != null) {
                if(habitacionesNoDisponibles.contains(habitacion)) {
                    throw new ReservaInvalidaException("La habitacion " + habitacion.getNroHab() + " no está disponible" );
                }
                reserva.agregarHabitacion(habitacion);
            } else {
                throw new HabitacionInexistenteException("Habitacion " + id + " no encontrada");
            }
        }

        reservaDAO.crearReserva(reserva);

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

        for(var id : alojados) {
            id.getDatos().nuevoCheckIn(checkIn);
        }

        Estadia estadia = new Estadia();
        estadia.setFecha_inicio(fechaInicio);
        estadia.setFecha_fin(fechaFin);
        estadia.setDatosCheckIn(checkIn);
        estadia.setHabitacion(habitacion);
        estadiaDAO.crear(estadia);
    }
}
