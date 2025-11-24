package ddb.deso.habitaciones;

import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DAO.ReservaDAO;
import org.springframework.stereotype.Service;

@Service
public class GestorHabitacion {
    private final ReservaDAO reservaDAO;
    public GestorHabitacion(ReservaDAO reservaDAO) {
        this.reservaDAO = reservaDAO;
    }


}
