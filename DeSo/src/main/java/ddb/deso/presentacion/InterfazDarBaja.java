package ddb.deso.presentacion;

import ddb.deso.alojamiento.CriteriosBusq;

import java.io.IOException;
import java.util.Scanner;



public class InterfazDarBaja {
    private Scanner scanner;
    public InterfazDarBaja(){
        scanner = new Scanner(System.in);
    }

    public void terminarCU(CriteriosBusq criterios) {
        System.out.println("El huesped "+
                criterios.getNombre() + " " +
                criterios.getApellido() +
                " Se dio de baja correctamente\n" +
                "Pulse cualquier tecla para continuar"
        );
        try{
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum BajaCliente{
        ELIMINAR,CANCELAR
    }

    public BajaCliente avisoBajaAlojado(CriteriosBusq mostrar){
        System.out.println("Los datos del huésped "+
                mostrar.getNombre()+" "+
                mostrar.getApellido()+" "+
                mostrar.getTipoDoc()+" "+
                mostrar.getNroDoc()+ " "+
                "serán ELIMINADOS del sistema"
        );
        System.out.println("Ingresa 1 para aceptar y 0 para cancelar");
        var inp = scanner.nextLine();
        /*
        * LOGICA DE VALIDADOR
        *   ES VACIO
        *   ES SOLO 0 O 1
        * */
        if(inp.equals("1")) {
            return BajaCliente.ELIMINAR;
        }

        return BajaCliente.CANCELAR;
    }

    public void noSePuedeDarBaja(){
        System.out.println("El " +
                "huésped no puede ser eliminado pues se " +
                "ha alojado en el Hotel en alguna " +
                "oportunidad. PRESIONE CUALQUIER " +
                "TECLA PARA CONTINUAR");
        // Entiendo que esto lee el primer byte y vuelve, hay que testearlo
        try{
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

}
