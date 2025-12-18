package ddb.deso;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ddb.deso.almacenamiento.DAO.*;
import ddb.deso.almacenamiento.DTO.GenerarFacturaRequestDTO;
import ddb.deso.negocio.EstadoHab;
import ddb.deso.negocio.TipoHab;
import ddb.deso.negocio.TipoFactura;
import ddb.deso.negocio.alojamiento.DatosCheckOut;
import ddb.deso.negocio.contabilidad.ResponsablePago;
import ddb.deso.negocio.habitaciones.Estadia;
import ddb.deso.negocio.habitaciones.Habitacion;
import ddb.deso.service.GestorContabilidad;

@SpringBootTest
@Transactional
@Rollback(true)
public class TestCU07Integracion {

    @Autowired private GestorContabilidad gestorContabilidad;
    @Autowired private HabitacionDAO habitacionDAO;
    @Autowired private EstadiaDAO estadiaDAO;
    @Autowired private ResponsablePagoDAO responsablePagoDAO;
    @Autowired private DatosCheckOutDAO checkOutDAO;

    @Test
    public void pruebaGenerarFacturaCompleta() throws Exception {
        // 1. PREPARACIÓN DE DATOS (Fixture)
        
        // --- A. Habitación ---
        Habitacion hab = new Habitacion(TipoHab.DOBLEESTANDAR, EstadoHab.OCUPADA, 2000.0f, 2);
        habitacionDAO.crearHabitacion(hab); 

        // --- B. Responsable de Pago ---
        
        ResponsablePago responsable = new ResponsablePago();
        responsable.setRazonSocial("Integration Test SA");
        responsable.setCuit(20304050607L); 
        responsablePagoDAO.crear(responsable); 

        // --- C. CheckOut ---
        DatosCheckOut dco = new DatosCheckOut();
        dco.setFecha_hora_out(LocalDateTime.now().withHour(10).withMinute(0));
        checkOutDAO.crearDatosCheckOut(dco); 

        // --- D. Estadía ---
        Estadia estadia = new Estadia();
        estadia.setHabitacion(hab);
        estadia.setFecha_inicio(LocalDate.now().minusDays(3));
        estadia.setFecha_fin(LocalDate.now());
        estadia.setListaServicios(new ArrayList<>());
        estadia.setDatosCheckOut(dco); 
        estadiaDAO.crear(estadia); 

        // 2. EJECUCIÓN
        
        GenerarFacturaRequestDTO request = GenerarFacturaRequestDTO.builder()
                .idEstadia(estadia.getIdEstadia())
                .responsableId("20304050607") 
                .tipoFactura(TipoFactura.A)
                .cobrarAlojamiento(true)
                .destinatario("Integration Test SA")
                .idsConsumosAIncluir(new ArrayList<>()) // Obligatorio para evitar NullPointerException
                .build();

        var facturaGenerada = gestorContabilidad.generarFactura(request);

        // 3. VERIFICACIONES
        assertNotNull(facturaGenerada);
        
        assertTrue(facturaGenerada.getImporte_total() >= 6000.0);
    }
}
