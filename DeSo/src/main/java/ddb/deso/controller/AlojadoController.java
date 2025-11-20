package ddb.deso.controller;

import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.FactoryAlojado;
import ddb.deso.alojamiento.GestorAlojamiento;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Controlador REST para gestionar las operaciones de Huéspedes.
 * Escucha las peticiones web y las delega al GestorAlojamiento.
 */
@RestController
@RequestMapping("/api/huesped")
@CrossOrigin(origins = "http://localhost:63342")
public class AlojadoController {

    private final GestorAlojamiento gestorAlojamiento;

    /**
     * Constructor para la Inyección de Dependencias.
     * Spring inyectará automáticamente el Bean de GestorAlojamiento.
     */
    public AlojadoController(GestorAlojamiento gestorAlojamiento) {
        this.gestorAlojamiento = gestorAlojamiento;
    }

    /**
     * Endpoint para CREAR un nuevo alojado (Huésped o Invitado).
     * Escucha peticiones POST en /api/huesped
     *
     * @param alojadoDTO Los datos del nuevo alojado (vienen en el body del POST en formato JSON)
     * @return El AlojadoDTO creado con un código 201 (Created).
     */
    @PostMapping
    public ResponseEntity<AlojadoDTO> crearAlojado(@RequestBody AlojadoDTO alojadoDTO) {

        Alojado nuevoAlojado = FactoryAlojado.createFromDTO(alojadoDTO);

        if (nuevoAlojado == null) {
            // No pudo crear=>Devuelve 400 Bad Request
            return ResponseEntity.badRequest().build();
        }

        gestorAlojamiento.darDeAltaHuesped(nuevoAlojado);

        return ResponseEntity.status(HttpStatus.CREATED).body(alojadoDTO);
    }

    // @GetMapping -> para buscar
    // @PutMapping -> para modificar
    // @DeleteMapping -> para borrar
}