package ddb.deso.almacenamiento.DAO;

import java.util.Optional;

import ddb.deso.login.Usuario;

public interface UsuarioDAO {
    Optional<Usuario> buscarPorNombre(String nombre);
}
