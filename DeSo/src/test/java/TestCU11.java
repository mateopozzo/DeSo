import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.Huesped;

import java.util.List;

public class TestCU11 {

    /*
    * Para probarlo necesito
    * 1-poblar alojados
    * 2-darles algunos checkin
    * 3-eliminarlos
    * todo esto va a estar hardcodeado para probar especificamente cu11 hasta que el resto de fun
    *  cionalidades necesarias estén implementadas como pretende el enunciado
    * */

    public TestCU11() {
    }

    public void testCU11() {
        PoblacionDeAlojados poblacion = new PoblacionDeAlojados();
        List<? extends Alojado> lista = poblacion.crearNHuespedes();
        poblacion.guardarLista(lista);
    }
}
