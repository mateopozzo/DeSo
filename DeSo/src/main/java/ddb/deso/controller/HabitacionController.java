package ddb.deso.controller;

import ddb.deso.EstadoHab;
import ddb.deso.TipoHab;
import ddb.deso.almacenamiento.DAO.EstadiaDAO;
import ddb.deso.almacenamiento.DAO.ReservaDAO;
import ddb.deso.gestores.GestorHabitacion;
import ddb.deso.habitaciones.Estadia;
import ddb.deso.habitaciones.Reserva;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class HabitacionController {

    @Autowired
    private final GestorHabitacion gestorHabitacion;

    @Autowired
    public HabitacionController(GestorHabitacion gestorHabitacion) {this.gestorHabitacion = gestorHabitacion;}

    /**
     * DTO destinado a la comunicación de reservas y estadias en su rango de fecha
     */
    @AllArgsConstructor
    public class DisponibilidadDTO {

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

    private List<DisponibilidadDTO> listarPorReserva(Reserva reserva){
        var iteradorHabitaciones = reserva.getListaHabitaciones().iterator();
        List<DisponibilidadDTO> listaDisponibilidades = new ArrayList<>();
        while(iteradorHabitaciones.hasNext()){
            Long idHab = iteradorHabitaciones.next().getNroHab();
            var  tipoH = iteradorHabitaciones.next().getTipo_hab();
            var disponibilidad = new DisponibilidadDTO(
                    tipoH,
                    idHab,
                    reserva.getFecha_inicio(),
                    reserva.getFecha_fin(),
                    EstadoHab.OCUPADA
            );
            listaDisponibilidades.add(disponibilidad);
        }
        return listaDisponibilidades;
    }

    public class RangoFechas{
        LocalDate fecha_inicio;
        LocalDate fecha_fin;
    }

    @GetMapping("/api/habitaciones-disponibilidad")
    public ResponseEntity<List<DisponibilidadDTO>> disponibilidadHabitaciones(@RequestBody RangoFechas rango) {

        if(rango.fecha_inicio.isAfter(rango.fecha_fin)){
            return ResponseEntity.badRequest().build();
        }

        var listaReservas = gestorHabitacion.listarReservas(rango.fecha_inicio, rango.fecha_fin);
        var listaEstadias = gestorHabitacion.listarEstadias(rango.fecha_inicio, rango.fecha_fin);

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
