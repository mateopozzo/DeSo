package ddb.deso;

import ddb.deso.alojamiento.CriteriosBusq;
import ddb.deso.alojamiento.GestorAlojamiento;
import ddb.deso.login.presentacion.InterfazLogin;

import java.util.List;
import java.util.Scanner;

/**
 *
 * @author mat
 */
public class DeSo {
    public static void main(String[] args) {
        // CASO DE USO 1: DAR DE ALTA HUESPED
        InterfazLogin interfazLogin=new InterfazLogin();
        interfazLogin.ejecutar();

        GestorAlojamiento gestor_aloj=new GestorAlojamiento();
        System.out.println("Bienvenido/a al sistema de gestión Hotel Premier © V.1.0.0 ----------------------------");
        System.out.println("El menú de opciones se navega ingresando el número correspondiente a la opción deseada.");
        gestor_aloj.darDeAltaHuesped();

        // CASO DE USO 2: BUSCAR HUESPED
        CriteriosBusq criterios=new CriteriosBusq();
        gestor_aloj.buscarHuesped(criterios);
    }

}
