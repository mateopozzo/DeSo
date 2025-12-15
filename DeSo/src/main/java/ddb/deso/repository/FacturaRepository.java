package ddb.deso.repository;

import ddb.deso.negocio.contabilidad.Factura;
import ddb.deso.negocio.TipoFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    
    // Método para buscar por número y tipo 
    @Query("SELECT f FROM Factura f WHERE f.num_factura = :num AND f.tipo_factura = :tipo")
    Optional<Factura> buscarPorNumeroYTipo(@Param("num") int numFactura, @Param("tipo") TipoFactura tipo);
}