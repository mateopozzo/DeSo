
import java.time.LocalDate;
import java.util.Scanner;

import ddb.deso.TipoDoc;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.DatosAlojado;
import ddb.deso.alojamiento.DatosContacto;
import ddb.deso.alojamiento.DatosPersonales;
import ddb.deso.alojamiento.DatosResidencia;
import ddb.deso.alojamiento.Huesped;
import ddb.deso.presentacion.InterfazDarBaja;
import ddb.deso.presentacion.InterfazModificarHuesped;

public class TestCU10_CU11 {


    public static void main(String[] args) {
        System.out.println("TESTING: CASO DE USO 10 - BUSCAR HUÉSPED ----------------------------");
        System.out.println("Intento de modificacion de Huesped inexistente\n\n" +
                "====================================================\n\n");
        modificarHuespedInExistente();
        System.out.println("\n\nTESTING: CASO DE USO 11 - ELIMINAR HUÉSPED ----------------------------");
        System.out.println("Intento de eliminacion de huesped existente (El guardado en intento de modificacion anterior)\n\n" +
                "====================================================\n\n");
        eliminarHuespedExistente();
        System.out.println("\n\nTESTING: CASO DE USO 11 - ELIMINAR HUÉSPED ----------------------------");
        System.out.println("Intento de eliminacion de huesped INexistente\n\n" +
                "====================================================\n\n");
        eliminarHuespedInExistente();
    }

    public static void modificarHuespedInExistente(){


        // Crear datos personales
        DatosPersonales datosPersonales = new DatosPersonales("Hernan","Lopital", "Francés","excento","Matematico","56739633",TipoDoc.LC,"25-123457-2", LocalDate.parse("2001-11-09"));

        // Crear datos de residencia
        DatosResidencia datosResidencia = new DatosResidencia("Calle falsa","Capital","Santa Fe","Santa Fe","Argentina","1234","","1234");

        // Crear datos de contacto
        DatosContacto datosContacto = new DatosContacto("1234784","herniLopi@gmail.com");

        // Crear DatosAlojado que agrupa todos los datos
        DatosAlojado datos = new DatosAlojado(datosContacto, datosResidencia, datosPersonales);

        // Crear el Alojado
        Alojado alojado = new Huesped(datos);  // Uso Huesped que extiende de Alojado

        InterfazModificarHuesped interfaz_modi = new InterfazModificarHuesped();
        interfaz_modi.ejecutarModiHuesped(alojado);
    }

    public static void eliminarHuespedInExistente(){
        // Crear datos personales
        DatosPersonales datosPersonales = new DatosPersonales("Martin","Araujo", "España","Excento","Fisico","1234J3495",TipoDoc.DNI,"27-123457-2", LocalDate.parse("1990-11-09"));

        // Crear datos de residencia
        DatosResidencia datosResidencia = new DatosResidencia("Lopez","Rafaela","Santa Fe","Santa Fe","Argentina","Treinta de febrero","","3000");

        // Crear datos de contacto
        DatosContacto datosContacto = new DatosContacto("43534553","martuaraujo@gmail.com");

        // Crear DatosAlojado que agrupa todos los datos
        DatosAlojado datos = new DatosAlojado(datosContacto, datosResidencia, datosPersonales);

        // Crear el Alojado
        Alojado alojado = new Huesped(datos);  // Uso Huesped que extiende de Alojado
        Scanner scanner = new Scanner(System.in);
        InterfazDarBaja.eliminarHuesped(alojado,scanner);
    }

    public static void eliminarHuespedExistente(){
        // Crear datos personales
        DatosPersonales datosPersonales = new DatosPersonales("Hernan","Lopital", "Francés","excento","Matematico","56739633",TipoDoc.LC,"25-123457-2", LocalDate.parse("2001-11-09"));

        // Crear datos de residencia
        DatosResidencia datosResidencia = new DatosResidencia("Calle falsa","Capital","Santa Fe","Santa Fe","Argentina","1234","","1234");

        // Crear datos de contacto
        DatosContacto datosContacto = new DatosContacto("1234784","herniLopi@gmail.com");

        // Crear DatosAlojado que agrupa todos los datos
        DatosAlojado datos = new DatosAlojado(datosContacto, datosResidencia, datosPersonales);

        // Crear el Alojado
        Alojado alojado = new Huesped(datos);  // Uso Huesped que extiende de Alojado

        Scanner scanner = new Scanner(System.in);

        InterfazDarBaja.eliminarHuesped(alojado, scanner);
    }


}
