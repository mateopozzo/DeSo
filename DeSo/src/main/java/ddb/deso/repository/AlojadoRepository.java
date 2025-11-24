package ddb.deso.repository;

import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.AlojadoID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AlojadoRepository extends JpaRepository<Alojado, AlojadoID>,
        JpaSpecificationExecutor<Alojado> {

}
