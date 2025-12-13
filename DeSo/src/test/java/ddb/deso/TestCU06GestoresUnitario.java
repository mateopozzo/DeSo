package ddb.deso;

import ddb.deso.almacenamiento.DAO.*;
import ddb.deso.almacenamiento.DTO.ReservaGrillaDTO;
import ddb.deso.service.GestorHabitacion;
import ddb.deso.service.excepciones.ApellidoVacioException;
import ddb.deso.service.excepciones.ReservaInexistenteException;
import ddb.deso.negocio.EstadoHab;
import ddb.deso.negocio.TipoHab;
import ddb.deso.negocio.habitaciones.Habitacion;
import ddb.deso.negocio.habitaciones.Reserva;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestCU06GestoresUnitario {

    @Mock private HabitacionDAO habitacionDAO;
    @Mock private EstadiaDAO estadiaDAO;
    @Mock private ReservaDAO reservaDAO;
    @Mock private AlojadoDAO alojadoDAO;
    @Mock private DatosCheckInDAO checkInDAO;

    @InjectMocks
    private GestorHabitacion gestorHabitacion;

    private Reserva crearReservaConHabitaciones(Long id, String apellido, String nombre) {
        Reserva r = new Reserva(
                LocalDate.parse("2026-01-16"),
                LocalDate.parse("2026-01-19"),
                "Reservado",
                nombre,
                apellido,
                "123"
        );
        r.setIdReserva(id);

        Habitacion h1 = new Habitacion(TipoHab.DOBLEESTANDAR, EstadoHab.DISPONIBLE, 100, 2);
        h1.setNroHab(101L);
        Habitacion h2 = new Habitacion(TipoHab.SUITE, EstadoHab.DISPONIBLE, 200, 2);
        h2.setNroHab(102L);

        r.setListaHabitaciones(List.of(h1, h2));
        return r;
    }

    @Test
    public void buscarReservas_apellidoVacio_lanzaExcepcion() {
        assertThrows(
                ApellidoVacioException.class,
                () -> gestorHabitacion.buscarReservasPorApellidoNombre("   ", "Juan")
        );
        verifyNoInteractions(reservaDAO);
    }

    @Test
    public void buscarReservas_ok_apellidoSolo() {
        Reserva r = crearReservaConHabitaciones(8L, "Perez", "Juan");
        when(reservaDAO.buscarPorApellidoNombre("Perez", null)).thenReturn(List.of(r));

        List<ReservaGrillaDTO> out = gestorHabitacion.buscarReservasPorApellidoNombre("Perez", null);

        assertNotNull(out);
        assertEquals(1, out.size());

        ReservaGrillaDTO dto = out.get(0);
        assertEquals(8L, dto.getIdReserva());
        assertEquals("Perez", dto.getApellido());
        assertEquals("Juan", dto.getNombre());
        assertEquals(LocalDate.parse("2026-01-16"), dto.getFechaInicio());
        assertEquals(LocalDate.parse("2026-01-19"), dto.getFechaFin());
        assertNotNull(dto.getHabitaciones());
        assertEquals(2, dto.getHabitaciones().size());

        verify(reservaDAO).buscarPorApellidoNombre("Perez", null);
    }

    @Test
    public void buscarReservas_ok_apellidoYNombre() {
        Reserva r = crearReservaConHabitaciones(9L, "Perez", "Juan");
        when(reservaDAO.buscarPorApellidoNombre("Perez", "Juan")).thenReturn(List.of(r));

        List<ReservaGrillaDTO> out = gestorHabitacion.buscarReservasPorApellidoNombre("Perez", "Juan");

        assertEquals(1, out.size());
        assertEquals(9L, out.get(0).getIdReserva());

        verify(reservaDAO).buscarPorApellidoNombre("Perez", "Juan");
    }

    @Test
    public void buscarReservas_trim_enApellidoYNombre() {
        when(reservaDAO.buscarPorApellidoNombre("Perez", "Juan"))
                .thenReturn(List.of(crearReservaConHabitaciones(1L, "Perez", "Juan")));

        gestorHabitacion.buscarReservasPorApellidoNombre("  Perez  ", "  Juan  ");

        verify(reservaDAO).buscarPorApellidoNombre("Perez", "Juan");
    }

    @Test
    public void buscarReservas_daoDevuelveVacio_retornaListaVacia() {
        when(reservaDAO.buscarPorApellidoNombre("Perez", "Juan")).thenReturn(List.of());

        List<ReservaGrillaDTO> out = gestorHabitacion.buscarReservasPorApellidoNombre("Perez", "Juan");

        assertNotNull(out);
        assertTrue(out.isEmpty());
        verify(reservaDAO).buscarPorApellidoNombre("Perez", "Juan");
    }

    @Test
    public void cancelarReserva_ok_seteaCancelada_yActualizaDAO() {
        Reserva r = crearReservaConHabitaciones(8L, "Perez", "Juan");
        when(reservaDAO.buscarPorID(8L)).thenReturn(r);

        assertDoesNotThrow(() -> gestorHabitacion.cancelarReserva(8L));

        assertEquals("Cancelada", r.getEstado());
        verify(reservaDAO).actualizar(r);
    }

    @Test
    public void cancelarReserva_idNulo_lanzaExcepcion_yNoActualiza() {
        assertThrows(ReservaInexistenteException.class, () -> gestorHabitacion.cancelarReserva(null));
        verify(reservaDAO, never()).actualizar(any());
    }

    @Test
    public void cancelarReserva_inexistente_lanzaExcepcion_yNoActualiza() {
        when(reservaDAO.buscarPorID(999L)).thenReturn(null);

        assertThrows(
                ReservaInexistenteException.class,
                () -> gestorHabitacion.cancelarReserva(999L)
        );

        verify(reservaDAO, never()).actualizar(any());
    }
}
