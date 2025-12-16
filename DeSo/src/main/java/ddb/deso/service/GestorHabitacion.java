package ddb.deso.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DAO.DatosCheckInDAO;
import ddb.deso.almacenamiento.DAO.EstadiaDAO;
import ddb.deso.almacenamiento.DAO.HabitacionDAO;
import ddb.deso.almacenamiento.DAO.ReservaDAO;
import ddb.deso.almacenamiento.DTO.ConsultarReservasDTO;
import ddb.deso.almacenamiento.DTO.CriteriosBusq;
import ddb.deso.almacenamiento.DTO.DisponibilidadDTO;
import ddb.deso.almacenamiento.DTO.HabitacionDTO;
import ddb.deso.almacenamiento.DTO.HabitacionReservaDTO;
import ddb.deso.almacenamiento.DTO.ReservaDTO;
import ddb.deso.almacenamiento.DTO.ReservaGrillaDTO;
import ddb.deso.negocio.EstadoHab;
import ddb.deso.negocio.alojamiento.Alojado;
import ddb.deso.negocio.alojamiento.DatosCheckIn;
import ddb.deso.negocio.alojamiento.Invitado;
import ddb.deso.negocio.habitaciones.Estadia;
import ddb.deso.negocio.habitaciones.Habitacion;
import ddb.deso.negocio.habitaciones.Reserva;
import ddb.deso.service.excepciones.AlojadoInvalidoException;
import ddb.deso.service.excepciones.ApellidoVacioException;
import ddb.deso.service.excepciones.HabitacionInexistenteException;
import ddb.deso.service.excepciones.ReservaInexistenteException;
import ddb.deso.service.excepciones.ReservaInvalidaException;

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
            if ("Cancelada".equalsIgnoreCase(reserva.getEstado())) {
                continue;
            }
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

        if(reservaDTO == null){
            throw new ReservaInvalidaException("DTO nulo");
        }

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

        // busqueda del encargado
        var listaAlojados = alojadoDAO.buscarAlojado(criteriosHuesped);

        if(listaAlojados == null || listaAlojados.isEmpty()){
            throw new AlojadoInvalidoException("El alojado no existe en la base");
        } else if(listaAlojados.size() > 1){
            // Deberia existir un solo huesped con el id de criteriosBusqueda
            throw new AlojadoInvalidoException("Existe mas de un alojado con " + criteriosHuesped.getTipoDoc() + " " + criteriosHuesped.getNroDoc());
        }

        Alojado huesped = listaAlojados.getFirst();

        if(huesped == null){
            throw new AlojadoInvalidoException("El alojado no existe en la base");
        }
        if(huesped instanceof Invitado){
            String nroDoc = huesped.getDatos().getNroDoc();
            String tipoDoc = huesped.getDatos().getTipoDoc().toString();
            // Actualiza el tipo de alojado en la base de datos
            alojadoDAO.promoverAHuesped(nroDoc, tipoDoc);
            // vuelvo a traer al alojado desde la base, ahora actualizado
            listaAlojados = alojadoDAO.buscarAlojado(criteriosHuesped);

            if(listaAlojados == null || listaAlojados.isEmpty()){
                throw new AlojadoInvalidoException("El alojado no existe en la base");
            } else if(listaAlojados.size() > 1){
                // Deberia existir un solo huesped con el id de criteriosBusqueda
                throw new AlojadoInvalidoException("Existe mas de un alojado con " + criteriosHuesped.getTipoDoc() + " " + criteriosHuesped.getNroDoc());
            }

            huesped = listaAlojados.getFirst();
        }

        // busqueda de los invitados
        List<Alojado> alojados = criteriosinvitados.stream()
                .map(criteriosBusq -> alojadoDAO.buscarAlojado(criteriosBusq).getFirst())
                .toList();

        // busqueda de las habitaciones
        Habitacion habitacion = habitacionDAO.buscarPorNumero(IDHabitacion);

        if(habitacion == null){
            throw new HabitacionInexistenteException("Habitacion " + IDHabitacion + " no encontrada");
        }

        // crear check in
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
    @Transactional(readOnly = true)
    public Collection<ReservaDTO> consultarReservas(ConsultarReservasDTO rango) {

        var fechaInicio = LocalDate.parse(rango.getFechaInicio());
        var fechaFin = LocalDate.parse(rango.getFechaFin());

        if(fechaFin.isBefore(fechaInicio)){
            return List.of();
        }

        var reservas = reservaDAO.listarPorFecha(fechaInicio, fechaFin);

        List<ReservaDTO> reservasDTOCoincidentes = new ArrayList<>();

        for(var r : reservas){

            boolean contieneHabitacion = r.getListaHabitaciones().stream()
                    .anyMatch(habitacion -> habitacion.getNroHab().equals(rango.getIdHabitacion()));

            if(contieneHabitacion){
                var rdto = new ReservaDTO();

                rdto.setFecha_inicio(r.getFecha_inicio());
                rdto.setFecha_fin(r.getFecha_fin());
                rdto.setNombre(r.getNombre());
                rdto.setApellido(r.getApellido());
                rdto.setTelefono(r.getTelefono());
                rdto.setEstado(r.getEstado());


                reservasDTOCoincidentes.add(rdto);
            }
        }

        return reservasDTOCoincidentes;
    }

    /**
     * Busca reservas por apellido (obligatorio) y opcionalmente por nombre,
     * para mostrar en la grilla del CU06 - Cancelar Reserva.
     *
     * @param apellido apellido del eventual huésped (obligatorio).
     * @param nombre nombre del eventual huésped (opcional).
     * @return lista de reservas encontradas para la grilla.
     * @throws ApellidoVacioException si el apellido es nulo o vacío (flujo alternativo 3.A del CU).
     */
    @Transactional(readOnly = true)
    public List<ReservaGrillaDTO> buscarReservasPorApellidoNombre(String apellido, String nombre)  {

        if (apellido == null || apellido.isBlank()) {
            throw new ApellidoVacioException("El campo apellido no puede estar vacío");
        }

        var reservas = reservaDAO.buscarPorApellidoNombre(apellido.trim(),
                (nombre == null ? null : nombre.trim()));

        if (reservas == null || reservas.isEmpty()) {
            return List.of();
        }

        List<ReservaGrillaDTO> resultado = new ArrayList<>();

        for (var r : reservas) {

            // Si la reserva está cancelada, la ignoramos y pasamos a la siguiente
            if ("Cancelada".equalsIgnoreCase(r.getEstado())) {
                continue;
            }
            var habitaciones = new ArrayList<HabitacionReservaDTO>();
            if (r.getListaHabitaciones() != null) {
                for (var h : r.getListaHabitaciones()) {
                    habitaciones.add(new HabitacionReservaDTO(h.getNroHab(),h.getTipo_hab()));
                }
            }

            resultado.add(new ReservaGrillaDTO(
                    r.getIdReserva(),
                    r.getApellido(),
                    r.getNombre(),
                    r.getFecha_inicio(),
                    r.getFecha_fin(),
                    habitaciones
            ));
        }

        return resultado;
    }

    /**
     * Cancela una reserva por ID marcándola con estado "Cancelada".
     *
     * @param idReserva id de la reserva.
     * @throws ReservaInexistenteException si la reserva no existe.
     */
    public void cancelarReserva(Long idReserva) {
        if (idReserva == null) {
            throw new ReservaInexistenteException("ID de reserva nulo");
        }

        Reserva reserva = reservaDAO.buscarPorID(idReserva);
        if (reserva == null) {
            throw new ReservaInexistenteException("No existe la reserva con id: " + idReserva);
        }

        reserva.setEstado("Cancelada");
        reservaDAO.actualizar(reserva);
    }

}
