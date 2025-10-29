import ddb.deso.almacenamiento.JSON.AlojadoDAOJSON;
import ddb.deso.alojamiento.CriteriosBusq;
import ddb.deso.presentacion.InterfazBusqueda;

import static ddb.deso.alojamiento.GestorAlojamiento.buscarHuesped;

public class TestCU02 {
    public static void main(String[] args) {
        AlojadoDAOJSON alojadoDAOJSON = new AlojadoDAOJSON();

        System.out.println("TESTING: CASO DE USO 02 - BUSCAR HUÉSPED ----------------------------");

         // CASO DE USO 2: BUSCAR HUESPED
        InterfazBusqueda ui_busq=new InterfazBusqueda();

        CriteriosBusq crit1 = new CriteriosBusq(nombre, apellido, tipoDoc, num_documento);
        buscarHuesped(crit1);

        CriteriosBusq crit2 = new CriteriosBusq(nombre, apellido, tipoDoc, num_documento);
        buscarHuesped(crit2);

        CriteriosBusq crit3 = new CriteriosBusq(nombre, apellido, tipoDoc, num_documento);
        buscarHuesped(crit3);

        CriteriosBusq crit4 = new CriteriosBusq(nombre, apellido, tipoDoc, num_documento);
        buscarHuesped(crit4);

        CriteriosBusq crit5 = new CriteriosBusq(nombre, apellido, tipoDoc, num_documento);
        buscarHuesped(crit5);
    }
}
