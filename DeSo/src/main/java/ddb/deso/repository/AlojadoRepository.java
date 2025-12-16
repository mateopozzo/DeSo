package ddb.deso.repository;

import ddb.deso.negocio.alojamiento.Alojado;
import ddb.deso.negocio.alojamiento.AlojadoID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlojadoRepository extends JpaRepository<Alojado, AlojadoID>,
        JpaSpecificationExecutor<Alojado> {


    // instruccion para que la base de datos cambie el atributo de invitado a huesped
    @Modifying
    @Query(value = "UPDATE alojado SET tipo_alojado = 'Huesped' WHERE nro_doc = :nroDoc AND tipo_doc = :tipoDoc", nativeQuery = true)
    void promoverAHuesped(@Param("nroDoc") String nroDoc, @Param("tipoDoc") String tipoDoc);

    // busqueda de alojados que ocuparon la estadia
    @Query("SELECT DISTINCT a FROM Alojado a JOIN a.listaEstadias e WHERE e.idEstadia = :idEstadia")
    List<Alojado> findAlojadosByEstadiaId(@Param("idEstadia") long idEstadia);
}
