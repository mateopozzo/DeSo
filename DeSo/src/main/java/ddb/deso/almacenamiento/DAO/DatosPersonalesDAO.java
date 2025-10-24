package ddb.deso.almacenamiento.DAO;

import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.alojamiento.CriteriosBusq;

import java.util.List;

public interface DatosPersonalesDAO {
    List<AlojadoDTO> buscarAlojados(CriteriosBusq criterios);

}
