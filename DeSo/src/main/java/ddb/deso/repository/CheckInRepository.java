package ddb.deso.repository;

import ddb.deso.alojamiento.DatosCheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckInRepository extends JpaRepository<DatosCheckIn, Long>{

}
