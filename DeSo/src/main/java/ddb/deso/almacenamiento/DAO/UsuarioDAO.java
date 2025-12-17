package ddb.deso.almacenamiento.DAO;

import java.util.Optional;

import ddb.deso.negocio.login.Usuario;

/**
 * Interfaz DAO responsable del acceso a los datos de usuarios del sistema.
 * <p>
 * Define el contrato de búsqueda de usuarios por nombre, permitiendo
 * implementar distintos mecanismos de persistencia (por ejemplo, JSON o BD).
 * </p>
 */
public interface UsuarioDAO {

    /**
     * Busca un usuario por su nombre de acceso.
     *
     * @param nombre nombre del usuario a buscar.
     * @return un {@link Optional} que contiene el {@link Usuario} si existe,
     * o vacío si no se encuentra.
     */
    Optional<Usuario> buscarPorNombre(String nombre);
}
