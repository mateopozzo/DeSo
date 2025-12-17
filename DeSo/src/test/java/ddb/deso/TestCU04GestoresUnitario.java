package ddb.deso;

import ddb.deso.almacenamiento.DAO.*;
import ddb.deso.almacenamiento.DTO.ReservaDTO;
import ddb.deso.service.GestorHabitacion;
import ddb.deso.service.excepciones.HabitacionInexistenteException;
import ddb.deso.service.excepciones.ReservaInvalidaException;
import ddb.deso.negocio.EstadoHab;
import ddb.deso.negocio.TipoHab;
import ddb.deso.negocio.habitaciones.Estadia;
import ddb.deso.negocio.habitaciones.Habitacion;
import ddb.deso.negocio.habitaciones.Reserva;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


/**
 * Pruebas unitarias para la lógica de negocio en {@link GestorHabitacion}.
 * <p>
 * Se enfoca en la validación del método de creación de reservas. Mockea los DAOs de habitación,
 * estadía y reserva para verificar:
 * <ul>
 * <li>Validación de integridad de datos de entrada (reservas nulas o habitaciones inexistentes).</li>
 * <li>Detección de conflictos de disponibilidad por solapamiento de fechas con {@link Estadia} existentes.</li>
 * <li>Detección de conflictos de disponibilidad con otras {@link Reserva} ya registradas.</li>
 * <li>Flujo exitoso cuando no existen conflictos temporales.</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
public class TestCU04GestoresUnitario {

    @Mock
    private HabitacionDAO habitacionDAO;
    @Mock
    private EstadiaDAO estadiaDAO;
    @Mock
    private ReservaDAO reservaDAO;
    @Mock
    private AlojadoDAO alojadoDAO;
    @Mock
    private DatosCheckInDAO checkInDAO;

    @InjectMocks
    private GestorHabitacion gestorHabitacion;

    private List<Estadia> crearEstadiaConflictiva(){
        var lh = crearListaHabitaciones();
        Estadia estadia = new Estadia();
        estadia.setFecha_fin(LocalDate.now());
        estadia.setFecha_inicio(LocalDate.now().minusDays(1));
        estadia.setHabitacion(lh.getFirst());
        return List.of(estadia);
    }

    private List<Habitacion> crearListaHabitaciones() {
        Habitacion h1 = new Habitacion(TipoHab.DOBLEESTANDAR, EstadoHab.DISPONIBLE,1,2), h2 = new Habitacion(TipoHab.DOBLEESTANDAR,EstadoHab.DISPONIBLE,1,2);
        h1.setNroHab(101L);
        h2.setNroHab(202L);
        return List.of(h1,h2);
    }

    private List<Long> crearListaIdHabitaciones(){
        var l = crearListaHabitaciones();
        List<Long> x = new ArrayList<>();
        for(var y : l){ // buenos nombres
            x.add(y.getNroHab());
        }
        return x;
    }

    private Reserva crearReservaValida(){
        Reserva reserva = new Reserva();
        reserva.setApellido("Reserva");
        reserva.setNombre("Reserva");
        reserva.setEstado("Reservado");
        reserva.setFecha_fin(LocalDate.now());
        reserva.setFecha_inicio(LocalDate.parse("2025-12-01"));
        reserva.setTelefono("123123123");
        reserva.setListaHabitaciones(crearListaHabitaciones());
        return reserva;
    }

    private ReservaDTO convertirReservaADTO(Reserva r){
        ReservaDTO ret = new ReservaDTO();
        ret.setApellido(r.getApellido());
        ret.setNombre(r.getNombre());
        ret.setEstado(r.getEstado());
        ret.setFecha_fin(r.getFecha_fin());
        ret.setFecha_inicio(r.getFecha_inicio());
        ret.setTelefono(r.getTelefono());
        return ret;

    }

    private List<Reserva> crearReservaConflictiva(){
        var r1 = crearReservaValida();
        var r2 = crearReservaValida();
        r1.setFecha_inicio(LocalDate.now().minusDays(1));
        r2.setFecha_inicio(LocalDate.now().minusDays(1));
        return List.of(r1,r2);
    }

    private List<Reserva> crearReservasNoConflictivas(){
        var lr = crearReservaConflictiva();
        for(var r : lr){
            r.setFecha_fin(LocalDate.parse("2025-11-11"));
            r.setFecha_inicio(LocalDate.parse("2025-11-10"));
        }
        return lr;
    }

    private List<Estadia> crearEstadiaNoConflictiva(){
        var listae = crearEstadiaConflictiva();
        var estadia = listae.getFirst();
        estadia.setFecha_inicio(LocalDate.parse("2025-11-10"));
        estadia.setFecha_fin(LocalDate.parse("2025-11-11"));
        return listae;
    }

    @Test
    public void reservaNula(){
        assertThrows(
                ReservaInvalidaException.class,
                ()->gestorHabitacion.crearReserva(null, crearListaIdHabitaciones())
        );
    }

    @Test
    public void reservaValida(){
        Habitacion hab = new Habitacion();
        hab.setCapacidad(1);
        hab.setTipo_hab(TipoHab.DOBLEESTANDAR);
        hab.setEstado_hab(EstadoHab.DISPONIBLE);
        hab.setTarifa(234);
        hab.setNroHab(101L);
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(hab);
        var r = crearReservaValida();
        assertDoesNotThrow(
                ()->gestorHabitacion.crearReserva(convertirReservaADTO(r), List.of(101L))
        );
    }

    @Test
    public void habitacionesInexsitentes(){
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(null);
        assertThrows(
                HabitacionInexistenteException.class,
                ()->gestorHabitacion.crearReserva(
                        convertirReservaADTO(crearReservaValida()),
                        crearListaIdHabitaciones())
        );
    }

    @Test
    public void existeEstadiaConflictiva(){
        var lh = crearListaHabitaciones();
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(lh.getFirst());
        when(estadiaDAO.listarPorFecha(any(LocalDate.class), any(LocalDate.class))).thenReturn(crearEstadiaConflictiva());
        assertThrows(
                ReservaInvalidaException.class,
                ()->gestorHabitacion.crearReserva(
                        convertirReservaADTO(crearReservaValida()),
                        crearListaIdHabitaciones())
        );
    }

    @Test
    public void existeReservaConflictiva(){
        var lh = crearListaHabitaciones();
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(lh.getFirst());
        when(habitacionDAO.buscarPorNumero(202L)).thenReturn(lh.getLast());
        when(reservaDAO.listarPorFecha(any(LocalDate.class), any(LocalDate.class))).thenReturn(crearReservaConflictiva());
        assertThrows(
                ReservaInvalidaException.class,
                ()->gestorHabitacion.crearReserva(
                        convertirReservaADTO(crearReservaValida()),
                        crearListaIdHabitaciones())
        );
    }

    @Test
    public void existeReservasEstadiaConflictiva(){
        var lh = crearListaHabitaciones();
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(lh.getFirst());
        when(habitacionDAO.buscarPorNumero(202L)).thenReturn(lh.getLast());
        when(estadiaDAO.listarPorFecha(any(LocalDate.class), any(LocalDate.class))).thenReturn(crearEstadiaConflictiva());
        when(reservaDAO.listarPorFecha(any(LocalDate.class), any(LocalDate.class))).thenReturn(crearReservaConflictiva());
        assertThrows(
                ReservaInvalidaException.class,
                ()->gestorHabitacion.crearReserva(
                        convertirReservaADTO(crearReservaValida()), crearListaIdHabitaciones())
        );
    }

    @Test
    public void existenReservasEstadiasNoConflictivas(){
        var lh = crearListaHabitaciones();
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(lh.get(0));
        when(habitacionDAO.buscarPorNumero(202L)).thenReturn(lh.get(1));
        when(estadiaDAO.listarPorFecha(any(LocalDate.class), any(LocalDate.class))).thenReturn(null);
        assertDoesNotThrow(
                ()->gestorHabitacion.crearReserva(
                        convertirReservaADTO(crearReservaValida()), crearListaIdHabitaciones())
        );
    }
}
