package ddb.deso.login.presentacion;

import java.util.Scanner;

import ddb.deso.almacenamiento.JSON.UsuarioJsonDAO;
import ddb.deso.login.Usuario;
import ddb.deso.login.negocio.GestorAutenticacion;
import ddb.deso.login.negocio.excepciones.CredencialesInvalidasException;
import ddb.deso.login.negocio.excepciones.UsuarioNoEncontradoException;

/**
 * Clase que implementa la interfaz de usuario por consola
 * para realizar el proceso de inicio de sesión.
 * <p>
 * Se comunica con {@link GestorAutenticacion} para validar
 * las credenciales ingresadas.
 */
public class InterfazLogin {
    
    /**
     * Ejecuta el ciclo de inicio de sesión por consola.
     * <p>
     * Pide al usuario su nombre y contraseña, intenta autenticarlo
     * y permite reintentos en caso de error.
     */
    public void ejecutar(){
        GestorAutenticacion gestor = new GestorAutenticacion(new UsuarioJsonDAO());
        Scanner scanner = new Scanner(System.in);
        
        boolean autenticado = false;

        while (!autenticado) {
            System.out.print("Nombre de usuario: ");
            String nombre = scanner.nextLine();
            System.out.print("Contraseña: ");
            String password = scanner.nextLine();
            
            try {
                Usuario usuario = gestor.autenticar(nombre, password);
                System.out.println("¡Bienvenido, " + usuario.getNombre() + "!");
                autenticado = true;
            } catch (UsuarioNoEncontradoException e) {
                System.out.println(e.getMessage());
            } catch (CredencialesInvalidasException e) {
                System.out.println(e.getMessage());
            }
        }
        scanner.close();
    }
}
