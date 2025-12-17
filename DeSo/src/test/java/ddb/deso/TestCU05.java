package ddb.deso;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DAO.DatosCheckInDAO;
import ddb.deso.almacenamiento.DAO.EstadiaDAO;
import ddb.deso.almacenamiento.DAO.HabitacionDAO;
import ddb.deso.almacenamiento.DAO.ReservaDAO;
import ddb.deso.almacenamiento.DTO.ConsultarReservasDTO;
import ddb.deso.negocio.EstadoHab;
import ddb.deso.negocio.TipoHab;
import ddb.deso.negocio.habitaciones.Estadia;
import ddb.deso.negocio.habitaciones.Habitacion;
import ddb.deso.negocio.habitaciones.Reserva;
import ddb.deso.service.GestorHabitacion;

@ExtendWith(MockitoExtension.class)
public class TestCU05 {

    @Mock private HabitacionDAO habitacionDAO;
    @Mock private EstadiaDAO estadiaDAO;
    @Mock private ReservaDAO reservaDAO;
    @Mock private AlojadoDAO alojadoDAO;
    @Mock private DatosCheckInDAO checkInDAO;

    @InjectMocks
    private GestorHabitacion gestorHabitacion;

    @Test
    void listarHabitaciones_daoDevuelveNull_retornaNull() {
        when(habitacionDAO.listar()).thenReturn(null);

        var out = gestorHabitacion.listarHabitaciones();

        assertNull(out);
    }

    @Test
    void listarHabitaciones_ok_mapeaAHabitacionDTO() {
        Habitacion h1 = new Habitacion();
        h1.setNroHab(101L);
        h1.setTipo_hab(TipoHab.DOBLEESTANDAR);
        h1.setEstado_hab(EstadoHab.DISPONIBLE);

        when(habitacionDAO.listar()).thenReturn(List.of(h1));

        var out = gestorHabitacion.listarHabitaciones();

        assertNotNull(out);
        assertEquals(1, out.size());
        assertEquals(101L, out.get(0).getNroHab());
        assertEquals(TipoHab.DOBLEESTANDAR, out.get(0).getTipo_hab());
        assertEquals(EstadoHab.DISPONIBLE, out.get(0).getEstado_hab());
    }


   @Test
    void listarReservas_porFecha_ignoraCanceladas() {
        // Reserva cancelada
        Reserva cancelada = new Reserva(
                LocalDate.parse("2026-01-10"),
                LocalDate.parse("2026-01-12"),
                "Cancelada",
                "X",
                "Y",
                "123"
        );
        Habitacion h1 = new Habitacion(TipoHab.DOBLEESTANDAR, EstadoHab.DISPONIBLE, 100, 2);
        h1.setNroHab(101L);
        cancelada.setListaHabitaciones(List.of(h1));

        // Reserva activa
        Reserva activa = new Reserva(
                LocalDate.parse("2026-01-10"),
                LocalDate.parse("2026-01-12"),
                "Reservado",
                "A",
                "B",
                "123"
        );
        Habitacion h2 = new Habitacion(TipoHab.SUITE, EstadoHab.DISPONIBLE, 200, 2);
        h2.setNroHab(102L);
        activa.setListaHabitaciones(List.of(h2));

        when(reservaDAO.listarPorFecha(any(), any())).thenReturn(List.of(cancelada, activa));

        var out = gestorHabitacion.listarReservas(
                LocalDate.parse("2026-01-01"),
                LocalDate.parse("2026-01-31")
        );

        assertEquals(1, out.size());
        assertEquals(102L, out.get(0).getIdHabitacion());
    }


    @Test
    void listarEstadias_daoDevuelveNull_retornaListaVacia() {
        when(estadiaDAO.listar()).thenReturn(null);

        var out = gestorHabitacion.listarEstadias();

        assertNotNull(out);
        assertTrue(out.isEmpty());
    }

    @Test
    void listarEstadias_porFecha_ok() {
        Habitacion h = new Habitacion();
        h.setNroHab(101L);
        h.setTipo_hab(TipoHab.DOBLEESTANDAR);
        h.setEstado_hab(EstadoHab.DISPONIBLE);

        Estadia e = new Estadia();
        e.setHabitacion(h);
        e.setFecha_inicio(LocalDate.parse("2026-01-10"));
        e.setFecha_fin(LocalDate.parse("2026-01-12"));

        when(estadiaDAO.listarPorFecha(any(), any())).thenReturn(List.of(e));

        var out = gestorHabitacion.listarEstadias(
                LocalDate.parse("2026-01-01"),
                LocalDate.parse("2026-01-31")
        );

        assertEquals(1, out.size());
        assertEquals(101L, out.get(0).getIdHabitacion());
    }


    @Test
    void consultarReservas_fechasInvertidas_devuelveVacio_yNoLlamaDAO() {
        ConsultarReservasDTO rango = new ConsultarReservasDTO();
        rango.setFechaInicio("2026-01-10");
        rango.setFechaFin("2026-01-01");
        rango.setIdHabitacion(101L);

        var out = gestorHabitacion.consultarReservas(rango);

        assertNotNull(out);
        assertTrue(out.isEmpty());
        verify(reservaDAO, never()).listarPorFecha(any(), any());
    }

    @Test
    void consultarReservas_filtraPorHabitacion() {
        ConsultarReservasDTO rango = new ConsultarReservasDTO();
        rango.setFechaInicio("2026-01-01");
        rango.setFechaFin("2026-01-31");
        rango.setIdHabitacion(101L);

        Reserva r = new Reserva(
                LocalDate.parse("2026-01-16"),
                LocalDate.parse("2026-01-19"),
                "Reservado",
                "Juan",
                "Perez",
                "123"
        );

        Habitacion h = new Habitacion(TipoHab.DOBLEESTANDAR, EstadoHab.DISPONIBLE, 100, 2);
        h.setNroHab(101L);
        r.setListaHabitaciones(List.of(h));

        when(reservaDAO.listarPorFecha(any(), any())).thenReturn(List.of(r));

        var out = gestorHabitacion.consultarReservas(rango);

        assertEquals(1, out.size());
        var dto = out.iterator().next();
        assertEquals("Perez", dto.getApellido());
        assertEquals(LocalDate.parse("2026-01-16"), dto.getFecha_inicio());
    }
}
