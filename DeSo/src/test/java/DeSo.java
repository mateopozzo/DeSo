
import ddb.deso.alojamiento.GestorAlojamiento;
import ddb.deso.alojamiento.Huesped;



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

/**
 *
 * @author mat
 */
public class DeSo {
    public static void main(String[] args) {
        GeneradorDatosAleatorios generadorTest = new GeneradorDatosAleatorios();
       
        Huesped huespedTest = new Huesped(generadorTest.generarDatosAlojadoAleatorio());

        // guardar el huesped en el archivo JSON usando GestorAlojamiento.java y su metodo dar alta huesped
        GestorAlojamiento ga = new GestorAlojamiento();
        ga.darDeAltaHuesped(huespedTest);


    }
    
}
