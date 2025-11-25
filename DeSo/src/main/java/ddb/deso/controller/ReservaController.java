package ddb.deso.controller;

import ddb.deso.almacenamiento.DTO.ReservaDTO;
import ddb.deso.gestores.GestorHabitacion;
import ddb.deso.habitaciones.Reserva;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reserva")
@CrossOrigin("localhost:63342")
public class ReservaController {
    private final GestorHabitacion gestorHabitacion;

    public ReservaController(GestorHabitacion gestorHabitacion) {
        this.gestorHabitacion = gestorHabitacion;
    }

    public class EstructuraPost{
        ReservaDTO reservaDTO;
        List<Long> listaIDHabitaciones;
    }

    @PostMapping
    public ResponseEntity<ReservaDTO> crearReserva (@RequestBody EstructuraPost estructura) {

        /*  La idea es que despues el gestor busque en la base segun los ids de las habitaciones
            que el conserje elige   */

        ReservaDTO reservaDTO = estructura.reservaDTO;
        List<Long> listaIDHabitaciones  = estructura.listaIDHabitaciones;

        if(reservaDTO == null || listaIDHabitaciones == null || listaIDHabitaciones.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Reserva reserva = new Reserva(reservaDTO.getFecha_inicio(), reservaDTO.getFecha_fin(), "Reservado", reservaDTO.getNombre(), reservaDTO.getApellido(), reservaDTO.getTelefono());

        gestorHabitacion.crearReserva(reserva, listaIDHabitaciones);

        return ResponseEntity.status(HttpStatus.CREATED).body(reservaDTO);

    }
}
