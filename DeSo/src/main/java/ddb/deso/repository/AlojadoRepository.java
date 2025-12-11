package ddb.deso.repository;

import ddb.deso.service.alojamiento.Alojado;
import ddb.deso.service.alojamiento.AlojadoID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlojadoRepository extends JpaRepository<Alojado, AlojadoID>,
        JpaSpecificationExecutor<Alojado> {
    @Modifying
    @Query(value = "UPDATE alojado SET tipo_alojado = 'Huesped' WHERE nro_doc = :nroDoc AND tipo_doc = :tipoDoc", nativeQuery = true)
    void promoverAHuesped(@Param("nroDoc") String nroDoc, @Param("tipoDoc") String tipoDoc);
}
