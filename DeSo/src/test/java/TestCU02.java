import ddb.deso.almacenamiento.JSON.AlojadoDAOJSON;
import ddb.deso.presentacion.InterfazBusqueda;

public class TestCU02 {
    public static void main(String[] args) {
        AlojadoDAOJSON alojadoDAOJSON = new AlojadoDAOJSON();

        System.out.println("TESTING: CASO DE USO 02 - BUSCAR HUÉSPED ----------------------------");

         // CASO DE USO 2: BUSCAR HUESPED
        InterfazBusqueda ui_busq=new InterfazBusqueda();
        ui_busq.busqueda_huesped();

    }
}
