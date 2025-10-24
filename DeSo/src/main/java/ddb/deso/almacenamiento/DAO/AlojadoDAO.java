package ddb.deso.almacenamiento.DAO;
import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.alojamiento.CriteriosBusq;

import java.util.List;

/**
 *
 * @author mat
 */
public interface AlojadoDAO {
    void crearAlojado(AlojadoDTO alojado);
    void actualizarAlojado(AlojadoDTO alojado);
    void eliminarAlojado(AlojadoDTO alojado);
    List<AlojadoDTO> listarAlojados();
    AlojadoDTO buscarPorDNI(String documento, TipoDoc tipo);
}
