package ddb.deso.controller;

import ddb.deso.almacenamiento.DTO.CrearEstadiaDTO;
import ddb.deso.gestores.GestorHabitacion;
import ddb.deso.habitaciones.Estadia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
public class EstadiaController {

    private final GestorHabitacion gestorHabitacion;

    @Autowired
    public EstadiaController(GestorHabitacion gestorHabitacion) {this.gestorHabitacion = gestorHabitacion;}

    @PostMapping("/api/ocupar-habitacion")
    public ResponseEntity<CrearEstadiaDTO> crearEstadia(@RequestBody CrearEstadiaDTO estadiaDTO) {

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
}
