package ddb.deso.almacenamiento.JPA;

import ddb.deso.almacenamiento.DAO.DatosCheckInDAO;
import ddb.deso.service.alojamiento.DatosCheckIn;
import ddb.deso.repository.CheckInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DatosCheckInDAOJPA implements DatosCheckInDAO {

    private CheckInRepository checkInRepository ;

    @Autowired
    public DatosCheckInDAOJPA(CheckInRepository checkInRepository) {this.checkInRepository = checkInRepository;}

    @Override
    public void crearDatosCheckIn(DatosCheckIn datosCheckIn) {
        checkInRepository.save(datosCheckIn);
    }

    @Override
    public void actualizarDatosCheckIn(DatosCheckIn datosCheckInPre, DatosCheckIn datosCheckIn) {

    }

    @Override
    public void eliminarDatosCheckIn(DatosCheckIn datosCheckIn) {
    }

    @Override
    public List<DatosCheckIn> listarDatosCheckIn() {
        return List.of();
    }
}
