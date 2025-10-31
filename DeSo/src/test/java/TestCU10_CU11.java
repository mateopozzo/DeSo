
import java.time.LocalDate;

import ddb.deso.TipoDoc;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.DatosAlojado;
import ddb.deso.alojamiento.DatosContacto;
import ddb.deso.alojamiento.DatosPersonales;
import ddb.deso.alojamiento.DatosResidencia;
import ddb.deso.alojamiento.Huesped;
import ddb.deso.presentacion.InterfazModificarHuesped;

public class TestCU10_CU11 {


    public static void main(String[] args) {
        System.out.println("TESTING: CASO DE USO 10 - BUSCAR HUÉSPED ----------------------------");
        modificarHuespedExistente();

    }

    public static void modificarHuespedExistente(){
        // Crear datos personales
        DatosPersonales datosPersonales = new DatosPersonales("Gatito","Negro", "Francés","excento","Gato","56739633",TipoDoc.LC,"25-123457-2", LocalDate.parse("2023-11-09"));

        // Crear datos de residencia
        DatosResidencia datosResidencia = new DatosResidencia("Calle falsa","Capital","Santa Fe","Santa Fe","Argentina","1234","","1234");

        // Crear datos de contacto
        DatosContacto datosContacto = new DatosContacto("1234784","gmichitonegro@utn.frsf.edu.ar");

        // Crear DatosAlojado que agrupa todos los datos
        DatosAlojado datos = new DatosAlojado(datosContacto, datosResidencia, datosPersonales);

        // Crear el Alojado
        Alojado alojado = new Huesped(datos);  // Uso Huesped que extiende de Alojado

        InterfazModificarHuesped interfaz_modi = new InterfazModificarHuesped();
        interfaz_modi.ejecutarModiHuesped(alojado);
    }


}
