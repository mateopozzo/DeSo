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


@RestController
@CrossOrigin(origins ={"http://localhost:3000/", "http://localhost:8080"})
public class HabitacionController {

    private final GestorHabitacion gestorHabitacion;

    public HabitacionController(GestorHabitacion gestorHabitacion) {this.gestorHabitacion = gestorHabitacion;}

    @GetMapping("api/habitacion")
    public ResponseEntity<List<HabitacionDTO>> listarTodaHabitacion(){

        var habitacionesDTO = gestorHabitacion.listarHabitaciones();

        if(habitacionesDTO==null || habitacionesDTO.isEmpty()){
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(habitacionesDTO);
    }


    @GetMapping("/api/habitaciones-disponibilidad")
    public ResponseEntity<List<DisponibilidadDTO>>
    disponibilidadHabitaciones(@RequestParam LocalDate fecha_inicio, @RequestParam LocalDate fecha_fin) {

        if(fecha_inicio.isAfter(fecha_fin)){
            return ResponseEntity.ok(List.of());
        }
        if(fecha_inicio.isEqual(LocalDate.now())){
            return ResponseEntity.ok(List.of());
        }

        var listaReservas = gestorHabitacion.listarReservas(fecha_inicio, fecha_fin);
        var listaEstadias = gestorHabitacion.listarEstadias(fecha_inicio, fecha_fin);

        listaReservas.addAll(listaEstadias);

        return ResponseEntity.ok(listaReservas);

    }

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

    @PostMapping("/api/reserva")
    public ResponseEntity<ReservaDTO> crearReserva (@RequestBody ReservaHabitacionesDTO estructura) {

        ReservaDTO reservaDTO = estructura.getReservaDTO();
        List<Long> listaIDHabitaciones  = estructura.getListaIDHabitaciones();

        try{
            gestorHabitacion.crearReserva(reservaDTO, listaIDHabitaciones);
        } catch (ReservaInvalidaException | HabitacionInexistenteException excepcion){
            System.out.println(excepcion.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(reservaDTO);
    }

    @GetMapping("/api/obtener-reservas")
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
