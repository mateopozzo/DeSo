import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.almacenamiento.JSON.AlojadoDAOJSON;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.CriteriosBusq;
import ddb.deso.presentacion.InterfazBusqueda;

import static ddb.deso.alojamiento.GestorAlojamiento.buscarHuesped;

public class TestCU02 {
    public static void main(String[] args) {
        AlojadoDAOJSON alojadoDAOJSON = new AlojadoDAOJSON();

        System.out.println("TESTING: CASO DE USO 02 - BUSCAR HUÉSPED ----------------------------");

        // CASO DE USO 2: BUSCAR HUESPED
        InterfazBusqueda ui_busq=new InterfazBusqueda();

        // ENCONTRADO - Todos los criterios -> una coincidencia
        CriteriosBusq crit1 = new CriteriosBusq("Orellano", "Guillermo", TipoDoc.DNI, "34784093");
        buscarHuesped(crit1);

        // CON TILDE EN BDD, SIN TILDE EN BÚSQUEDA - Todos los criterios
        CriteriosBusq crit2 = new CriteriosBusq("Perez", "Ana Maria", TipoDoc.DNI, "25123456");
        buscarHuesped(crit2);

        // SOLO NOMBRE - MÁS DE UNA COINCIDENCIA
        CriteriosBusq crit3 = new CriteriosBusq("Gómez","Juan", null, null);
        buscarHuesped(crit3);

        // SOLO NUM DOC - MÁS DE UNA COINCIDENCIA (porque puede ser el mismo num pero distinto tipo)
        CriteriosBusq crit4 = new CriteriosBusq(null,null, null, "30987654");
        buscarHuesped(crit4);

        // SIN COINCIDENCIAS
        CriteriosBusq crit5 = new CriteriosBusq("Suárez","Gael", TipoDoc.PASAPORTE, "AAF94042");
        buscarHuesped(crit5);
    }
}
