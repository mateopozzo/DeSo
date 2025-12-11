package ddb.deso.controller;

import ddb.deso.almacenamiento.DTO.*;
import ddb.deso.gestores.GestorHabitacion;
import ddb.deso.gestores.excepciones.AlojadoInvalidoException;
import ddb.deso.gestores.excepciones.HabitacionInexistenteException;
import ddb.deso.gestores.excepciones.ReservaInvalidaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controlador REST encargado de gestionar las operaciones relacionadas con las Habitaciones,
 * Reservas y Estadías.
 * <p>Expone endpoints para consultar disponibilidad, crear reservas y registrar ocupaciones (check-in).</p>
 */
@RestController
public class HabitacionController {

    private final GestorHabitacion gestorHabitacion;
    /**
     * Inyección de dependencia del Gestor de Habitaciones.
     * @param gestorHabitacion Lógica de negocio para habitaciones.
     */
    public HabitacionController(GestorHabitacion gestorHabitacion) {this.gestorHabitacion = gestorHabitacion;}

    /**
     * Obtiene el listado de todas las habitaciones registradas en el sistema.
     *
     * @return {@link ResponseEntity} con la lista de {@link HabitacionDTO} y estado HTTP 200 (OK).
     * Devuelve una lista vacía si no hay habitaciones.
     */
    @GetMapping("api/habitacion")
    public ResponseEntity<List<HabitacionDTO>> listarTodaHabitacion(){

        var habitacionesDTO = gestorHabitacion.listarHabitaciones();
        if(habitacionesDTO==null || habitacionesDTO.isEmpty()){
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(habitacionesDTO);
    }

    /**
     * Consulta la disponibilidad de habitaciones en un rango de fechas.
     * Retorna una lista de ocupaciones (Reservas y Estadías existentes) que impiden la disponibilidad.
     *
     * @param fecha_inicio Fecha de inicio del rango a consultar.
     * @param fecha_fin Fecha de fin del rango a consultar.
     * @return Lista de {@link DisponibilidadDTO} que representan los intervalos ocupados.
     */
    @GetMapping("/api/habitaciones-disponibilidad")
    public ResponseEntity<List<DisponibilidadDTO>>
    disponibilidadHabitaciones(@RequestParam LocalDate fecha_inicio, @RequestParam LocalDate fecha_fin) {

        if(fecha_inicio.isAfter(fecha_fin)){
            System.out.println("fecha_inicio.isAfter(fecha_fin)");
            return ResponseEntity.ok(List.of());
        }

        var listaReservas = gestorHabitacion.listarReservas(fecha_inicio, fecha_fin);
        var listaEstadias = gestorHabitacion.listarEstadias(fecha_inicio, fecha_fin);

        listaReservas.addAll(listaEstadias);

        return ResponseEntity.ok(listaReservas);

    }


    /**
     * Registra el ingreso de huéspedes a una habitación (Check-In / Estadía).
     *
     * @param estadiaDTO Objeto con los datos de la estadía, incluyendo encargado, invitados y fechas.
     * @return {@link ResponseEntity} con:
     * <ul>
     * <li>201 CREATED y el DTO si se creó con éxito.</li>
     * <li>400 BAD REQUEST si faltan datos obligatorios o hay conflicto de lógica (Alojado inválido, Habitación inexistente).</li>
     * </ul>
     */
    @PostMapping("/api/ocupar-habitacion")
    public ResponseEntity<EstadiaDTO> crearEstadia(
            @RequestBody EstadiaDTO estadiaDTO
    ) {

        if(     estadiaDTO == null ||
                estadiaDTO.getEncargado() == null ||
                estadiaDTO.getFechaFin() == null ||
                estadiaDTO.getFechaInicio() == null ||
                estadiaDTO.getListaInvitados() == null ||
                estadiaDTO.getIdHabitacion() == null
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estadiaDTO);
        }

        try {
            gestorHabitacion.ocuparHabitacion(
                    estadiaDTO.getIdHabitacion(),
                    estadiaDTO.getIdReserva(),
                    estadiaDTO.getEncargado(),
                    estadiaDTO.getListaInvitados(),
                    estadiaDTO.getFechaInicio(),
                    estadiaDTO.getFechaFin()
            );
        } catch (AlojadoInvalidoException | HabitacionInexistenteException e){
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(estadiaDTO);

    }

    /**
     * Crea una nueva reserva de habitaciones.
     *
     * @param estructura Objeto contenedor que incluye los datos de la reserva y la lista de IDs de habitaciones solicitadas.
     * @return {@link ResponseEntity} con:
     * <ul>
     * <li>201 CREATED si la reserva fue exitosa.</li>
     * <li>422 UNPROCESSABLE ENTITY si la reserva es inválida o la habitación no existe.</li>
     * </ul>
     */
    @PostMapping("/api/reserva")
    public ResponseEntity<ReservaDTO> crearReserva (@RequestBody ReservaHabitacionesDTO estructura) {

        ReservaDTO reservaDTO = estructura.getReservaDTO();
        List<Long> listaIDHabitaciones  = estructura.getListaIDHabitaciones();

        try{
            gestorHabitacion.crearReserva(reservaDTO, listaIDHabitaciones);
        } catch (ReservaInvalidaException excepcion){
            System.out.println(excepcion.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        } catch (HabitacionInexistenteException excepcion){
            System.out.println(excepcion.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(reservaDTO);
    }

    /**
     * Consulta reservas existentes basándose en una lista de criterios.
     *
     * @param listaConsultas Lista de objetos {@link ConsultarReservasDTO} con los criterios de filtrado.
     * @return Un conjunto (Set) de {@link ReservaDTO} que coinciden con los criterios, evitando duplicados.
     */
    @PostMapping("/api/obtener-reservas")
    public ResponseEntity<Set<ReservaDTO>> consultaReserva(@RequestBody List<ConsultarReservasDTO> listaConsultas) {
        if(listaConsultas == null || listaConsultas.isEmpty()){
            return ResponseEntity.ok(new HashSet<>());
        }

        Set<ReservaDTO> reservasCoincidentes = new HashSet<>();

        listaConsultas
                .forEach(rango -> {
                    reservasCoincidentes.addAll(gestorHabitacion.consultarReservas(rango));
                });
        return ResponseEntity.ok(reservasCoincidentes);
    }

    /**
     * Valida la integridad de los datos de una reserva antes de su creación.
     * Verifica fechas nulas, fechas pasadas y datos del cliente.
     *
     * @param reservaDTO Datos de la reserva.
     * @param listaIDHabitaciones Lista de habitaciones.
     * @return true si los datos son válidos, false en caso contrario.
     */
    private boolean creacionReservaValida(ReservaDTO reservaDTO, List<Long> listaIDHabitaciones) {
        if(reservaDTO == null || listaIDHabitaciones == null || listaIDHabitaciones.isEmpty()) {
            return false;
        }
        if(reservaDTO.getFecha_inicio()==null || reservaDTO.getFecha_fin()==null)
            return false;
        if(reservaDTO.getFecha_inicio().isAfter(LocalDate.now())) {
            return false;
        }
        if(reservaDTO.getFecha_fin().isBefore(LocalDate.now())) {
            return false;
        }

        if(reservaDTO.getNombre()==null || reservaDTO.getNombre().isEmpty()) {
            return false;
        }
        if(reservaDTO.getApellido()==null || reservaDTO.getApellido().isEmpty()) {
            return false;
        }
        return reservaDTO.getTelefono() != null && !reservaDTO.getTelefono().isEmpty();
    }

    /**
     * Verifica si las habitaciones solicitadas están disponibles en las fechas indicadas.
     * Compara contra las reservas y estadías existentes.
     *
     * @param reservaDTO Datos de la reserva (incluye fechas).
     * @param listaIDHabitaciones IDs de las habitaciones a verificar.
     * @return true si todas las habitaciones están libres, false si hay conflicto.
     */
    private boolean verificarDisponibilidadDeHabitaciones(ReservaDTO reservaDTO, List<Long> listaIDHabitaciones) {
        // Verificacion de disponibilidad de reserva
        var listaDisponibilidad = disponibilidadHabitaciones(reservaDTO.getFecha_inicio(), reservaDTO.getFecha_fin()).getBody();
        var habitacionesDeReserva = new HashSet<Long>(listaIDHabitaciones);

        if(listaDisponibilidad!=null) for(var disponibilidad : listaDisponibilidad){
            if(habitacionesDeReserva.contains(disponibilidad.getIdHabitacion())){

                //CONFLICTO -> Una estadia o reserva ya ocupa la habitacion en la fecha seleccionada

                return false;
            }
        }
        return true;
    }

}
