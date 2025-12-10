package ddb.deso;

import ddb.deso.almacenamiento.DAO.HabitacionDAO;
import ddb.deso.almacenamiento.DAO.ReservaDAO;

import ddb.deso.almacenamiento.DTO.ReservaDTO;
import ddb.deso.gestores.GestorHabitacion;
import ddb.deso.gestores.excepciones.HabitacionInexistenteException;
import ddb.deso.service.habitaciones.Reserva;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Rollback(true)
public class TestCU04Integracion {

    @Autowired
    private HabitacionDAO habitacionDAO;
    @Autowired
    private ReservaDAO reservaDAO;
    @Autowired
    private GestorHabitacion gestorHabitacion;


    /**
     * Test de GestorHabitacion -> crear reserva()
     * Prueba una Reserva valida
     */
    @Test public void pruebaCrearReserva() {
        var rese = convertirReservaADTO(crearReservaValida());
        List<Long> habs = new ArrayList<>() ;
        habs.add(101L);
        habs.add(102L);
        gestorHabitacion.crearReserva(rese, habs);
    }

    @Test void pruebaCrearReservaConIDsRepetidos( ){
        var rese = convertirReservaADTO(crearReservaValida());
        List<Long> habs = new ArrayList<Long>() ;
        habs.add(101L);
        habs.add(101L);
        habs.add(102L);
        habs.add(102L);
        gestorHabitacion.crearReserva(rese, habs);
    }

    @Test void pruebaCrearReservaConHabitacionesNoExistentes(){
        var rese = convertirReservaADTO(crearReservaValida());
        List<Long> habs = new ArrayList<Long>() ;
        habs.add(1L<<62-3);
        habs.add(1L<<62-2);
        habs.add(1L<<62-1);
        habs.add(1L<<62);
        Exception e = assertThrows(HabitacionInexistenteException.class, () -> {
            gestorHabitacion.crearReserva(rese, habs);
        } );
    }

    public static Reserva crearReservaValida() {
        LocalDate fecha_inicio = LocalDate.parse("2032-11-01");
        LocalDate fecha_fin = LocalDate.parse("2032-11-05");
        String nombre, apellido, telefono, estado;
        nombre = "Juan";
        apellido = "Perez";
        telefono = "12345";
        estado = "Reservado";
        Reserva reserva = new Reserva(fecha_inicio,fecha_fin,estado,nombre,apellido,telefono);
        return reserva;
    }

    public ReservaDTO convertirReservaADTO(Reserva r){
        ReservaDTO ret = new ReservaDTO();
        ret.setApellido(r.getApellido());
        ret.setNombre(r.getNombre());
        ret.setEstado(r.getEstado());
        ret.setFecha_fin(r.getFecha_fin());
        ret.setFecha_inicio(r.getFecha_inicio());
        ret.setTelefono(r.getTelefono());
        return ret;
    }
}
