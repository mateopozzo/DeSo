package ddb.deso.almacenamiento.JPA;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import ddb.deso.almacenamiento.DAO.UsuarioDAO;
import ddb.deso.negocio.login.Usuario;
import ddb.deso.repository.UsuarioRepository;

/**
 * Implementación JPA de {@link UsuarioDAO}.
 *
 * <p>Encargada de acceder a los usuarios persistidos en PostgreSQL mediante
 * {@link UsuarioRepository}. Se utiliza para el CU01 (Autenticar Usuario).</p>
 */
@Repository
public class UsuarioDAOJPA implements UsuarioDAO {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDAOJPA(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Busca un usuario por nombre sin distinguir mayúsculas/minúsculas.
     *
     * @param nombre nombre de usuario.
     * @return {@link Optional} con el usuario si existe, o vacío si no existe.
     */
    @Override
    public Optional<Usuario> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) return Optional.empty();
        return usuarioRepository.findByNombreIgnoreCase(nombre.trim());
    }
}
