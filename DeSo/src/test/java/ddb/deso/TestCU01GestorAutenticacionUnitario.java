package ddb.deso;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import ddb.deso.almacenamiento.DAO.UsuarioDAO;
import ddb.deso.negocio.login.Usuario;
import ddb.deso.negocio.login.excepciones.CredencialesInvalidasException;
import ddb.deso.negocio.login.excepciones.UsuarioNoEncontradoException;
import ddb.deso.service.GestorAutenticacion;

@ExtendWith(MockitoExtension.class)
class TestCU01GestorAutenticacionUnitario {

    @Mock
    private UsuarioDAO usuarioDAO;

    @InjectMocks
    private GestorAutenticacion gestorAutenticacion;

    @Test
    void autenticar_ok_trimNombre_y_passwordCorrecta() throws Exception {
        // Arrange
        Usuario usuario = new Usuario("admin", "1234", 1);
        when(usuarioDAO.buscarPorNombre("admin"))
                .thenReturn(Optional.of(usuario));

        // Act
        Usuario resultado = gestorAutenticacion.autenticar("  admin  ", "1234");

        // Assert
        assertSame(usuario, resultado);
        verify(usuarioDAO).buscarPorNombre("admin");
    }

    @Test
    void autenticar_usuarioNoExiste_lanzaUsuarioNoEncontrado() {
        // Arrange
        when(usuarioDAO.buscarPorNombre("juan"))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                UsuarioNoEncontradoException.class,
                () -> gestorAutenticacion.autenticar("juan", "cualquiera")
        );

        verify(usuarioDAO).buscarPorNombre("juan");
    }

    @Test
    void autenticar_passwordIncorrecta_lanzaCredencialesInvalidas() {
        // Arrange
        Usuario usuario = new Usuario("ana", "correcta", 1);
        when(usuarioDAO.buscarPorNombre("ana"))
                .thenReturn(Optional.of(usuario));

        // Act + Assert
        assertThrows(
                CredencialesInvalidasException.class,
                () -> gestorAutenticacion.autenticar("ana", "incorrecta")
        );

        verify(usuarioDAO).buscarPorNombre("ana");
    }

    @Test
    void autenticar_nombreNull_y_passwordNull() {
        // Arrange
        when(usuarioDAO.buscarPorNombre(""))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                UsuarioNoEncontradoException.class,
                () -> gestorAutenticacion.autenticar(null, null)
        );

        verify(usuarioDAO).buscarPorNombre("");
    }
}
