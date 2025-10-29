
import java.time.LocalDate;

import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.JSON.AlojadoDAOJSON;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.DatosAlojado;
import ddb.deso.alojamiento.DatosContacto;
import ddb.deso.alojamiento.DatosPersonales;
import ddb.deso.alojamiento.DatosResidencia;
import ddb.deso.alojamiento.GestorAlojamiento;
import ddb.deso.alojamiento.Huesped;

public class TestCU10{
    public static void main(String[] args) {
        AlojadoDAOJSON alojadoDAOJSON = new AlojadoDAOJSON();
        GestorAlojamiento gestor_aloj = new GestorAlojamiento(alojadoDAOJSON);

        System.out.println("TESTING: CASO DE USO 10 - BUSCAR HUÉSPED ----------------------------");

        // CASO DE USO 10: Modificar Huesped
        // Crear datos personales
        DatosPersonales datosPersonales = new DatosPersonales("Guillermo","Orellano", "argentino","excento","Docente","34784093",TipoDoc.DNI,"20-34784093-2", LocalDate.parse("2025-10-29"));

        // Crear datos de residencia
        DatosResidencia datosResidencia = new DatosResidencia("Lopez y Planes","Capital","Santa Fe","Santa Fe","Argentina","1456","","3000");

        // Crear datos de contacto
        DatosContacto datosContacto = new DatosContacto("4742205","guillermoorellano@utn.frsf.edu.ar");

        // Crear DatosAlojado que agrupa todos los datos
        DatosAlojado datos = new DatosAlojado(datosContacto, datosResidencia, datosPersonales);

        // Crear el Alojado
        Alojado alojado = new Huesped(datos);  // Uso Huesped que extiende de Alojado

        gestor_aloj.modificarHuesped(alojado);

    }
}
