package ddb.deso.almacenamiento.JPA;

import ddb.deso.almacenamiento.DAO.DatosCheckInDAO;
import ddb.deso.negocio.alojamiento.DatosCheckIn;
import ddb.deso.repository.CheckInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * Implementación JPA de {@link DatosCheckInDAO}.
 * Gestiona la persistencia de los datos asociados al proceso de Check-In.
 */
@Repository
public class DatosCheckInDAOJPA implements DatosCheckInDAO {

    private CheckInRepository checkInRepository ;

    @Autowired
    public DatosCheckInDAOJPA(CheckInRepository checkInRepository) {this.checkInRepository = checkInRepository;}
    /**
     * Guarda los datos de un Check-In.
     * @param datosCheckIn Objeto con la información del Check-In.
     */
    @Override
    public void crearDatosCheckIn(DatosCheckIn datosCheckIn) {
        checkInRepository.save(datosCheckIn);
    }

    /** No implementado */
    @Override
    public void actualizarDatosCheckIn(DatosCheckIn datosCheckInPre, DatosCheckIn datosCheckIn) {

    }
    /** No implementado */
    @Override
    public void eliminarDatosCheckIn(DatosCheckIn datosCheckIn) {
    }
    /** No implementado */
    @Override
    public List<DatosCheckIn> listarDatosCheckIn() {
        return checkInRepository.findAll();
    }
}
