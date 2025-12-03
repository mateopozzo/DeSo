package ddb.deso.controller;

import ddb.deso.almacenamiento.DTO.ReservaDTO;
import ddb.deso.gestores.GestorHabitacion;
import ddb.deso.habitaciones.Reserva;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins ={"http://localhost:3000/", "http://localhost:8080"})
public class ReservaController {

    private final GestorHabitacion gestorHabitacion;

    public ReservaController(GestorHabitacion gestorHabitacion) {
        this.gestorHabitacion = gestorHabitacion;
    }

    public class PostDTO {
        ReservaDTO reservaDTO;
        List<Long> listaIDHabitaciones;
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




}
