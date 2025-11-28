package ddb.deso.controller;

import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.CriteriosBusq;
import ddb.deso.alojamiento.FactoryAlojado;
import ddb.deso.alojamiento.Huesped;
import ddb.deso.gestores.GestorAlojamiento;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.gestores.excepciones.AlojadosSinCoincidenciasException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar las operaciones de Huéspedes.
 * Escucha las peticiones web y las delega al GestorAlojamiento.
 */
@RestController
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
    @PostMapping("/api/huesped")
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


    @GetMapping("/api/ocupar-habitacion")
    List<Alojado> obtenerAlojados(@RequestBody CriteriosBusq criteriosBusq) {

        if(criteriosBusq == null){
            return null;
        }

        List<Alojado> alojadosEncontrados;
        try {
            alojadosEncontrados = gestorAlojamiento.buscarAlojado(criteriosBusq);
        } catch (AlojadosSinCoincidenciasException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return alojadosEncontrados;
    }


    @GetMapping("/api/buscar-huesped")
    List<Huesped> obtenerHuespedes(@RequestBody CriteriosBusq criteriosBusq) {

        if(criteriosBusq == null){
            return null;
        }

        List<Huesped> huespedesEncontrados;
        try {
            huespedesEncontrados = gestorAlojamiento.buscarHuesped(criteriosBusq);
        } catch (AlojadosSinCoincidenciasException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return huespedesEncontrados;
    }



    // @GetMapping -> para buscar
    // @PutMapping -> para modificar
    // @DeleteMapping -> para borrar
}