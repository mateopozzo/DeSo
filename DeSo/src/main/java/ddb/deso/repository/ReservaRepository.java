package ddb.deso.repository;

import ddb.deso.negocio.habitaciones.Reserva;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ddb.deso.negocio.habitaciones.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    /**
     * Busca reservas cuyo apellido contenga el texto indicado (ignorando mayúsculas/minúsculas).
     *
     * @param apellido texto a buscar dentro del apellido.
     * @return lista de reservas coincidentes.
     */
    List<Reserva> findByApellidoContainingIgnoreCase(String apellido);

    /**
     * Busca reservas cuyo apellido y nombre contengan los textos indicados
     * (ignorando mayúsculas/minúsculas).
     *
     * @param apellido texto a buscar dentro del apellido.
     * @param nombre texto a buscar dentro del nombre.
     * @return lista de reservas coincidentes.
     */
    List<Reserva> findByApellidoContainingIgnoreCaseAndNombreContainingIgnoreCase(String apellido, String nombre);
}
