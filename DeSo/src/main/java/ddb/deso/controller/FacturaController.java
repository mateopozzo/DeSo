package ddb.deso.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ddb.deso.service.GestorFactura;
import ddb.deso.almacenamiento.DTO.*;
import org.springframework.beans.factory.annotation.*;

@RestController
@RequestMapping("/api/facturacion")
public class FacturaController {

    @Autowired
    private GestorFactura gestorFacturacion;

    // Paso 1, 2 y 3 del CU: Buscar ocupantes e ítems a facturar
    @GetMapping("/habitacion/{nroHabitacion}/detalle")
    public ResponseEntity<DetalleFacturaDTO> obtenerDetalleFacturacion(
            @PathVariable Integer nroHabitacion,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaSalida) {
        
        try {
            DetalleFacturaDTO detalle = gestorFacturacion.calcularDetalleFacturacion(nroHabitacion, horaSalida);
            return ResponseEntity.ok(detalle);
        } catch (HabitacionNoOcupadaException | HabitacionInexistenteException e) {
            return ResponseEntity.badRequest().body(null); // O manejar con ControllerAdvice
        }
    }

    // Paso 7 y 8 del CU: Generar la factura
    @PostMapping("/generar")
    public ResponseEntity<FacturaDTO> generarFactura(@RequestBody GenerarFacturaRequestDTO request) {
        try {
            FacturaDTO factura = gestorFacturacion.generarFactura(request);
            return ResponseEntity.ok(factura);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
