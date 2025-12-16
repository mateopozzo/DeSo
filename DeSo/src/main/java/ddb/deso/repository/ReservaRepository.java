package ddb.deso.repository;

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

    /**
     * Busca reservas cuyo apellido empiece con el texto indicado (ignorando mayúsculas/minúsculas).
     *
     * @param apellido texto inicial a buscar en el apellido.
     * @return lista de reservas coincidentes.
     */
    List<Reserva> findByApellidoStartingWithIgnoreCase(String apellido);

    /**
     * Busca reservas cuyo apellido y nombre empiecen con los textos indicados
     * (ignorando mayúsculas/minúsculas).
     *
     * @param apellido texto inicial a buscar en el apellido.
     * @param nombre texto inicial a buscar en el nombre.
     * @return lista de reservas coincidentes.
     */
    List<Reserva> findByApellidoStartingWithIgnoreCaseAndNombreStartingWithIgnoreCase(String apellido, String nombre);
}
