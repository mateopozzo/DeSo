package ddb.deso.habitaciones;

import ddb.deso.almacenamiento.DAO.HabitacionDAO;
import ddb.deso.almacenamiento.DAO.ReservaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GestorHabitacion {
    private final ReservaDAO reservaDAO;
    private HabitacionDAO habitacionDAO;

    @Autowired
    public GestorHabitacion(ReservaDAO reservaDAO) {
        this.reservaDAO = reservaDAO;
    }

    @Autowired
    public void setHabitacionDAO(HabitacionDAO habitacionDAO) {
        this.habitacionDAO = habitacionDAO;
    }

    public void crearReserva(Reserva reserva, List<Long> listaIDHabitaciones) {

        for(var id : listaIDHabitaciones) {
            try {
                Habitacion habitacion = habitacionDAO.buscarPorNumero(id);
                reserva.agregarHabitacion(habitacion);
            } catch (Exception e){
                e.printStackTrace(); // TODO -> hacer excepcion especifica por si no encuentra una habitacao
            }
        }

        reservaDAO.crearReserva(reserva);

        return;

    }


}
