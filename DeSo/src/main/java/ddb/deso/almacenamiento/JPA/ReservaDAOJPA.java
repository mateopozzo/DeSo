package ddb.deso.almacenamiento.JPA;

import ddb.deso.almacenamiento.DAO.ReservaDAO;
import ddb.deso.alojamiento.Huesped;
import ddb.deso.habitaciones.Estadia;
import ddb.deso.habitaciones.Reserva;
import ddb.deso.repository.AlojadoRepository;

import java.time.LocalDate;
import java.util.List;
import ddb.deso.repository.ReservaRepository;

public class ReservaDAOJPA implements ReservaDAO {

    private final ReservaRepository reservaRepository;

    public ReservaDAOJPA(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Override
    public void crearReserva(Reserva reserva) {
        if(reserva != null) {return;}
        reservaRepository.save(reserva);
    }

    @Override
    public void actualizar(Reserva reserva) {

    }

    @Override
    public void eliminar(Reserva reserva) {

    }

    @Override
    public List<Reserva> listar() {
        return List.of();
    }

    @Override
    public List<Reserva> listarPorFecha(LocalDate fecha) {
        return List.of();
    }

}

