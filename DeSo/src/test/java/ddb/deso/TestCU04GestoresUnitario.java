package ddb.deso;

import ddb.deso.almacenamiento.DAO.EstadiaDAO;
import ddb.deso.almacenamiento.DAO.HabitacionDAO;
import ddb.deso.almacenamiento.DAO.ReservaDAO;
import ddb.deso.gestores.GestorHabitacion;
import ddb.deso.gestores.excepciones.HabitacionInexistenteException;
import ddb.deso.gestores.excepciones.ReservaInvalidaException;
import ddb.deso.habitaciones.Estadia;
import ddb.deso.habitaciones.Habitacion;
import ddb.deso.habitaciones.Reserva;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TestCU04GestoresUnitario {

    @MockitoBean
    private HabitacionDAO habitacionDAO;
    @MockitoBean
    private EstadiaDAO estadiaDAO;
    @MockitoBean
    private ReservaDAO reservaDAO;

    @Autowired
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
        Habitacion h1 = new Habitacion(TipoHab.DOBLEESTANDAR,EstadoHab.DISPONIBLE,1,2), h2 = new Habitacion(TipoHab.DOBLEESTANDAR,EstadoHab.DISPONIBLE,1,2);
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
        var r = crearReservaValida();
        assertDoesNotThrow(
                ()->gestorHabitacion.crearReserva(r, crearListaIdHabitaciones())
        );
    }

    @Test
    public void habitacionesInexsitentes(){
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(null);
        when(habitacionDAO.buscarPorNumero(202L)).thenReturn(null);
        assertThrows(
                ReservaInvalidaException.class,
                ()->gestorHabitacion.crearReserva(crearReservaValida(), crearListaIdHabitaciones())
        );
    }

    @Test
    public void existeEstadiaConflictiva(){
        var lh = crearListaHabitaciones();
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(lh.getFirst());
        when(habitacionDAO.buscarPorNumero(202L)).thenReturn(lh.getLast());
        when(estadiaDAO.listarPorFecha(any(LocalDate.class), any(LocalDate.class))).thenReturn(crearEstadiaConflictiva());
        assertThrows(
                ReservaInvalidaException.class,
                ()->gestorHabitacion.crearReserva(crearReservaValida(), crearListaIdHabitaciones())
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
                ()->gestorHabitacion.crearReserva(crearReservaValida(), crearListaIdHabitaciones())
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
                ()->gestorHabitacion.crearReserva(crearReservaValida(), crearListaIdHabitaciones())
        );
    }

    @Test
    public void existenReservasEstadiasNoConflictivas(){
        var lh = crearListaHabitaciones();
        when(habitacionDAO.buscarPorNumero(101L)).thenReturn(lh.getFirst());
        when(habitacionDAO.buscarPorNumero(202L)).thenReturn(lh.getLast());
        when(estadiaDAO.listarPorFecha(any(LocalDate.class), any(LocalDate.class))).thenReturn(crearEstadiaNoConflictiva());
        when(reservaDAO.listarPorFecha(any(LocalDate.class), any(LocalDate.class))).thenReturn(crearReservasNoConflictivas());
        assertThrows(
                ReservaInvalidaException.class,
                ()->gestorHabitacion.crearReserva(crearReservaValida(), crearListaIdHabitaciones())
        );
    }


}
