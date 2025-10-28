package ddb.deso;

import ddb.deso.almacenamiento.JSON.AlojadoDAOJSON;
import ddb.deso.alojamiento.CriteriosBusq;
import ddb.deso.alojamiento.GestorAlojamiento;
import ddb.deso.presentacion.InterfazLogin;

public class DeSo {
    public static void main(String[] args) {
        // CASO DE USO 1: LOGIN
        InterfazLogin interfazLogin=new InterfazLogin();
        interfazLogin.ejecutar();
        AlojadoDAOJSON alojadoDAOJSON=new AlojadoDAOJSON();
        GestorAlojamiento gestor_aloj=new GestorAlojamiento(alojadoDAOJSON);

        System.out.println("Bienvenido/a al sistema de gestión Hotel Premier © V.1.0.0 ----------------------------");
        System.out.println("El menú de opciones se navega ingresando el número correspondiente a la opción deseada.");

        // CASO DE USO 9: DAR DE ALTA HUESPED
        gestor_aloj.darDeAltaHuesped();

        //CASO DE USO 10: MODIFICAR HUESPED

        // CASO DE USO 2: BUSCAR HUESPED
        CriteriosBusq criterios=new CriteriosBusq();
        gestor_aloj.buscarHuesped(criterios);

        // CASO DE USO 11: DAR DE BAJA HUESPED

    }

}
