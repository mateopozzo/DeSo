package ddb.deso;

import ddb.deso.almacenamiento.DAO.*;
import ddb.deso.almacenamiento.DTO.FacturaDTO;
import ddb.deso.almacenamiento.DTO.GenerarFacturaRequestDTO;
import ddb.deso.negocio.TipoFactura;
import ddb.deso.negocio.TipoHab;
import ddb.deso.negocio.TipoServicio;
import ddb.deso.negocio.alojamiento.*;
import ddb.deso.negocio.contabilidad.ResponsablePago;
import ddb.deso.negocio.habitaciones.Estadia;
import ddb.deso.negocio.habitaciones.Habitacion;
import ddb.deso.negocio.habitaciones.Servicio;
import ddb.deso.service.GestorContabilidad;
import ddb.deso.service.excepciones.ResponsableMenorEdadExcepcion;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestCU07GestorUnitario {

    @Mock
    private EstadiaDAO estadiaDAO;
    @Mock
    private FacturaDAO facturaDAO;
    @Mock
    private ResponsablePagoDAO responsablePagoDAO;
    @Mock
    private HabitacionDAO habitacionDAO;
    @Mock
    private AlojadoDAO alojadoDAO;

    @InjectMocks
    private GestorContabilidad gestorContabilidad;

    // --- Helpers ---

    private Habitacion crearHabitacion(Long nro, float tarifa) {
        Habitacion h = new Habitacion();
        h.setNroHab(nro);
        h.setTarifa(tarifa);
        h.setTipo_hab(TipoHab.DOBLEESTANDAR);
        return h;
    }

    private Estadia crearEstadiaActiva(Long nroHab, LocalDate inicio, LocalDate fin) {
        Estadia e = new Estadia();
        e.setIdEstadia(1L);
        e.setHabitacion(crearHabitacion(nroHab,1000.0f)); 
        e.setFecha_inicio(inicio);
        e.setFecha_fin(fin);
        e.setDatosCheckOut(null); 
        e.setListaServicios(new ArrayList<>());
        return e;
    }

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

    // --- Tests de existeEstadia (Estos quedan IGUAL porque SÍ buscan) ---

    @Test
    public void existeEstadia_HabitacionNoEncontrada() throws Exception {
        when(habitacionDAO.buscarPorNumero(999L)).thenReturn(null);
        Exception ex = assertThrows(Exception.class, () -> gestorContabilidad.existeEstadia(999L));
        assertEquals("Habitación no encontrada", ex.getMessage());
    }

    @Test
    public void existeEstadia_SinEstadiaActiva() throws Exception {
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(crearHabitacion(101L,100.0f));
        when(estadiaDAO.listar()).thenReturn(new ArrayList<>()); 
        Exception ex = assertThrows(Exception.class, () -> gestorContabilidad.existeEstadia(101L));
        assertEquals("No hay estadía activa para esta habitación", ex.getMessage());
    }

    @Test
    public void existeEstadia_Exito() throws Exception {
        Estadia estadia = crearEstadiaActiva(101L, LocalDate.now().minusDays(2), LocalDate.now().plusDays(2));
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(estadia.getHabitacion());
        when(estadiaDAO.listar()).thenReturn(List.of(estadia));
        
        Estadia resultado = gestorContabilidad.existeEstadia(101L);
        assertNotNull(resultado);
        assertEquals(101L, resultado.getHabitacion().getNroHab());
    }

    // --- Tests de calcularDetalleFacturacion (Estos quedan IGUAL porque usan existeEstadia) ---

    @Test
    public void calcularDetalle_CalculoBasicoSinRecargo() throws Exception {
        Estadia estadia = crearEstadiaActiva(101L, LocalDate.now().minusDays(2), LocalDate.now());
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(estadia.getHabitacion());
        when(estadiaDAO.listar()).thenReturn(List.of(estadia));

        var detalle = gestorContabilidad.calcularDetalleFacturacion(101L, LocalTime.of(10, 0));
        assertEquals(2000.0, detalle.getMontoTotal());
    }

    @Test
    public void calcularDetalle_ConRecargoMedioDia() throws Exception {
        Estadia estadia = crearEstadiaActiva(101L, LocalDate.now().minusDays(2), LocalDate.now());
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(estadia.getHabitacion());
        when(estadiaDAO.listar()).thenReturn(List.of(estadia));

        var detalle = gestorContabilidad.calcularDetalleFacturacion(101L, LocalTime.of(12, 0));
        assertEquals(2500.0, detalle.getMontoTotal());
    }

    @Test
    public void calcularDetalle_ConRecargoDiaCompleto() throws Exception {
        Estadia estadia = crearEstadiaActiva(101L, LocalDate.now().minusDays(2), LocalDate.now());
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(estadia.getHabitacion());
        when(estadiaDAO.listar()).thenReturn(List.of(estadia));

        var detalle = gestorContabilidad.calcularDetalleFacturacion(101L, LocalTime.of(19, 0));
        assertEquals(3000.0, detalle.getMontoTotal());
    }

    @Test
    public void calcularDetalle_ConServicios() throws Exception {
        Estadia estadia = crearEstadiaActiva(101L, LocalDate.now().minusDays(1), LocalDate.now());
        Servicio s1 = new Servicio(); s1.setTipo_servicio(TipoServicio.BAR); 
        estadia.getListaServicios().add(s1);

        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(estadia.getHabitacion());
        when(estadiaDAO.listar()).thenReturn(List.of(estadia));

        var detalle = gestorContabilidad.calcularDetalleFacturacion(101L, LocalTime.of(10, 0));
        assertEquals(5500.0, detalle.getMontoTotal());
    }

    // --- Tests de generarFactura (AQUÍ ESTÁ LA CORRECCIÓN) ---

    @Test
    public void generarFactura_FallaPorMenorDeEdad() {
        Long idResp = 20304050L;
        GenerarFacturaRequestDTO request = new GenerarFacturaRequestDTO(1L, idResp, null);
        Alojado menor = crearAlojadoConEdad(17, String.valueOf(idResp));
        when(alojadoDAO.listarAlojados()).thenReturn(List.of(menor));

        assertThrows(ResponsableMenorEdadExcepcion.class, () -> gestorContabilidad.generarFactura(request));
    }

    @Test
    public void generarFactura_FacturaA_Exito() throws Exception {
        Long idResp = 20304050607L; 
        GenerarFacturaRequestDTO request = new GenerarFacturaRequestDTO(1L, idResp, null);
        
        Alojado adulto = crearAlojadoConEdad(30, String.valueOf(idResp));
        when(alojadoDAO.listarAlojados()).thenReturn(List.of(adulto));

        // Mockear Estadia (con habitación dentro) y Responsable
        Estadia estadia = crearEstadiaActiva(101L, LocalDate.now().minusDays(1), LocalDate.now());
        
        ResponsablePago resp = new ResponsablePago();
        resp.setCuit(idResp); 
        resp.setRazonSocial("Empresa SA");

        when(estadiaDAO.read(1L)).thenReturn(estadia);
        when(responsablePagoDAO.read(idResp)).thenReturn(resp);
        
        // --- BORRADAS LAS LÍNEAS INNECESARIAS ---
        // when(habitacionDAO.buscarPorNumero...) -> YA NO SE USA
        // when(estadiaDAO.listar()...) -> YA NO SE USA

        var facturaDTO = gestorContabilidad.generarFactura(request);

        assertNotNull(facturaDTO);
        assertEquals(TipoFactura.A, facturaDTO.getTipo_factura());
        verify(facturaDAO, times(1)).crearFactura(any());
        assertTrue(facturaDTO.getImporte_iva() > 0);
    }

    @Test
    public void generarFactura_FacturaB_Exito() throws Exception {
        Long idResp = 0L; 
        GenerarFacturaRequestDTO request = new GenerarFacturaRequestDTO(1L, idResp, null);
        
        Alojado adulto = crearAlojadoConEdad(25, "0"); 
        when(alojadoDAO.listarAlojados()).thenReturn(List.of(adulto));

        Estadia estadia = crearEstadiaActiva(101L, LocalDate.now().minusDays(1), LocalDate.now());
        
        ResponsablePago resp = new ResponsablePago();
        resp.setCuit(idResp); 
        resp.setRazonSocial("Juan Perez");

        when(estadiaDAO.read(1L)).thenReturn(estadia);
        when(responsablePagoDAO.read(idResp)).thenReturn(resp);

        // --- BORRADAS LAS LÍNEAS INNECESARIAS ---
        
        var facturaDTO = gestorContabilidad.generarFactura(request);

        assertEquals(TipoFactura.B, facturaDTO.getTipo_factura());
        assertEquals(0.0, facturaDTO.getImporte_iva());
    }

    @Test
    public void generacionArchivoPDF_Factura(){
        var fatura = instanciarFacturaHardCodeada();
        gestorContabilidad.guardarFacturaSegunStrategy(fatura, "pdf");
    }

    @Test
    public void generacionArchivoJSON_Factura(){
        var fatura = instanciarFacturaHardCodeada();
        gestorContabilidad.guardarFacturaSegunStrategy(fatura, "json");
    }

    private FacturaDTO instanciarFacturaHardCodeada(){
        FacturaDTO fatura = new FacturaDTO();
        fatura.setNum_factura(123456);
        fatura.setFecha_factura(java.time.LocalDate.now());
        fatura.setTipo_factura(ddb.deso.negocio.TipoFactura.B);
        fatura.setDestinatario("Cliente de Prueba S.R.L.");
        fatura.setImporte_neto(10000.0f);
        fatura.setImporte_iva(2100.0f);
        fatura.setImporte_total(12100.0f);
        return fatura;
    }
}