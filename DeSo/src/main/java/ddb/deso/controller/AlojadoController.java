package ddb.deso.controller;

import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.JPA.AlojadoDAOJPA;
import ddb.deso.alojamiento.FactoryAlojado;
import ddb.deso.gestores.GestorAlojamiento;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.repository.AlojadoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar las operaciones de Huéspedes.
 * Escucha las peticiones web y las delega al GestorAlojamiento.
 */
@RestController
@RequestMapping("/api/huesped")
@CrossOrigin(origins = "http://localhost:3000")
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
    public ResponseEntity<AlojadoDTO> crearAlojado(@RequestBody AlojadoDTO alojadoDTO, @RequestParam(required = false, defaultValue = "false") boolean force) {

        ddb.deso.alojamiento.Alojado nuevoAlojado = FactoryAlojado.createFromDTO(alojadoDTO);

        if (nuevoAlojado == null) {
            // No pudo crear=>Devuelve 400 Bad Request
            return ResponseEntity.badRequest().build();
        }

        if (!force) {
            boolean existe_doc = gestorAlojamiento.dniExiste(
                    nuevoAlojado.getDatos().getNroDoc(),
                    nuevoAlojado.getDatos().getTipoDoc()
            );

            if(existe_doc) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(alojadoDTO);
    }

    // @GetMapping -> para buscar
    // @PutMapping -> para modificar
    // @DeleteMapping -> para borrar
}