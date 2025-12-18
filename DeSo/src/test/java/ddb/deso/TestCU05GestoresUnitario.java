package ddb.deso;

import ddb.deso.almacenamiento.DAO.*;
import ddb.deso.almacenamiento.DTO.ConsultarReservasDTO;
import ddb.deso.almacenamiento.DTO.CriteriosBusq;
import ddb.deso.almacenamiento.DTO.DatosCheckOutDTO;
import ddb.deso.negocio.TipoDoc;
import ddb.deso.negocio.alojamiento.DatosAlojado;
import ddb.deso.negocio.alojamiento.Huesped;
import ddb.deso.negocio.alojamiento.Invitado;
import ddb.deso.negocio.habitaciones.Habitacion;
import ddb.deso.negocio.habitaciones.Reserva;
import ddb.deso.service.GestorHabitacion;
import ddb.deso.service.excepciones.AlojadoInvalidoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class TestCU05GestoresUnitario {
    @Mock
    private AlojadoDAO alojadoDAO;
    @Mock private HabitacionDAO habitacionDAO;
    @Mock private ReservaDAO reservaDAO;
    @Mock private DatosCheckInDAO checkInDAO;
    @Mock private EstadiaDAO estadiaDAO;
    @Mock private DatosCheckOutDAO checkOutDAO;
    @InjectMocks
    private GestorHabitacion gestorHabitacion;

    @Test
    public void ocuparHabitacion_PromocionInvitadoAHuesped() {
        CriteriosBusq criterios = new CriteriosBusq(null,null, TipoDoc.DNI,"123");

        // Mockeamos un Invitado que debe ser promovido
        Invitado invitado = mock(Invitado.class);
        DatosAlojado datos = new DatosAlojado();
        datos.setNroDoc("123");
        datos.setTipoDoc(TipoDoc.DNI);
        when(invitado.getDatos()).thenReturn(datos);

        // Mockeamos el Huesped tras la promoción
        Huesped huesped = mock(Huesped.class);
        when(huesped.getDatos()).thenReturn(datos);

        when(alojadoDAO.buscarAlojado(criterios)).thenReturn(List.of(invitado), List.of(huesped));
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(new Habitacion());

        assertDoesNotThrow(() ->
                gestorHabitacion.ocuparHabitacion(101L, 1L, criterios, List.of(), LocalDate.now(), LocalDate.now().plusDays(1))
        );

        verify(alojadoDAO).promoverAHuesped("123", "DNI"); // Verifica la rama de promoción
    }

    @Test
    public void ocuparHabitacion_AlojadoDuplicado_LanzaExcepcion() {
        CriteriosBusq criterios = new CriteriosBusq(null,null, TipoDoc.DNI,"123");
        when(alojadoDAO.buscarAlojado(criterios)).thenReturn(List.of(new Huesped(), new Huesped()));

        assertThrows(AlojadoInvalidoException.class, () ->
                gestorHabitacion.ocuparHabitacion(101L, 1L, criterios, List.of(), LocalDate.now(), LocalDate.now())
        );
    }

    @Test
    public void consultarReservas_FiltroExitoso() {
        ConsultarReservasDTO filtro = new ConsultarReservasDTO();
        filtro.setFechaInicio("2025-12-01");
        filtro.setFechaFin("2025-12-10");
        filtro.setIdHabitacion(101L);

        Reserva r = new Reserva();
        Habitacion h = new Habitacion(); h.setNroHab(101L);
        r.setListaHabitaciones(List.of(h));
        r.setEstado("Reservado");

        when(reservaDAO.listarPorFecha(any(), any())).thenReturn(List.of(r));

        var resultado = gestorHabitacion.consultarReservas(filtro);
        assertEquals(1, resultado.size()); // Cubre la rama del stream/anyMatch
    }

    @Test
    public void listarMetodos_CoberturaRamasNull() {
        when(habitacionDAO.listar()).thenReturn(null);
        assertNull(gestorHabitacion.listarHabitaciones()); // Cubre if(listaHabitaciones == null)

        when(reservaDAO.listar()).thenReturn(null);
        assertTrue(gestorHabitacion.listarReservas().isEmpty());
    }

    @Test
    public void guardarDatosCheckOut_FlujoCompleto() {
        DatosCheckOutDTO dto = new DatosCheckOutDTO();
        dto.setNroDoc(List.of("123"));
        dto.setTipoDoc(List.of(TipoDoc.DNI));

        Huesped h = new Huesped();
        h.setDatos(new DatosAlojado());
        when(alojadoDAO.buscarPorDNI("123", TipoDoc.DNI)).thenReturn(h);

        gestorHabitacion.guardarDatosCheckOut(dto);
        verify(checkOutDAO).crearDatosCheckOut(any()); // Cubre el bucle for de alojados
    }
}
