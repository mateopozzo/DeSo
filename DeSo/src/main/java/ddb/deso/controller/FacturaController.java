package ddb.deso.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ddb.deso.service.GestorContabilidad; 
import ddb.deso.almacenamiento.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@RestController
@RequestMapping("/api/facturacion")
public class FacturaController {

    @Autowired
    private GestorContabilidad gestorContabilidad;

    // Paso 1: Pre-visualización
    @GetMapping("/habitacion/{nroHabitacion}/detalle")
    public ResponseEntity<DetalleFacturaDTO> obtenerDetalleFacturacion(
            @PathVariable Long nroHabitacion,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaSalida) {
        
        try {
            // Llama al método del gestor para obtener los cálculos previos
            DetalleFacturaDTO detalle = gestorContabilidad.calcularDetalleFacturacion(nroHabitacion, horaSalida);
            return ResponseEntity.ok(detalle);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null); 
        }
    }

    // Paso 2: Generar la factura
    @PostMapping("/generar")
    public ResponseEntity<FacturaDTO> generarFactura(@RequestBody GenerarFacturaRequestDTO request) {
        try {
            FacturaDTO factura = gestorContabilidad.generarFactura(request);
            return ResponseEntity.ok(factura);
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en la consola del servidor
            return ResponseEntity.badRequest().build();
        }
    }
}