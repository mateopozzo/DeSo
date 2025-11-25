package ddb.deso.gestores;

import ddb.deso.almacenamiento.DAO.HabitacionDAO;
import ddb.deso.almacenamiento.DAO.ReservaDAO;
import ddb.deso.gestores.excepciones.HabitacionInexistenteException;
import ddb.deso.habitaciones.Habitacion;
import ddb.deso.habitaciones.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GestorHabitacion {

    private ReservaDAO reservaDAO;
    private HabitacionDAO habitacionDAO;

    @Autowired
    public GestorHabitacion(ReservaDAO reservaDAO, HabitacionDAO habitacionDAO) {
        this.reservaDAO = reservaDAO;
        this.habitacionDAO = habitacionDAO;
    }

    @Autowired
    public void setHabitacionDAO(HabitacionDAO habitacionDAO) {
        this.habitacionDAO = habitacionDAO;
    }

    @Autowired
    public void setReservaDAO(ReservaDAO reservaDAO) {this.reservaDAO = reservaDAO;}

    public void crearReserva(Reserva reserva, List<Long> listaIDHabitaciones) {

        for(var id : listaIDHabitaciones) {
            Habitacion habitacion = habitacionDAO.buscarPorNumero(id);
            if(habitacion != null) {
                reserva.agregarHabitacion(habitacion);
            } else {
                throw new HabitacionInexistenteException("Habitacion " + id + " no encontrada");
            }
        }

        reservaDAO.crearReserva(reserva);

        return;

    }


}
