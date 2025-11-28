package ddb.deso.controller;

import ddb.deso.EstadoHab;
import ddb.deso.almacenamiento.DAO.EstadiaDAO;
import ddb.deso.almacenamiento.DAO.ReservaDAO;
import ddb.deso.habitaciones.Estadia;
import ddb.deso.habitaciones.Reserva;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RestController
public class HabitacionController {

    @Autowired
    private EstadiaDAO estadiaDAO;
    @Autowired
    private ReservaDAO reservaDAO;

    @Autowired public HabitacionController(EstadiaDAO estadiaDAO, ReservaDAO reservaDAO) {
        this.estadiaDAO = estadiaDAO;
        this.reservaDAO = reservaDAO;
    }

    /**
     * DTO destinado a la comunacion de reservas y estadias en su rango de fecha
     */
    @AllArgsConstructor
    public class DisponibilidadDTO {

        public DisponibilidadDTO(Estadia estadia) {
            this.idHabitacion = estadia.getHabitacion().getNroHab();
            this.fecha_inicio = estadia.getFecha_inicio();
            this.fecha_fin = estadia.getFecha_fin();
            this.estado = EstadoHab.OCUPADA;
        }

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
            var disponibilidad = new DisponibilidadDTO(
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

        var listaReservas = reservaDAO.listarPorFecha(rango.fecha_inicio, rango.fecha_fin);
        var listaEstadias = estadiaDAO.listarPorFecha(rango.fecha_inicio, rango.fecha_fin);

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

    @GetMapping("/api/habitaciones-disponibilidad")
    public ResponseEntity<List<DisponibilidadDTO>> disponibilidadHabitaciones() {

        var listaReservas = reservaDAO.listar();
        var listaEstadias = estadiaDAO.listar();

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

}
