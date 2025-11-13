package ddb.deso;

import ddb.deso.login.Sesion;
import ddb.deso.presentacion.InterfazBusqueda;
import ddb.deso.presentacion.InterfazDarAlta;
import ddb.deso.presentacion.InterfazLogin;
import ddb.deso.presentacion.InterfazModificarHuesped;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class DeSo {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("Bienvenido/a al sistema de gestión Hotel Premier ©  -----------------------------------");

        InterfazLogin login = new InterfazLogin();
        while(Sesion.getUsuarioLogueado() == null){
            login.ejecutar();
        }
        esperar(500);
        limpiarTerminal();
        System.out.println("El menú de opciones se navega ingresando el número correspondiente a la opción deseada.");
        String input = null;
        do{
            Scanner scanner = new Scanner(System.in);
            System.out.println(" ==== Menu de gestion de huespedes ==== ");
            System.out.println("Ingrese alguna de las siguientes opciones:");
            System.out.println("1 - Buscar Huesped");
            System.out.println("0 - terminar ejecucion");
            input = scanner.nextLine();

            // Usamos un switch para manejar la opción
            switch (input) {
                case "1":
                    // CASO DE USO 2: BUSCAR HUESPED
                    //      Incluye ejecución de: CU09, CU10 y CU11
                    //      -> DAR DE ALTA HUESPED, MODIFICAR HUESPED QUE LLAMA A DAR BAJA
                    limpiarTerminal();
                    InterfazBusqueda UIBusqueda = new InterfazBusqueda();
                    UIBusqueda.busqueda_huesped();
                    UIBusqueda.close();
                    break;
                case "0":
                    System.out.println("¡Hasta pronto!");
                    esperar(1000);
                    scanner.close();
                    return; // Terminamos el método main y la ejecución
                default:
                    // Maneja cualquier otra entrada inválida
                    System.err.println("Opción inválida. Por favor ingrese 1, 2, 3 o 4.");
            }
            esperar(500);
            limpiarTerminal();
        }while(!input.equals("0"));


        // CASO DE USO 11: DAR DE BAJA HUESPED


    }

    public static void limpiarTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void esperar(long milisec){
        try{
            Thread.sleep(milisec);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
