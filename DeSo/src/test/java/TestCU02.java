import ddb.deso.TipoDoc;
import ddb.deso.alojamiento.CriteriosBusq;

import static ddb.deso.alojamiento.GestorAlojamiento.buscarHuesped;

public class TestCU02 {
    public static void main(String[] args) {
        System.out.println("TESTING: CASO DE USO 02 - BUSCAR HUÉSPED ----------------------------");

        // CASO DE USO 2: BUSCAR HUESPED
        System.out.println("CON TILDE EN BDD, SIN TILDE EN BÚSQUEDA - Todos los criterios ------");
        System.out.println("BÚSQUEDA: Rodriguez, Maria DNI 25123456 ----------------------------\n");
        CriteriosBusq crit2 = new CriteriosBusq("Gomez", "Juan", TipoDoc.LE, "47183532");
         buscarHuesped(crit2);

        System.out.println("CON TILDE EN BUSQUEDA, SIN TILDE EN BDD - Todos los criterios ------");
        System.out.println("BÚSQUEDA: Grís, Gatito Panzón DNI 25123456 ----------------------------\n");
        CriteriosBusq crit5 = new CriteriosBusq("Fernandez", "Domingo", TipoDoc.DNI, "87800466");
        buscarHuesped(crit5);

        System.out.println("SOLO NOMBRE - MÁS DE UNA COINCIDENCIA ------------------------------");
        System.out.println("BÚSQUEDA: Negro, Gatito ----------------------------------------------\n");
        CriteriosBusq crit3 = new CriteriosBusq("Gomez","Juan", null, null);
        buscarHuesped(crit3);

        System.out.println("SOLO NUM DOC - MÁS DE UNA COINCIDENCIA ------------------------------");
        System.out.println("BÚSQUEDA: 12345678 --------------------------------------------------\n");
        CriteriosBusq crit4 = new CriteriosBusq(null,null, null, "45510538");
        buscarHuesped(crit4);

        System.out.println("SIN COINCIDENCIAS ---------------------------------------------------");
        System.out.println("BÚSQUEDA: Naranja y peludito, Un michito PASAPORTE AAF94042 ---------\n");
        CriteriosBusq crit1 = new CriteriosBusq("Perez","Carlos", TipoDoc.LC, "59404690");
        buscarHuesped(crit1);

        System.out.println("ENCUENTRA TODOS ---------------------------------------------------");
        System.out.println("BÚSQUEDA: Todos los campos null ---------\n");
        CriteriosBusq crit7 = new CriteriosBusq(null,null, null, null);
        buscarHuesped(crit7);
    }
}
