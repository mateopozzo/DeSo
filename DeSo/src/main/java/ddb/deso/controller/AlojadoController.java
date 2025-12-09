package ddb.deso.controller;

import ddb.deso.service.TipoDoc;
import ddb.deso.almacenamiento.DTO.CriteriosBusq;
import ddb.deso.gestores.GestorAlojamiento;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.gestores.excepciones.AlojadoInvalidoException;
import ddb.deso.gestores.excepciones.AlojadosSinCoincidenciasException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * Controlador REST para gestionar las operaciones de Huéspedes.
 * Escucha las peticiones web y las delega al GestorAlojamiento.
 */
@RestController
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
    public ResponseEntity<String> crearAlojado(@RequestBody AlojadoDTO alojadoDTO, @RequestParam(required = false, defaultValue = "false") boolean force) {

        if (alojadoDTO == null) {
            // No pudo crear=>Devuelve 400 Bad Request
            return ResponseEntity.badRequest().build();
        }

        if (!force) {
            boolean existe_doc = gestorAlojamiento.dniExiste(
                    alojadoDTO.getNroDoc(),
                    alojadoDTO.getTipoDoc()
            );

            if(existe_doc) {
                // Ya existe el alojado => conflicto
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El alojado " + alojadoDTO.getTipoDoc() + " " + alojadoDTO.getNroDoc() + "ya existe");
            }
        }

        if(!alojadoDTO.verificarCamposObligatorios() ){
            // Los campos obligatorios no estan llenos => badReq
            return ResponseEntity.badRequest().body("Datos invalidos");
        }

        try{
            gestorAlojamiento.darDeAltaHuesped(alojadoDTO);
        } catch (AlojadoInvalidoException e) {
            // Alojado invalido =>  BadReq
            return ResponseEntity.badRequest().body("Error de aplicacion" + e.getMessage());
        } catch (Exception e) {
            // Excepcion impreviste => HTTP500
            return ResponseEntity.internalServerError().body("Error de aplicacion" + e.getMessage());
        }

        // EXITO
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    @GetMapping("/api/buscar-alojados")
    public ResponseEntity<List<CriteriosBusq>> obtenerAlojados(
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) TipoDoc tipoDoc,
            @RequestParam(required = false) String nroDoc
    ) {

        // Arma criterio
        CriteriosBusq criteriosBusq = new CriteriosBusq(apellido,nombre,tipoDoc,nroDoc);

        List<CriteriosBusq> alojadosEncontrados;
        try {
            alojadosEncontrados = gestorAlojamiento.buscarAlojado(criteriosBusq);
        } catch (AlojadosSinCoincidenciasException e) {
            // No se cumplen los criterios
            System.out.println(e.getMessage());
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(alojadosEncontrados);
    }


    @GetMapping("/api/buscar-huesped")
    private ResponseEntity<List <CriteriosBusq>> obtenerHuespedes(
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) TipoDoc tipoDoc,
            @RequestParam(required = false) String nroDoc
    ) {

        // Arma criterio
        CriteriosBusq criteriosBusq = new CriteriosBusq(apellido,nombre,tipoDoc,nroDoc);

        List<CriteriosBusq> huespedesEncontrados;

        try {
            huespedesEncontrados = gestorAlojamiento.buscarHuesped(criteriosBusq);
        } catch (AlojadosSinCoincidenciasException e) {
            // No se cumplen los criterios
            System.out.println(e.getMessage());
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(huespedesEncontrados);
    }


    // memo
    // @PutMapping    ->    modificar
    // @DeleteMapping ->    borrar
}