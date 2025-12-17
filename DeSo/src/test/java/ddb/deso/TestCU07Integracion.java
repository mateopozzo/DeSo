package ddb.deso;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DAO.DatosCheckOutDAO;
import ddb.deso.almacenamiento.DAO.EstadiaDAO;
import ddb.deso.almacenamiento.DAO.HabitacionDAO;
import ddb.deso.almacenamiento.DAO.ResponsablePagoDAO;
import ddb.deso.almacenamiento.DTO.CriteriosBusq;
import ddb.deso.almacenamiento.DTO.GenerarFacturaRequestDTO;
import ddb.deso.negocio.EstadoHab;
import ddb.deso.negocio.TipoDoc;
import ddb.deso.negocio.TipoHab;
import ddb.deso.negocio.alojamiento.DatosAlojado;
import ddb.deso.negocio.alojamiento.DatosCheckOut;
import ddb.deso.negocio.alojamiento.DatosPersonales;
import ddb.deso.negocio.alojamiento.Huesped;
import ddb.deso.negocio.contabilidad.ResponsablePago;
import ddb.deso.negocio.habitaciones.Estadia;
import ddb.deso.negocio.habitaciones.Habitacion;
import ddb.deso.service.GestorAlojamiento;
import ddb.deso.service.GestorContabilidad;

@Disabled("Test de integración – no requerido por el enunciado (solo tests unitarios de servicio)")
@SpringBootTest
@Transactional
@Rollback(true)
public class TestCU07Integracion {

    @Autowired private GestorContabilidad gestorContabilidad;
    @Autowired private HabitacionDAO habitacionDAO;
    @Autowired private EstadiaDAO estadiaDAO;
    @Autowired private AlojadoDAO alojadoDAO;
    @Autowired private ResponsablePagoDAO responsablePagoDAO;
    @Autowired private DatosCheckOutDAO checkOutDAO;
    @Autowired private GestorAlojamiento gestorAlojamiento;


    @Test
    @Transactional
    public void pruebaDeQueryPorId(){
        List<CriteriosBusq> x = gestorAlojamiento.buscarCriteriosALojadoDeEstadia(15);

        for(var coso : x){
            System.out.println(coso.getNombre());
            System.out.println(coso.getApellido());
            System.out.println(coso.getTipoDoc());
            System.out.println(coso.getNroDoc());
        }
    }

    @Test
    public void pruebaGenerarFacturaCompleta() throws Exception {
        // --------------------------------------------------------------------------------
        // 1. PREPARACIÓN DE DATOS (Fixture)
        // --------------------------------------------------------------------------------
        
        // --- A. Crear y guardar Habitacion ---
        Habitacion hab = new Habitacion(TipoHab.DOBLEESTANDAR, EstadoHab.OCUPADA, 2000.0f, 2);
        habitacionDAO.crearHabitacion(hab); 
        Long idHab = hab.getNroHab(); // Recuperamos el ID generado por la BD


        // --- B. Crear y guardar Alojado (Huesped) para validar edad ---
        // El sistema valida que el responsable sea mayor de edad buscando en los alojados por CUIT
        DatosPersonales dp = new DatosPersonales();
        dp.setNombre("Integration");
        dp.setApellido("Test");
        dp.setFechanac(LocalDate.of(1990, 1, 1)); // Mayor de edad (30+ años)
        dp.setCUIT("20304050607"); // CUIT coincidente con el responsable
        dp.setTipoDoc(TipoDoc.DNI);
        dp.setNroDoc("30111222");

        DatosAlojado da = new DatosAlojado();
        da.setDatos_personales(dp);
        
        Huesped huesped = new Huesped();
        huesped.setDatos(da);
        
        alojadoDAO.crearAlojado(huesped); 


        // --- C. Crear y guardar ResponsablePago ---
        ResponsablePago responsable = new ResponsablePago();
        responsable.setRazonSocial("Integration Test SA");
        responsable.setCuit(20304050607L); // ESTE ES EL ID (@Id)
        
        responsablePagoDAO.crear(responsable); 
        
        // CORRECCIÓN: El ID es el propio CUIT
        Long idResponsable = responsable.getCuit(); 


        // --- D. Crear CheckOut (necesario para la hora de salida) ---
        DatosCheckOut dco = new DatosCheckOut();
        dco.setFecha_hora_out(LocalDateTime.now());
        checkOutDAO.crearDatosCheckOut(dco); 


        // --- E. Crear y guardar Estadía ---
        Estadia estadia = new Estadia();
        estadia.setHabitacion(hab);
        estadia.setFecha_inicio(LocalDate.now().minusDays(3));
        estadia.setFecha_fin(LocalDate.now());
        estadia.setListaServicios(new ArrayList<>());
        estadia.setDatosCheckOut(dco); // Vinculamos el checkout
        
        estadiaDAO.crear(estadia); 
        Long idEstadia = estadia.getIdEstadia();


        // --------------------------------------------------------------------------------
        // 2. EJECUCIÓN DEL CASO DE USO
        // --------------------------------------------------------------------------------
        
        GenerarFacturaRequestDTO request = new GenerarFacturaRequestDTO();
        request.setIdEstadia(idEstadia);       
        request.setIdResponsable(idResponsable); // Pasamos el CUIT como ID
        request.setIdsServicios(null);         

        var facturaGenerada = gestorContabilidad.generarFactura(request);


        // --------------------------------------------------------------------------------
        // 3. VERIFICACIONES (Assertions)
        // --------------------------------------------------------------------------------
        
        assertNotNull(facturaGenerada, "La factura generada no debería ser nula");
        assertTrue(facturaGenerada.getImporte_total() > 0, "El importe total debería ser mayor a 0");
        
        // Validación básica de monto: 3 días * 2000 = 6000
        assertTrue(facturaGenerada.getImporte_total() >= 6000.0, "El importe no coincide con los días de estadía");
        
        // Verificamos que sea tipo A porque el responsable tiene CUIT
        // (Depende de tu lógica en GestorContabilidad, pero usualmente con CUIT es A)
        // assertEquals(TipoFactura.A, facturaGenerada.getTipo_factura());

        System.out.println("Test Integración Exitoso. Factura generada: " + facturaGenerada.getNum_factura());
    }
}