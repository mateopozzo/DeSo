package ddb.deso.controller;

import ddb.deso.TipoDoc;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.CriteriosBusq;
import ddb.deso.alojamiento.FactoryAlojado;
import ddb.deso.alojamiento.Huesped;
import ddb.deso.gestores.GestorAlojamiento;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.gestores.excepciones.AlojadoInvalidoException;
import ddb.deso.gestores.excepciones.AlojadosSinCoincidenciasException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador REST para gestionar las operaciones de Huéspedes.
 * Escucha las peticiones web y las delega al GestorAlojamiento.
 */
@RestController
@CrossOrigin(origins ={"http://localhost:3000/", "http://localhost:8080"})
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

//        System.out.println(nuevoAlojado.getDatos().getDatos_personales().getNombre());
//        System.out.println(nuevoAlojado.getDatos().getDatos_contacto().getEmail());
//        System.out.println(nuevoAlojado.getDatos().getDatos_residencia().getCalle());

        if (nuevoAlojado == null) {
            // No pudo crear=>Devuelve 400 Bad Request
//            System.out.println("No pudo crear");
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

        if(!nuevoAlojado.verificarCamposObligatorios() || !alojadoDTO.verificarCamposObligatorios() ){
//            System.out.println("Algun campo esta mal\naloj:" + nuevoAlojado.verificarCamposObligatorios() + "\ndto: " +  alojadoDTO.verificarCamposObligatorios() );
            return ResponseEntity.badRequest().build();
        }

        try{
            gestorAlojamiento.darDeAltaHuesped(nuevoAlojado);
        } catch (AlojadoInvalidoException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(alojadoDTO);
    }

    private List<CriteriosBusq> conversionAlojadoToCriterio(List<? extends Alojado> listaAlojado) {
        List<CriteriosBusq>retornoEncontrados = new ArrayList<>();
        for(var a: listaAlojado){
            retornoEncontrados.add(
                    new CriteriosBusq(
                            a.getDatos().getDatos_personales().getApellido(),
                            a.getDatos().getDatos_personales().getNombre(),
                            a.getDatos().getTipoDoc(),
                            a.getId().getNroDoc()
                    )
            );
        }
        return retornoEncontrados;
    }


    @GetMapping("/api/buscar-alojados")
    List<CriteriosBusq> obtenerAlojados(@RequestParam(required = false) String apellido,
                                        @RequestParam(required = false) String nombre,
                                        @RequestParam(required = false) TipoDoc tipoDoc,
                                        @RequestParam(required = false) String nroDoc) {

        CriteriosBusq criteriosBusq = new CriteriosBusq(apellido,nombre,tipoDoc,nroDoc);

        if(criteriosBusq == null)
            criteriosBusq = new CriteriosBusq();

        List<Alojado> alojadosEncontrados;
        try {
            alojadosEncontrados = gestorAlojamiento.buscarAlojado(criteriosBusq);
        } catch (AlojadosSinCoincidenciasException e) {
            System.out.println(e.getMessage());
            return null;
        }

        return conversionAlojadoToCriterio(alojadosEncontrados);
    }


    @GetMapping("/api/buscar-huesped")
    List <CriteriosBusq> obtenerHuespedes(@RequestParam(required = false) String apellido,
                                          @RequestParam(required = false) String nombre,
                                          @RequestParam(required = false) TipoDoc tipoDoc,
                                          @RequestParam(required = false) String nroDoc) {

        CriteriosBusq criteriosBusq = new CriteriosBusq(apellido,nombre,tipoDoc,nroDoc);

//        System.out.println(tipoDoc.toString());

        if(criteriosBusq == null)
            criteriosBusq = new CriteriosBusq();

        List<Huesped> huespedesEncontrados;


        try {
            huespedesEncontrados = gestorAlojamiento.buscarHuesped(criteriosBusq);
        } catch (AlojadosSinCoincidenciasException e) {
            System.out.println(e.getMessage());
            return null;
        }

//        System.out.println("Cantidad encontrada " + huespedesEncontrados.size());

        return conversionAlojadoToCriterio(huespedesEncontrados);
    }


    // memo
    // @PutMapping    ->    modificar
    // @DeleteMapping ->    borrar
}