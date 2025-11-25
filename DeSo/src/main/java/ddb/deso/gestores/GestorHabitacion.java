package ddb.deso.gestores;

import ddb.deso.almacenamiento.DAO.HabitacionDAO;
import ddb.deso.almacenamiento.DAO.ReservaDAO;
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
    public GestorHabitacion(ReservaDAO reservaDAO) {
        this.reservaDAO = reservaDAO;
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
                throw new NullPointerException("Habitacion no encontrada");
            }
        }

        reservaDAO.crearReserva(reserva);

        return;

    }


}
