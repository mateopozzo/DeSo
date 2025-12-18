package ddb.deso;

import ddb.deso.almacenamiento.DAO.*;
import ddb.deso.almacenamiento.DTO.FacturaDTO;
import ddb.deso.almacenamiento.DTO.GenerarFacturaRequestDTO;
import ddb.deso.negocio.TipoFactura;
import ddb.deso.negocio.TipoHab;
import ddb.deso.negocio.alojamiento.*;
import ddb.deso.negocio.contabilidad.ResponsablePago;
import ddb.deso.negocio.habitaciones.Estadia;
import ddb.deso.negocio.habitaciones.Habitacion;
import ddb.deso.service.GestorContabilidad;
import ddb.deso.service.excepciones.ResponsableMenorEdadExcepcion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestCU07GestorUnitario {

    @Mock private EstadiaDAO estadiaDAO;
    @Mock private FacturaDAO facturaDAO;
    @Mock private ResponsablePagoDAO responsablePagoDAO;
    @Mock private AlojadoDAO alojadoDAO;
    @Mock private HabitacionDAO habitacionDAO;

    @InjectMocks
    private GestorContabilidad gestorContabilidad;

    // Helper para crear la entidad Alojado que usa validarEdadResponsable
    private Alojado crearAlojadoConEdad(int edad, String cuit) {
        DatosPersonales dp = new DatosPersonales();
        dp.setFechanac(LocalDate.now().minusYears(edad));
        dp.setCUIT(cuit);
        DatosAlojado da = new DatosAlojado();
        da.setDatos_personales(dp);
        Huesped h = new Huesped();
        h.setDatos(da);
        return h;
    }

   @Test
public void generarFactura_FacturaA_Exito() throws Exception {
    // 1. Datos de entrada
    Long idRespLong = 20304050607L;
    String idRespStr = "20304050607";

    // El DTO debe tener la lista de consumos inicializada para evitar NullPointerException
    GenerarFacturaRequestDTO request = GenerarFacturaRequestDTO.builder()
            .idEstadia(1L)
            .responsableId(idRespStr) 
            .tipoFactura(TipoFactura.A)
            .idsConsumosAIncluir(new ArrayList<>()) 
            .cobrarAlojamiento(true)
            .destinatario("Empresa de Prueba SA")
            .build();
    
    
    Estadia estadia = new Estadia();
    estadia.setIdEstadia(1L);
    
    Habitacion hab = new Habitacion();
    hab.setNroHab(101L);
    hab.setTarifa(2500.0f);
    hab.setTipo_hab(TipoHab.DOBLEESTANDAR);
    estadia.setHabitacion(hab); // Requerido para evitar NullPointerException
    
    estadia.setFecha_inicio(LocalDate.now().minusDays(2));
    estadia.setFecha_fin(LocalDate.now());
    estadia.setListaServicios(new ArrayList<>()); // Evita error al iterar servicios

    // 3. Mock de Responsable de Pago
    ResponsablePago resp = new ResponsablePago();
    resp.setCuit(idRespLong); 
    resp.setRazonSocial("Empresa de Prueba SA");

    // 4. Configuración de DAOs
    when(estadiaDAO.read(1L)).thenReturn(estadia);
    when(responsablePagoDAO.read(idRespLong)).thenReturn(resp);
    
    // 5. Ejecución
    FacturaDTO resultado = gestorContabilidad.generarFactura(request);

    // 6. Verificaciones
    assertNotNull(resultado);
    assertEquals(TipoFactura.A, resultado.getTipo_factura());
    
    assertTrue(resultado.getImporte_iva() > 0); 
    verify(facturaDAO, times(1)).crearFactura(any());
}

    @Test
    public void generarFactura_FallaPorIdResponsableInvalido() {
        
        GenerarFacturaRequestDTO request = GenerarFacturaRequestDTO.builder()
                .responsableId("no-soy-un-numero") 
                .build();

        Exception exception = assertThrows(Exception.class, () -> {
            gestorContabilidad.generarFactura(request);
        });

        assertEquals("ID de responsable inválido", exception.getMessage());
    }

    @Test
    public void generacionArchivoPDF_Factura(){
        FacturaDTO factura = instanciarFacturaHardCodeada();
        assertDoesNotThrow(() -> gestorContabilidad.guardarFacturaSegunStrategy(factura, "pdf"));
    }

    @Test
    public void generacionArchivoJSON_Factura(){
        FacturaDTO factura = instanciarFacturaHardCodeada();
        assertDoesNotThrow(() -> gestorContabilidad.guardarFacturaSegunStrategy(factura, "json"));
    }

    private FacturaDTO instanciarFacturaHardCodeada(){
        FacturaDTO factura = new FacturaDTO();
        factura.setNum_factura(123456);
        factura.setFecha_factura(java.time.LocalDate.now());
        factura.setTipo_factura(ddb.deso.negocio.TipoFactura.B);
        factura.setDestinatario("Cliente de Prueba S.R.L.");
        factura.setImporte_neto(10000.0f);
        factura.setImporte_iva(2100.0f);
        factura.setImporte_total(12100.0f);
        return factura;
    }
}