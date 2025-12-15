package ddb.deso.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ddb.deso.negocio.login.Usuario;

/**
 * Repository JPA para la entidad {@link Usuario}.
 *
 * <p>Permite acceder a la persistencia de usuarios utilizando Spring Data JPA.
 * Se utiliza principalmente para el CU01 (Autenticar Usuario).</p>
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    /**
     * Busca un usuario por nombre sin distinguir mayúsculas/minúsculas.
     *
     * @param nombre nombre de usuario.
     * @return {@link Optional} con el usuario si existe, o vacío si no existe.
     */
  Optional<Usuario> findByNombreIgnoreCase(String nombre);

}