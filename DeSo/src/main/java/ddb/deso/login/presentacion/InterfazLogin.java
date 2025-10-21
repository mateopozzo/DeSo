package ddb.deso.login.presentacion;

import java.util.Scanner;

import ddb.deso.login.Usuario;
import ddb.deso.login.dao.UsuarioJsonDAO;
import ddb.deso.login.negocio.GestorAutenticacion;
import ddb.deso.login.negocio.excepciones.CredencialesInvalidasException;
import ddb.deso.login.negocio.excepciones.UsuarioNoEncontradoException;

public class InterfazLogin {
    
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
