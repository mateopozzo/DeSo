package ddb.deso;

import ddb.deso.almacenamiento.DAO.*;
import ddb.deso.almacenamiento.DTO.GenerarFacturaRequestDTO;
import ddb.deso.almacenamiento.DTO.ServicioDTO;
import ddb.deso.negocio.TipoFactura;
import ddb.deso.negocio.TipoHab;
import ddb.deso.negocio.TipoServicio;
import ddb.deso.negocio.alojamiento.*;
import ddb.deso.negocio.contabilidad.Factura;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    // --- Helpers para datos de prueba ---

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
        e.setHabitacion(crearHabitacion(nroHab,1000.0f)); // Tarifa 1000
        e.setFecha_inicio(inicio);
        e.setFecha_fin(fin);
        e.setDatosCheckOut(null); // Activa
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

    // --- Tests de existeEstadia ---

    @Test
    public void existeEstadia_HabitacionNoEncontrada() throws Exception {
        when(habitacionDAO.buscarPorNumero(999L)).thenReturn(null);
        
        Exception ex = assertThrows(Exception.class, () -> 
            gestorContabilidad.existeEstadia(999L)
        );
        assertEquals("Habitación no encontrada", ex.getMessage());
    }

    @Test
    public void existeEstadia_SinEstadiaActiva() throws Exception {
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(crearHabitacion(101L,100.0f));
        when(estadiaDAO.listar()).thenReturn(new ArrayList<>()); // Lista vacía
        
        Exception ex = assertThrows(Exception.class, () -> 
            gestorContabilidad.existeEstadia(101L)
        );
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

    // --- Tests de calcularDetalleFacturacion ---

    @Test
    public void calcularDetalle_CalculoBasicoSinRecargo() throws Exception {
        // Setup: 2 días de estadía, tarifa 1000, checkout a las 10:00 (sin recargo)
        Estadia estadia = crearEstadiaActiva(101L, LocalDate.now().minusDays(2), LocalDate.now());
        // Simular respuesta de existeEstadia dentro del método (mockeando DAOs)
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(estadia.getHabitacion());
        when(estadiaDAO.listar()).thenReturn(List.of(estadia));

        // Checkout a las 10:00 AM
        var detalle = gestorContabilidad.calcularDetalleFacturacion(101L, LocalTime.of(10, 0));

        // 2 días * 1000 = 2000
        assertEquals(2000.0, detalle.getMontoTotal());
    }

    @Test
    public void calcularDetalle_ConRecargoMedioDia() throws Exception {
        // Setup: Checkout a las 12:00 PM (entre 11 y 18 -> +50% de 1 noche)
        Estadia estadia = crearEstadiaActiva(101L, LocalDate.now().minusDays(2), LocalDate.now());
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(estadia.getHabitacion());
        when(estadiaDAO.listar()).thenReturn(List.of(estadia));

        var detalle = gestorContabilidad.calcularDetalleFacturacion(101L, LocalTime.of(12, 0));

        // (2 días * 1000) + (0.5 * 1000) = 2500
        assertEquals(2500.0, detalle.getMontoTotal());
    }

    @Test
    public void calcularDetalle_ConRecargoDiaCompleto() throws Exception {
        // Setup: Checkout a las 19:00 PM (después de 18 -> +100% de 1 noche)
        Estadia estadia = crearEstadiaActiva(101L, LocalDate.now().minusDays(2), LocalDate.now());
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(estadia.getHabitacion());
        when(estadiaDAO.listar()).thenReturn(List.of(estadia));

        var detalle = gestorContabilidad.calcularDetalleFacturacion(101L, LocalTime.of(19, 0));

        // (2 días * 1000) + (1 * 1000) = 3000
        assertEquals(3000.0, detalle.getMontoTotal());
    }

    @Test
    public void calcularDetalle_ConServicios() throws Exception {
        Estadia estadia = crearEstadiaActiva(101L, LocalDate.now().minusDays(1), LocalDate.now());
        Servicio s1 = new Servicio(); s1.setTipo_servicio(TipoServicio.BAR); // 4500
        estadia.getListaServicios().add(s1);

        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(estadia.getHabitacion());
        when(estadiaDAO.listar()).thenReturn(List.of(estadia));

        var detalle = gestorContabilidad.calcularDetalleFacturacion(101L, LocalTime.of(10, 0));

        // (1 día * 1000) + 4500 = 5500
        assertEquals(5500.0, detalle.getMontoTotal());
        assertEquals(1, detalle.getConsumos().size());
    }

    // --- Tests de generarFactura ---

    @Test
    public void generarFactura_FallaPorMenorDeEdad() {
        Long idResp = 20304050L;
        GenerarFacturaRequestDTO request = new GenerarFacturaRequestDTO(1L, idResp, null);
        
        // Simulamos que el responsable es un alojado de 17 años
        Alojado menor = crearAlojadoConEdad(17, String.valueOf(idResp));
        when(alojadoDAO.listarAlojados()).thenReturn(List.of(menor));

        assertThrows(ResponsableMenorEdadExcepcion.class, () -> 
            gestorContabilidad.generarFactura(request)
        );
    }
    //falla porque me falta lo del check out
    @Test
    public void generarFactura_FacturaA_Exito() throws Exception {
        Long idResp = 20304050L;
        GenerarFacturaRequestDTO request = new GenerarFacturaRequestDTO(1L, idResp, null);
        
        // 1. Validar edad (>18)
        Alojado adulto = crearAlojadoConEdad(30, String.valueOf(idResp));
        when(alojadoDAO.listarAlojados()).thenReturn(List.of(adulto));

        // 2. Mockear Estadia y Responsable
        Estadia estadia = crearEstadiaActiva(101L, LocalDate.now().minusDays(1), LocalDate.now());
        // Simular que ya se hizo el checkout previamente para tener hora
        DatosCheckOut dco = new DatosCheckOut();
        dco.setFecha_hora_out(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)));
        estadia.setDatosCheckOut(dco);

        ResponsablePago resp = new ResponsablePago();
        resp.setCuit(idResp);
        resp.setRazonSocial("Empresa SA");
        resp.setCuit(20304050L); // CUIT válido -> Factura A

        when(estadiaDAO.read(1L)).thenReturn(estadia);
        when(responsablePagoDAO.read(idResp)).thenReturn(resp);
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(estadia.getHabitacion());
        when(estadiaDAO.listar()).thenReturn(List.of(estadia)); // Necesario para calcularDetalle

        // Ejecución
        var facturaDTO = gestorContabilidad.generarFactura(request);

        // Verificaciones
        assertNotNull(facturaDTO);
        assertEquals(TipoFactura.A, facturaDTO.getTipo_factura()); // Debe ser A por tener CUIT
        verify(facturaDAO, times(1)).crearFactura(any()); // Se guardó en BD
        
        // Validar desglose IVA (aprox)
        // Total = 1000. Neto = 1000/1.21 = 826.44. IVA = 173.55
        assertTrue(facturaDTO.getImporte_iva() > 0);
    }

    @Test
    public void generarFactura_FacturaB_Exito() throws Exception {
        Long idResp = 11111111L;
        GenerarFacturaRequestDTO request = new GenerarFacturaRequestDTO(1L, idResp, null);
        
        Alojado adulto = crearAlojadoConEdad(25, String.valueOf(idResp));
        when(alojadoDAO.listarAlojados()).thenReturn(List.of(adulto));

        Estadia estadia = crearEstadiaActiva(101L, LocalDate.now().minusDays(1), LocalDate.now());
        DatosCheckOut dco = new DatosCheckOut();
        dco.setFecha_hora_out(LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)));
        estadia.setDatosCheckOut(dco);

        ResponsablePago resp = new ResponsablePago();
        resp.setCuit(null); // Sin CUIT -> Factura B

        when(estadiaDAO.read(1L)).thenReturn(estadia);
        when(responsablePagoDAO.read(idResp)).thenReturn(resp);
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(estadia.getHabitacion());
        when(estadiaDAO.listar()).thenReturn(List.of(estadia));

        var facturaDTO = gestorContabilidad.generarFactura(request);

        assertEquals(TipoFactura.B, facturaDTO.getTipo_factura());
        assertEquals(0.0, facturaDTO.getImporte_iva()); // IVA 0 en B
    }
}