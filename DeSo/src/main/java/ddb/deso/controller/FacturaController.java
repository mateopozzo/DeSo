package ddb.deso.controller;

import org.hibernate.type.OrderedMapType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ddb.deso.service.GestorContabilidad; 
import ddb.deso.almacenamiento.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import ddb.deso.negocio.habitaciones.Estadia;

import java.time.LocalTime;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;

@RestController
@RequestMapping("/api/facturacion")
public class FacturaController {

    @Autowired
    private GestorContabilidad gestorContabilidad;


    /**
     * ENDPOINT -> /api/facturacion/habitacion/{nroHabitacion}/verificar-estadia
     * Verifica si existe alguna {@link Estadia} activa en disponible para generar la factura dado los parametros del metodo
     * @param nroHabitacion : Numero de habitacion que se va a cobrar
     * @param horaSalida : Hora de salida de la habitacion
     * @return
     */
    @GetMapping("/habitacion/{nroHabitacion}/verificar-estadia")
    public ResponseEntity<EstadiaDTO> verificarEstadiaActiva(
            @PathVariable Long nroHabitacion,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaSalida) {
        
        try {

            if (horaSalida == null) {
                horaSalida = LocalTime.now();
            }

            Estadia estadia = gestorContabilidad.existeEstadia(nroHabitacion);
            EstadiaDTO estadiaDTO= new EstadiaDTO(estadia);
            return ResponseEntity.ok(estadiaDTO);

        } catch (Exception e) {
    
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * ENDPOINT -> /api/facturacion/habitacion/{nroHabitacion}/detalle
     * para calcular detalle de factura para una habitacion
     * @param nroHabitacion : Habitacion a facturar
     * @param horaSalida : Horario de salida de los huespedes
     * @return : {@link DetalleFacturaDTO} con datos cargados, o null frente a badRequest
     */
    @GetMapping("/habitacion/{nroHabitacion}/detalle")
    public ResponseEntity<DetalleFacturaDTO> obtenerDetalleFacturacion(
            @PathVariable Long nroHabitacion,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaSalida) {
        
        try {
            DetalleFacturaDTO detalle = gestorContabilidad.calcularDetalleFacturacion(nroHabitacion, horaSalida);
            return ResponseEntity.ok(detalle);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null); 
        }
    }

    /**
     * Busca en la base de datos una persona juridica que coincida con el CUIT provisto
     *
     * @param cuit: CUIT del la persona juridica
     * @return
     */
    @GetMapping("/buscar-tercero")
    private ResponseEntity<PersonaJuridicaDTO> obtenerHuespedesSegunCUIT(@RequestParam String cuit){
        if(cuit == null || cuit.isEmpty()){
            return ResponseEntity.ok().build();
        }

        System.out.println("estoy en obtenerHuespedesSegunCUIT");

        PersonaJuridicaDTO entidadRetorno = gestorContabilidad.buscarRespPago(cuit);

        return ResponseEntity.ok(entidadRetorno);
    }

    /**
     * ENDPOINT -> /api/facturacion/generar
     * Genera el bruto de la factura
     * @param request
     * @return
     */
    @PostMapping("/generar")
    public ResponseEntity<FacturaDTO> generarFactura(@RequestBody GenerarFacturaRequestDTO request) {
        try {
            System.out.println("IDS CONSUMOS: " + request.getIdsConsumosAIncluir());
            FacturaDTO factura = gestorContabilidad.generarFactura(request);
            return ResponseEntity.ok(factura);
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en la consola del servidor
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * ENDPOINT -> /api/facturacion/imprimir-factura
     * Genera un archivo que se puede imprimir y se guarda en DeSo/data/facturas/factura{id}.{extension}
     * @param factura : Factura a generar
     * @param strat : Estrategia de generacion de archivo, sigue patron Strategy. Estrategias disponibles "pdf", "json"
     * @return
     */
    @PostMapping("/imprimir-factura")
    public ResponseEntity<byte[]> imprimirFactura(
            @RequestBody FacturaDTO factura,
            @RequestParam String strat
    ) {
        byte[] data = gestorContabilidad.guardarFacturaSegunStrategy(factura, strat);

        String nombre;
        String tipo;

        if ("json".equalsIgnoreCase(strat)) {
            nombre = "factura.json";
            tipo = "application/json";
        } else if ("pdf".equalsIgnoreCase(strat)) {
            nombre = "factura.pdf";
            tipo = "application/pdf";
        } else {
            throw new IllegalArgumentException("Formato erróneo: " + strat);
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + nombre)
                .header("Content-Type", tipo)
                .body(data);
    }
  
}