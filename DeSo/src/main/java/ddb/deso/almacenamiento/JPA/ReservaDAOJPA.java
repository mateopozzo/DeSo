package ddb.deso.almacenamiento.JPA;

import ddb.deso.almacenamiento.DAO.ReservaDAO;
import ddb.deso.service.habitaciones.Reserva;

import java.time.LocalDate;
import java.util.List;

import ddb.deso.repository.ReservaRepository;
import org.springframework.stereotype.Repository;


@Repository
public class ReservaDAOJPA implements ReservaDAO {

    private final ReservaRepository reservaRepository;

    public ReservaDAOJPA(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Override
    public void crearReserva(Reserva reserva) {
        if(reserva == null) {return;}
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
        return reservaRepository.findAll();
    }

    @Override
    public Reserva buscarPorID(Long ID) {
        if(ID == null) {return null;}
        var reserva = reservaRepository.findById(ID);
        return reserva.orElse(null);

    }

    @Override
    public List<Reserva> listarPorFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        var listaReservas = listar();
        listaReservas = listaReservas.stream().
                filter(una_res -> enRango(una_res, fechaInicio, fechaFin)).
                toList();
        return listaReservas;
    }

    boolean enRango(Reserva reserva, LocalDate fechaInicio, LocalDate fechaFin){
        LocalDate fechaResInicio = reserva.getFecha_inicio();
        LocalDate fechaResFin = reserva.getFecha_fin();
        if(fechaResInicio.isEqual(fechaInicio) || fechaResInicio.isEqual(fechaFin)){
            return true;
        }
        if(fechaResFin.isEqual(fechaInicio) || fechaResFin.isEqual(fechaFin)){
            return true;
        }
        if(fechaResInicio.isBefore(fechaFin) && fechaResInicio.isAfter(fechaInicio)){
            return true;
        }
        return fechaResFin.isBefore(fechaFin) && fechaResFin.isAfter(fechaInicio);
    }
}

