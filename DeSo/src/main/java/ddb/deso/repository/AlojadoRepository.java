package ddb.deso.repository;

import ddb.deso.TipoDoc;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.AlojadoID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlojadoRepository extends JpaRepository<Alojado, AlojadoID>,
        JpaSpecificationExecutor<Alojado> {
    // spring data jpa hace la consulta sql en vez de ir a la bdd a pata
        Optional<Alojado> findByDatos_DatosPersonales_TipoDocAndDatos_DatosPersonales_NroDoc(TipoDoc tipoDoc, String nroDoc);
        Optional<Alojado> findById(AlojadoID id);
}
