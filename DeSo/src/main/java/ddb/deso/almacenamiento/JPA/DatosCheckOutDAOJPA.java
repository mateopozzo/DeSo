package ddb.deso.almacenamiento.JPA;

import ddb.deso.almacenamiento.DAO.DatosCheckOutDAO;
import ddb.deso.negocio.TipoDoc;
import ddb.deso.negocio.alojamiento.DatosCheckOut;
import ddb.deso.repository.CheckOutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DatosCheckOutDAOJPA implements DatosCheckOutDAO {

    @Autowired
    CheckOutRepository checkOutRepository;

    /**
     * @param datosCheckOut
     */
    @Override
    public void crearDatosCheckOut(DatosCheckOut datosCheckOut) {
        checkOutRepository.save(datosCheckOut);
    }

    /**
     * @param datosCheckOut
     */
    @Override
    public void actualizarDatosCheckOut(DatosCheckOut datosCheckOut) {

    }

    /**
     * @param datosCheckOut
     */
    @Override
    public void eliminarDatosCheckOut(DatosCheckOut datosCheckOut) {

    }

    /**
     * @param documento
     * @param tipo
     * @return
     */
    @Override
    public List<DatosCheckOut> listarDatosCheckOut(String documento, TipoDoc tipo) {
        return checkOutRepository.findAll();
    }
}
