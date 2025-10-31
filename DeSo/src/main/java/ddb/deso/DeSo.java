package ddb.deso;

import ddb.deso.presentacion.InterfazBusqueda;
import ddb.deso.presentacion.InterfazDarAlta;
import ddb.deso.presentacion.InterfazLogin;

public class DeSo {
    public static void main(String[] args) {
        // CASO DE USO 1: LOGIN
        InterfazLogin interfazLogin=new InterfazLogin();
        interfazLogin.ejecutar();

        System.out.println("Bienvenido/a al sistema de gestión Hotel Premier ©  -----------------------------------");
        System.out.println("El menú de opciones se navega ingresando el número correspondiente a la opción deseada.");

        // CASO DE USO 2: BUSCAR HUESPED
        // Incluye ejecución de: CU09, CU10 -> DAR DE ALTA HUESPED, MODIFICAR HUESPED
        InterfazBusqueda ui_busq = new InterfazBusqueda();
        ui_busq.busqueda_huesped();
        ui_busq.close();

        // CASO DE USO 11: DAR DE BAJA HUESPED


  }

}
