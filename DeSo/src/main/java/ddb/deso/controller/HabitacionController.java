package ddb.deso.controller;

import ddb.deso.EstadoHab;
import ddb.deso.TipoHab;
import ddb.deso.almacenamiento.DTO.EstadiaDTO;
import ddb.deso.almacenamiento.DTO.HabitacionDTO;
import ddb.deso.almacenamiento.DTO.ReservaDTO;
import ddb.deso.gestores.GestorHabitacion;
import ddb.deso.habitaciones.Estadia;
import ddb.deso.habitaciones.Reserva;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class HabitacionController {

    private final GestorHabitacion gestorHabitacion;

    public HabitacionController(GestorHabitacion gestorHabitacion) {this.gestorHabitacion = gestorHabitacion;}

    /**
     * DTO destinado a la comunicación de reservas y estadias en su rango de fecha
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DisponibilidadDTO {

        public DisponibilidadDTO(Estadia estadia) {
            this.tipo = estadia.getHabitacion().getTipo_hab();
            this.idHabitacion = estadia.getHabitacion().getNroHab();
            this.fecha_inicio = estadia.getFecha_inicio();
            this.fecha_fin = estadia.getFecha_fin();
            this.estado = EstadoHab.OCUPADA;
        }

        TipoHab tipo;
        Long idHabitacion;
        LocalDate fecha_inicio;
        LocalDate fecha_fin;
        EstadoHab estado;

    }

    @GetMapping("api/habitacion")
    public ResponseEntity<List<HabitacionDTO>> listarTodaHabitacion(){

        var listaHabitaciones = gestorHabitacion.listarHabitaciones();

        if(listaHabitaciones == null){
            return ResponseEntity.noContent().build();
        }

        List<HabitacionDTO> habitacionesDTO = new ArrayList<>();

        for(var h : listaHabitaciones){
            HabitacionDTO hdto = new HabitacionDTO(h.getNroHab(), h.getTipo_hab(), h.getEstado_hab());
            habitacionesDTO.add(hdto);
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

        listaReservas.forEach(
                reserva -> listaDisponibilidades.addAll(listarPorReserva(reserva)
                )
        );

        listaEstadias.forEach(
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

    @Getter
    @Setter
    @NoArgsConstructor
    public static class PostDTO {
        private ReservaDTO reservaDTO;
        private List<Long> listaIDHabitaciones;
    }

    @PostMapping("/api/reserva")
    public ResponseEntity<ReservaDTO> crearReserva (@RequestBody PostDTO estructura) {

        ReservaDTO reservaDTO = estructura.reservaDTO;
        List<Long> listaIDHabitaciones  = estructura.listaIDHabitaciones;

        if(reservaDTO == null || listaIDHabitaciones == null || listaIDHabitaciones.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Reserva reserva = new Reserva(
                reservaDTO.getFecha_inicio(),
                reservaDTO.getFecha_fin(),
                "Reservado",
                reservaDTO.getNombre(),
                reservaDTO.getApellido(),
                reservaDTO.getTelefono()
        );

        gestorHabitacion.crearReserva(reserva, listaIDHabitaciones);

        return ResponseEntity.status(HttpStatus.CREATED).body(reservaDTO);
    }


/*
    @GetMapping("/api/habitaciones-disponibilidad")
    public ResponseEntity<List<DisponibilidadDTO>> disponibilidadHabitaciones() {

        var listaReservas = gestorHabitacion.listarReservas();
        var listaEstadias = gestorHabitacion.listarEstadias();

        List<DisponibilidadDTO> listaDisponibilidades = new ArrayList<>();

        listaReservas.forEach(
                reserva -> listaDisponibilidades.addAll(listarPorReserva(reserva)
                )
        );

        listaEstadias.forEach(
                estadia -> listaDisponibilidades.add(new DisponibilidadDTO(estadia)
                )
        );

        return ResponseEntity.ok(listaDisponibilidades);

    }
    */

}
