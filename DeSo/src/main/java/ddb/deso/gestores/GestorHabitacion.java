package ddb.deso.gestores;

import java.time.LocalDate;
import java.util.*;

import ddb.deso.EstadoHab;
import ddb.deso.almacenamiento.DAO.*;
import ddb.deso.almacenamiento.DTO.ConsultarReservasDTO;
import ddb.deso.almacenamiento.DTO.DisponibilidadDTO;
import ddb.deso.almacenamiento.DTO.HabitacionDTO;
import ddb.deso.almacenamiento.DTO.ReservaDTO;
import ddb.deso.gestores.excepciones.ReservaInvalidaException;
import ddb.deso.service.alojamiento.Alojado;
import ddb.deso.almacenamiento.DTO.CriteriosBusq;
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
    private GestorAlojamiento gestorAlojamiento;


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

    public List<HabitacionDTO> listarHabitaciones(){
        var listaHabitaciones = habitacionDAO.listar();

        if(listaHabitaciones == null){
            return null;
        }

        List<HabitacionDTO> habitacionesDTO = new ArrayList<>();

        for(var h : listaHabitaciones){
            HabitacionDTO hdto = new HabitacionDTO(h.getNroHab(), h.getTipo_hab(), h.getEstado_hab());
            habitacionesDTO.add(hdto);
        }

        return habitacionesDTO;

    }

    public List<DisponibilidadDTO> listarReservas() {

        var reservas = reservaDAO.listar();

        List<DisponibilidadDTO> disponibilidades = new ArrayList<>();

        if(reservas != null) for (Reserva reserva : reservas) {
            disponibilidades.addAll(listarDisponibilidadesPorReserva(reserva));
        }

        return disponibilidades;

    }

    public List<DisponibilidadDTO> listarReservas(LocalDate fechaInicio, LocalDate fechaFin) {

        var reservas = reservaDAO.listarPorFecha(fechaInicio, fechaFin);

        List<DisponibilidadDTO> disponibilidadesEnFecha = new ArrayList<>();

        if(reservas != null) for (Reserva reserva : reservas) {
            disponibilidadesEnFecha.addAll(listarDisponibilidadesPorReserva(reserva));
        }

        return disponibilidadesEnFecha;
    }

    public List<DisponibilidadDTO> listarEstadias() {

        var estadias = estadiaDAO.listar();

        List<DisponibilidadDTO> disponibilidades = new ArrayList<>();

        if(estadias!=null) estadias.
                forEach(estadia ->  disponibilidades.add(new DisponibilidadDTO(estadia)));

        return disponibilidades;

    }

    public List<DisponibilidadDTO> listarEstadias(LocalDate fechaInicio, LocalDate fechaFin){

        var estadias = estadiaDAO.listarPorFecha(fechaInicio, fechaFin);

        List<DisponibilidadDTO> disponibilidades = new ArrayList<>();

        if(estadias!=null) estadias.
                forEach(estadia ->  disponibilidades.add(new DisponibilidadDTO(estadia)));

        return disponibilidades;

    }


    public void crearReserva(ReservaDTO reservaDTO, List<Long> listaIDHabitaciones) {

        Reserva reserva = new Reserva(
                reservaDTO.getFecha_inicio(),
                reservaDTO.getFecha_fin(),
                "Reservado",
                reservaDTO.getNombre(),
                reservaDTO.getApellido(),
                reservaDTO.getTelefono()
        );

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
            var habitacion = habitacionDAO.buscarPorNumero(e.getIdHabitacion());
            if(habitacion == null) continue;
            habitacionesNoDisponibles.add(habitacion);
        }
        if(listaReservas != null) for(var r : listaReservas){
            var habitacion = habitacionDAO.buscarPorNumero(r.getIdHabitacion());
            if(habitacion == null) continue;
            habitacionesNoDisponibles.add(habitacion);
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


    public void ocuparHabitacion(Long IDHabitacion, Long idReserva, CriteriosBusq criteriosHuesped, List<CriteriosBusq> criteriosinvitados, LocalDate fechaInicio, LocalDate fechaFin ) {

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

        Reserva reserva = reservaDAO.buscarPorID(idReserva);

        Estadia estadia = new Estadia();
        estadia.setFecha_inicio(fechaInicio);
        estadia.setFecha_fin(fechaFin);
        estadia.setDatosCheckIn(checkIn);
        estadia.setHabitacion(habitacion);
        estadia.setReserva(reserva);
        estadiaDAO.crear(estadia);
    }

    private List<DisponibilidadDTO> listarDisponibilidadesPorReserva(Reserva reserva){
        var iteradorHabitaciones = reserva.getListaHabitaciones().iterator();
        List<DisponibilidadDTO> listaDisponibilidades = new ArrayList<>();
        while(iteradorHabitaciones.hasNext()){
            var habitacion = iteradorHabitaciones.next();
            Long idHab = habitacion.getNroHab();
            var  tipoH = habitacion.getTipo_hab();
            var disponibilidad = new DisponibilidadDTO(
                    tipoH,
                    idHab,
                    reserva.getFecha_inicio(),
                    reserva.getFecha_fin(),
                    EstadoHab.RESERVADA
            );
            listaDisponibilidades.add(disponibilidad);
        }
        return listaDisponibilidades;
    }

    public Collection<ReservaDTO> consultarReservas(ConsultarReservasDTO rango) {

        var fechaInicio = LocalDate.parse(rango.getFechaInicio());
        var fechaFin = LocalDate.parse(rango.getFechaFin());

        if(fechaFin.isBefore(fechaInicio)){
            return List.of();
        }

        var reservas = reservaDAO.listarPorFecha(fechaInicio, fechaFin);

        List<Reserva> reservasCoincidentes = new ArrayList<>();

        for(var r : reservas){
            if(rango.getIDHabitacion().equals(r.getIdReserva())){
                reservasCoincidentes.add(r);
            }
        }

        List<ReservaDTO> reservasDTOCoincidentes = new ArrayList<>();

        for(var r : reservasCoincidentes){
            var rdto = new ReservaDTO();
            rdto.setNombre(r.getNombre());
            rdto.setApellido(r.getApellido());
            rdto.setTelefono(r.getTelefono());
        }

        return reservasDTOCoincidentes;
    }
}
