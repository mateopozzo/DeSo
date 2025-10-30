import ddb.deso.TipoDoc;
import ddb.deso.alojamiento.CriteriosBusq;

import static ddb.deso.alojamiento.GestorAlojamiento.buscarHuesped;

public class TestCU02 {
    public static void main(String[] args) {
        System.out.println("TESTING: CASO DE USO 02 - BUSCAR HUÉSPED ----------------------------");

        // CASO DE USO 2: BUSCAR HUESPED
        System.out.println("Todos los criterios -> una coincidencia ----------------------------");
        System.out.println("BÚSQUEDA: Orellano, Guillermo DNI 34784093 -------------------------");
        CriteriosBusq crit1 = new CriteriosBusq("Orellano", "Guillermo", TipoDoc.DNI, "34784093");
        buscarHuesped(crit1);

        System.out.println("CON TILDE EN BDD, SIN TILDE EN BÚSQUEDA - Todos los criterios ------");
        System.out.println("BÚSQUEDA: Perez, Ana Maria DNI 25123456 ----------------------------");
        CriteriosBusq crit2 = new CriteriosBusq("Perez", "Ana Maria", TipoDoc.DNI, "25123456");
        buscarHuesped(crit2);

        System.out.println("SOLO NOMBRE - MÁS DE UNA COINCIDENCIA ------------------------------");
        System.out.println("BÚSQUEDA: Gómez, Juan ----------------------------------------------");
        CriteriosBusq crit3 = new CriteriosBusq("Gómez","Juan", null, null);
        buscarHuesped(crit3);

        System.out.println("SOLO NUM DOC - MÁS DE UNA COINCIDENCIA ------------------------------");
        System.out.println("BÚSQUEDA: 30987654 --------------------------------------------------");
        CriteriosBusq crit4 = new CriteriosBusq(null,null, null, "30987654");
        buscarHuesped(crit4);

        System.out.println("SIN COINCIDENCIAS ---------------------------------------------------");
        System.out.println("BÚSQUEDA: Naranja y peludito, Un michito PASAPORTE AAF94042 ---------");
        CriteriosBusq crit5 = new CriteriosBusq("Naranja y peludito","Un michito", TipoDoc.PASAPORTE, "AAF94042");
        buscarHuesped(crit5);
    }
}
