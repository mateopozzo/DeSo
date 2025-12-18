package ddb.deso.repository;

import ddb.deso.negocio.contabilidad.ResponsablePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponsablePagoRepository extends JpaRepository<ResponsablePago, Long> {
    ResponsablePago findByCuit(Long cuit);
}