import ddb.deso.*;

import ddb.deso.almacenamiento.JSON.AlojadoDAOJSON;
import ddb.deso.alojamiento.CriteriosBusq;
import ddb.deso.alojamiento.GestorAlojamiento;

public class TestCU02 {
    public static void main(String[] args) {
        AlojadoDAOJSON alojadoDAOJSON = new AlojadoDAOJSON();
        GestorAlojamiento gestor_aloj = new GestorAlojamiento(alojadoDAOJSON);

        System.out.println("TESTING: CASO DE USO 02 - BUSCAR HUÉSPED ----------------------------");

        // CASO DE USO 2: BUSCAR HUESPED
        CriteriosBusq criterios = new CriteriosBusq();
        gestor_aloj.buscarHuesped(criterios);

    }
}
