package ddb.deso.presentacion;

import ddb.deso.almacenamiento.JSON.UsuarioJsonDAO;
import ddb.deso.login.Usuario;
import ddb.deso.login.negocio.GestorAutenticacion;
import ddb.deso.login.negocio.excepciones.CredencialesInvalidasException;
import ddb.deso.login.negocio.excepciones.UsuarioNoEncontradoException;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

/**
 * Clase que representa la interfaz de usuario por consola
 * para el caso de uso CU-01: "Validar usuario".
 * 
 * <p>Permite ingresar nombre de usuario y contraseña desde consola,
 * mostrando un asterisco ('*') por cada carácter introducido en la contraseña.
 * Utiliza la librería JLine para gestionar la entrada de texto en modo interactivo.</p>
 * 
 * <p>En esta primera etapa del sistema, la autenticación se realiza por consola,
 * sin interfaz gráfica, interactuando con la capa de negocio
 * a través del {@link GestorAutenticacion}.</p>
 */
public class InterfazLogin {    
    /**
     * Ejecuta la interfaz de login por consola.
     * 
     * <p>Solicita al usuario su nombre y contraseña, mostrando un asterisco ('*')
     * por cada carácter que se ingresa. Luego invoca al {@link GestorAutenticacion}
     * para validar las credenciales contra el origen de datos.</p>
     * 
     * <p>Si la autenticación es exitosa, muestra un mensaje de bienvenida;
     * de lo contrario, informa el error correspondiente.</p>
     */
    public void ejecutar() {
        // Capa de negocio responsable de autenticar usuarios
        GestorAutenticacion gestor = new GestorAutenticacion(new UsuarioJsonDAO());

        boolean autenticado = false;

         // Inicializa una terminal interactiva del sistema
        try (Terminal terminal = TerminalBuilder.builder().system(true).build()) {

            // Crea un lector de líneas para capturar entrada del usuario
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
            
            while (!autenticado) {
                String nombre = reader.readLine("Nombre de usuario: ");
                String password = reader.readLine("Contraseña: ", '*'); // muestra * por cada tecla

                try {
                    // Intenta autenticar al usuario
                    Usuario usuario = gestor.autenticar(nombre, password);
                    System.out.println("¡Bienvenido, " + usuario.getNombre() + "!");
                    autenticado = true;
                } catch (UsuarioNoEncontradoException | CredencialesInvalidasException e) {
                    // Muestra el mensaje de error definido en la capa de negocio
                    System.out.println(e.getMessage());
                    System.out.println(); // línea en blanco para legibilidad
                }
            }
        } catch (Exception e) {
            // Si algo falla con la TTY, informamos claramente
            System.err.println("No se pudo inicializar la terminal interactiva: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
