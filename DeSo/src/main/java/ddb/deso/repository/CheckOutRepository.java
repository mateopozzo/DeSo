package ddb.deso.repository;

import ddb.deso.negocio.alojamiento.DatosCheckOut;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckOutRepository extends JpaRepository<DatosCheckOut, Long>{

}