package ddb.deso.almacenamiento.JPA;

import ddb.deso.almacenamiento.DAO.ResponsablePagoDAO;
import ddb.deso.negocio.contabilidad.ResponsablePago;
import ddb.deso.repository.ResponsablePagoRepository; // Asegúrate de tener este repositorio creado
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ResponsablePagoDAOJPA implements ResponsablePagoDAO {

    @Autowired
    private ResponsablePagoRepository responsablePagoRepository;

    @Override
    public void crear(ResponsablePago responsablePago) {
        responsablePagoRepository.save(responsablePago);
    }

    @Override
    public void actualizar(ResponsablePago responsablePago) {
        responsablePagoRepository.save(responsablePago);
    }

    @Override
    public void eliminar(ResponsablePago responsablePago) {
        responsablePagoRepository.delete(responsablePago);
    }

    @Override
    public List<ResponsablePago> listar() {
        return responsablePagoRepository.findAll();
    }

    @Override
    public ResponsablePago buscarPorCUIT(String cuit) {
        // Asumiendo que el ID es el CUIT, parseamos a Long
        try {
            return read(Long.parseLong(cuit));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public ResponsablePago read(Long id) {
        return responsablePagoRepository.findById(id).orElse(null);
    }
}