package ddb.deso.repository;

import ddb.deso.negocio.alojamiento.AlojadoID;
import ddb.deso.negocio.alojamiento.DatosAlojado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatosAlojadoRepository extends JpaRepository<DatosAlojado, AlojadoID> {

}
