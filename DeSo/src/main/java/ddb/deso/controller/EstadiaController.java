package ddb.deso.controller;

import ddb.deso.almacenamiento.DTO.EstadiaDTO;
import ddb.deso.gestores.GestorHabitacion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:8080"})
public class EstadiaController {
    private final GestorHabitacion gestorHabitacion;

    public EstadiaController(GestorHabitacion gestorHabitacion) {
        this.gestorHabitacion = gestorHabitacion;
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



}
