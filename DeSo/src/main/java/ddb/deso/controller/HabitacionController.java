package ddb.deso.controller;

import ddb.deso.EstadoHab;
import ddb.deso.almacenamiento.DTO.*;
import ddb.deso.gestores.GestorHabitacion;
import ddb.deso.gestores.excepciones.HabitacionInexistenteException;
import ddb.deso.gestores.excepciones.ReservaInvalidaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class HabitacionController {

    private final GestorHabitacion gestorHabitacion;

    public HabitacionController(GestorHabitacion gestorHabitacion) {this.gestorHabitacion = gestorHabitacion;}

    @GetMapping("api/habitacion")
    public ResponseEntity<List<HabitacionDTO>> listarTodaHabitacion(){

        var listaHabitaciones = gestorHabitacion.listarHabitaciones();

        if(listaHabitaciones == null){
            return ResponseEntity.badRequest().body(List.of());
        }

        List<HabitacionDTO> habitacionesDTO = new ArrayList<>();

        for(var h : listaHabitaciones){
            HabitacionDTO hdto = new HabitacionDTO(h.getNroHab(), h.getTipo_hab(), h.getEstado_hab());
            habitacionesDTO.add(hdto);
        }

        if(listaHabitaciones==null || listaHabitaciones.isEmpty()){
            return ResponseEntity.badRequest().body(List.of());
        }

        return ResponseEntity.ok(habitacionesDTO);
    }

    private List<DisponibilidadDTO> listarPorReserva(Reserva reserva){
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


    @GetMapping("/api/habitaciones-disponibilidad")
    public ResponseEntity<List<DisponibilidadDTO>>
    disponibilidadHabitaciones(@RequestParam LocalDate fecha_inicio, @RequestParam LocalDate fecha_fin) {

        if(fecha_inicio.isAfter(fecha_fin)){
            return ResponseEntity.badRequest().build();
        }

        var listaReservas = gestorHabitacion.listarReservas(fecha_inicio, fecha_fin);
        var listaEstadias = gestorHabitacion.listarEstadias(fecha_inicio, fecha_fin);

        List<DisponibilidadDTO> listaDisponibilidades = new ArrayList<>();

        if(listaReservas!=null) listaReservas.forEach(
                reserva -> listaDisponibilidades.addAll(listarPorReserva(reserva)
                )
        );

        if(listaEstadias!=null) listaEstadias.forEach(
                estadia -> listaDisponibilidades.add(new DisponibilidadDTO(estadia)
                )
        );

        return ResponseEntity.ok(listaDisponibilidades);

    }

    @PostMapping("/api/ocupar-habitacion")
    public ResponseEntity<EstadiaDTO> crearEstadia(@RequestBody EstadiaDTO estadiaDTO) {

        if(     estadiaDTO == null ||
                estadiaDTO.getEncargado() == null ||
                estadiaDTO.getFechaFin() == null ||
                estadiaDTO.getFechaInicio() == null ||
                estadiaDTO.getListaInvitados() == null ||
                estadiaDTO.getIdHabitacion() == null
        ) {
            return ResponseEntity.badRequest().body(null);
        }


        gestorHabitacion.ocuparHabitacion(
                estadiaDTO.getIdHabitacion(),
                estadiaDTO.getEncargado(), estadiaDTO.getListaInvitados(),
                estadiaDTO.getFechaInicio(),
                estadiaDTO.getFechaFin()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(estadiaDTO);

    }

    @PostMapping("/api/reserva")
    public ResponseEntity<ReservaDTO> crearReserva (@RequestBody ReservaHabitacionesDTO estructura) {

        ReservaDTO reservaDTO = estructura.getReservaDTO();
        List<Long> listaIDHabitaciones  = estructura.getListaIDHabitaciones();

        if(!creacionReservaValida(reservaDTO, listaIDHabitaciones))
            return ResponseEntity.badRequest().build();

        if(!verificarDisponibilidadDeHabitaciones(reservaDTO, listaIDHabitaciones))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(reservaDTO);

        Reserva reserva = new Reserva(
                reservaDTO.getFecha_inicio(),
                reservaDTO.getFecha_fin(),
                "Reservado",
                reservaDTO.getNombre(),
                reservaDTO.getApellido(),
                reservaDTO.getTelefono()
        );

        try{
            gestorHabitacion.crearReserva(reserva, listaIDHabitaciones);
        } catch (ReservaInvalidaException | HabitacionInexistenteException excepcion){
            System.out.println(excepcion.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(reservaDTO);
    }

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
        if(reservaDTO.getTelefono() == null || reservaDTO.getTelefono().isEmpty()){
            return false;
        }
        return true;
    }

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
