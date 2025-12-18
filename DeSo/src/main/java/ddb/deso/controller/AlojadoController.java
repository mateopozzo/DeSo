package ddb.deso.controller;

import ddb.deso.almacenamiento.DTO.*;
import ddb.deso.controller.enumeradores.BajaHuesped;
import ddb.deso.negocio.TipoDoc;
import ddb.deso.service.GestorAlojamiento;
import ddb.deso.service.GestorHabitacion;
import ddb.deso.service.excepciones.AlojadoInvalidoException;
import ddb.deso.service.excepciones.AlojadoNoEliminableException;
import ddb.deso.service.excepciones.AlojadoPreExistenteException;
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
    private final GestorHabitacion gestorHabitacion;

    /**
     * Constructor para la Inyección de Dependencias.
     * Spring inyectará automáticamente el Bean de GestorAlojamiento.
     */
    public AlojadoController(GestorAlojamiento gestorAlojamiento, GestorHabitacion gestorHabitacion) {
        this.gestorAlojamiento = gestorAlojamiento;
        this.gestorHabitacion = gestorHabitacion;
    }

    /**
     * Endpoint para crear un nuevo alojado (Huésped o Invitado).
     * Escucha peticiones POST en /api/huesped.
     *
     * @param alojadoDTO Los datos del nuevo alojado (vienen en el body del POST en formato JSON).
     * @param force (Opcional) Booleano para forzar la creación incluso si hay validaciones no críticas (por defecto false).
     * @return {@link ResponseEntity} con body {@link String} que devuelve tipo de error
     */
    @PostMapping("/api/huesped")
    public ResponseEntity<String> crearAlojado(@RequestBody AlojadoDTO alojadoDTO, @RequestParam(required = false, defaultValue = "false") boolean force) {

        if (alojadoDTO == null) {
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

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Endpoint para obtener los datos del {@link ddb.deso.negocio.alojamiento.Alojado} que el usuario elige para modificar
     * @param nroDoc : Numero de documento del alojado a buscar
     * @param tipoDocStr : String del tipo de documento del alojado a buscar
     * @return
     */
    @GetMapping("api/obtener-atributos-huesped")
    ResponseEntity<AlojadoDTO> obetenerAtributosAlojado(@RequestParam String nroDoc, @RequestParam String tipoDocStr) {

        if(nroDoc == null || tipoDocStr == null || nroDoc.isEmpty() || tipoDocStr.isEmpty()) {
            return null;
        }

        TipoDoc tipoDoc = null;
        try {
            tipoDoc = TipoDoc.valueOf(tipoDocStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Se intento obtener datos de un Tipo de documento" + tipoDocStr);
            return null;
        }


        var dtoAlojado = gestorAlojamiento.obtenerAlojadoPorDNI(nroDoc, tipoDoc);

        return ResponseEntity.ok().body(dtoAlojado);
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
            alojadosEncontrados = gestorAlojamiento.buscarCriteriosAlojado(criteriosBusq);
        } catch (AlojadosSinCoincidenciasException e) {
            // No se cumplen los criterios
            System.out.println(e.getMessage());
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(alojadosEncontrados);
    }

    /**
     * Busca solo huéspedes que cumplan con los criterios especificados.
     *
     * @param apellido Filtro por apellido (opcional).
     * @param nombre Filtro por nombre (opcional).
     * @param tipoDoc Filtro por tipo de documento (opcional).
     * @param nroDoc Filtro por número de documento (opcional).
     * @return Lista de {@link CriteriosBusq} con los huéspedes encontrados o lista vacía si no hay coincidencias.
     */
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
            huespedesEncontrados = gestorAlojamiento.buscarCriteriosHuesped(criteriosBusq);
        } catch (AlojadosSinCoincidenciasException e) {
            // No se cumplen los criterios
            System.out.println(e.getMessage());
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(huespedesEncontrados);
    }

    /**
     * Busca alojados (invitados o huéspedes) que hayan participado de la estadía.
     *
     * @param idEstadia id de la estadía que se desea operar
     * @return Lista de {@link CriteriosBusq} con los alojados encontrados o lista vacía si no hay coincidencias.
     */
    @GetMapping("api/buscar-huespedes-de-estadia")
    private ResponseEntity<List<CriteriosBusq>> obtenerHuespedesDeEstadia(@RequestParam long idEstadia){
        return ResponseEntity.ok(gestorAlojamiento.buscarCriteriosALojadoDeEstadia(idEstadia));
    }

    /**
     * ENDPOINT para la actualizacion de los datos de una entidad {@link ddb.deso.negocio.alojamiento.Alojado}
     *
     * @return {@link AlojadoDTO} con datos que estan en la base
     */
    @PutMapping("api/actualizar-alojado")
    ResponseEntity<AlojadoDTO> actualizarAlojado(@RequestBody ActualizarAlojadoDTO dto, @RequestParam boolean force){

        var pre = dto.pre;
        var post= dto.post;

        if(pre == null || post == null){
            //TODO -> Definir tipos de retorno a front
            return ResponseEntity.noContent().build();
        }

        System.out.println("NO NULOS");

        AlojadoDTO dtoRta = null;
        try{
            System.out.println("SE LO MANDO AL GESTOR");
            dtoRta = gestorAlojamiento.modificarHuesped(pre, post, force);
        } catch (AlojadoPreExistenteException ape){
            System.out.println(ape.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (AlojadoInvalidoException ea) {
            System.out.println(ea.getMessage());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(dtoRta);

    }

    @PutMapping("/api/generar-check-out")
    ResponseEntity<DatosCheckOutDTO> generarCheckOutAlojado(@RequestBody List<CriteriosBusq> criteriosBusq){

        if(criteriosBusq == null){
            return ResponseEntity.ok().build();
        }

        var cout = gestorAlojamiento.generarCheckOut(criteriosBusq);
        gestorHabitacion.guardarDatosCheckOut(cout);

        return ResponseEntity.ok().body(cout);

    }

    /**
     * ENDPOINT para la eliminacion de los datos de una entidad {@link ddb.deso.negocio.alojamiento.Alojado}
     *
     * @return {@link BajaHuesped} con el estado final de la operacion
     */
    @DeleteMapping("api/eliminar-huesped")
    ResponseEntity<BajaHuesped> darDeBajaAlojado(@RequestBody AlojadoDTO dtoAlojadoPorEliminar){

        if(!identidadValida(dtoAlojadoPorEliminar)){
            return ResponseEntity.badRequest().build();
        }

        try{
            gestorAlojamiento.eliminarAlojado(dtoAlojadoPorEliminar);
        } catch (AlojadoNoEliminableException e){
            return ResponseEntity.ok(BajaHuesped.OPERACION_PROHIBIDA);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        return ResponseEntity.ok(BajaHuesped.DADO_DE_BAJA);
    }

    /**
     * Metodo para verificar la validez de la entidad pasada.
     * @param a
     * @return {@code false} si el tipo de documento o el numero es nulo o vacío, {@code true} de lo contrario
     */
    private boolean identidadValida(AlojadoDTO a){
        if(a==null)return false;
        if(a.getTipoDoc()==null)return false;
        if(a.getNroDoc()==null || a.getNroDoc().isEmpty())return false;
        return true;
    }

    //memo
    // @PutMapping    ->    modificar
    // @DeleteMapping ->    borrar
}