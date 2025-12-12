package ddb.deso.controller;

import ddb.deso.negocio.TipoDoc;
import ddb.deso.almacenamiento.DTO.CriteriosBusq;
import ddb.deso.service.GestorAlojamiento;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.service.excepciones.AlojadoInvalidoException;
import ddb.deso.service.excepciones.AlojadosSinCoincidenciasException;
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
     * Escucha peticiones POST en /api/huesped.
     *
     * @param alojadoDTO Los datos del nuevo alojado (vienen en el body del POST en formato JSON).
     * @param force (Opcional) Booleano para forzar la creación incluso si hay validaciones no críticas (por defecto false).
     * @return {@link ResponseEntity} con:
     * <ul>
     * <li>201 CREATED: Si se creó exitosamente.</li>
     * <li>400 BAD REQUEST: Si el body es nulo, faltan campos o el alojado es inválido.</li>
     * <li>409 CONFLICT: Si el documento ya existe (y no se usó force).</li>
     * <li>500 INTERNAL SERVER ERROR: Para errores no previstos.</li>
     * </ul>
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


    /**
     * Busca alojados (invitados o huéspedes) que cumplan con los criterios especificados.
     *
     * @param apellido Filtro por apellido (opcional).
     * @param nombre Filtro por nombre (opcional).
     * @param tipoDoc Filtro por tipo de documento (opcional).
     * @param nroDoc Filtro por número de documento (opcional).
     * @return Lista de {@link CriteriosBusq} con los alojados encontrados o lista vacía si no hay coincidencias.
     */
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
            alojadosEncontrados = gestorAlojamiento.obtenerCriteriosAlojado(criteriosBusq);
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