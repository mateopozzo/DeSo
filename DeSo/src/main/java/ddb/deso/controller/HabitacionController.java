package ddb.deso.controller;

import ddb.deso.EstadoHab;
import ddb.deso.TipoHab;
import ddb.deso.almacenamiento.DTO.HabitacionDTO;
import ddb.deso.gestores.GestorHabitacion;
import ddb.deso.habitaciones.Estadia;
import ddb.deso.habitaciones.Reserva;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
