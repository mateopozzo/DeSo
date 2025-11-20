package ddb.deso.repository;

import ddb.deso.alojamiento.DatosCheckOut;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckOutRepository extends JpaRepository<DatosCheckOut, Long>{

}