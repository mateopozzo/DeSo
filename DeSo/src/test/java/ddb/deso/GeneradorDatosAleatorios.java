package ddb.deso;//import ddb.deso.service.TipoDoc;
import ddb.deso.negocio.TipoDoc;
import ddb.deso.negocio.alojamiento.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class GeneradorDatosAleatorios {

    private static final Random RANDOM = new Random();

    // Listas de datos posibles
    private static final List<String> NOMBRES = Arrays.asList("Ana", "Juan", "María", "Carlos", "Sofia", "Mateo", "Ernesto", "Esther", "Julia", "Domingo");
    private static final List<String> APELLIDOS = Arrays.asList("Gomez", "Perez", "Rodriguez", "Lopez", "Fernandez", "Martinez", "Lehmnan", "Johnson", "Kler", "Díaz");
    private static final List<String> CALLES = Arrays.asList("Av. Corrientes", "Mitre", "Ruta 40", "Queen St", "Elm St", "Castellanos");
    private static final List<String> PAISES = Arrays.asList("Argentina", "Canadá", "España", "Brasil", "Chile", "Paraguay", "Perú");
    private static final List<String> LOCALIDADES = Arrays.asList("Buenos Aires", "Toronto", "Madrid", "Rio de Janeiro", "Santiago", "Asunción", "Lima");
    private static final List<String> POSICIONES_IVA = Arrays.asList("Responsable Inscripto", "Monotributista", "Excento", "Consumidor Final");
    private static final List<String> OCUPACIONES = Arrays.asList("Ingeniero", "Abogado", "Cientifico", "Estudiante", "Comerciante", "Empleado");

    // Genera un número aleatorio como String de la longitud especificada.

    private static String generarNroAleatorio(int longitud) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            sb.append(RANDOM.nextInt(10)); // Genera un dígito del 0 al 9
        }
        return sb.toString();
    }

    /*
     Genera un objeto DatosAlojado con valores aleatorios.
     @return un nuevo objeto DatosAlojado.
     */

    public static DatosAlojado generarDatosAlojadoAleatorio() {
        // --- Datos Personales ---
        String nombre = NOMBRES.get(RANDOM.nextInt(NOMBRES.size()));
        String apellido = APELLIDOS.get(RANDOM.nextInt(APELLIDOS.size()));
        String nacionalidad = PAISES.get(RANDOM.nextInt(PAISES.size())); // Usamos la lista de países para la nacionalidad
        String posicionIva = POSICIONES_IVA.get(RANDOM.nextInt(POSICIONES_IVA.size()));
        String ocupacion = OCUPACIONES.get(RANDOM.nextInt(OCUPACIONES.size()));
        String nroDoc = generarNroAleatorio(8); // DNI/Pasaporte de 8 dígitos simulado
        String CUIT = generarNroAleatorio(11); // CUIT de 11 dígitos simulado
        TipoDoc tipoDoc = TipoDoc.values()[RANDOM.nextInt(TipoDoc.values().length)];
        // Fecha de alta: entre 1 y 10k días en el pasado
        LocalDate fechaa = LocalDate.now().minusDays(RANDOM.nextInt(10000) + 1);

        DatosPersonales dp = new DatosPersonales(nombre, apellido, nacionalidad, posicionIva, ocupacion, tipoDoc, "Minero", nroDoc, fechaa);

        // --- Datos Residencia ---
        String pais = PAISES.get(RANDOM.nextInt(PAISES.size()));
        String localidad = LOCALIDADES.get(RANDOM.nextInt(LOCALIDADES.size()));
        String calle = CALLES.get(RANDOM.nextInt(CALLES.size()));
        String nro_calle = generarNroAleatorio(4);
        String provincia = "Provincia " + generarNroAleatorio(2);
        String nro_postal = generarNroAleatorio(5);
        String depto = RANDOM.nextBoolean() ? "Depto " + (char)('A' + RANDOM.nextInt(3)) : ""; // A, B o C, o vacío
        String piso = RANDOM.nextBoolean() ? String.valueOf(RANDOM.nextInt(10) + 1) : ""; // 1 a 10, o vacío

        DatosResidencia dr = new DatosResidencia(calle, depto, localidad, provincia, pais, nro_calle, piso, nro_postal);

        // --- Datos contacto ---
        String telefono = "1" + generarNroAleatorio(9); // Teléfono simulado con 10 dígitos
        String correo = nombre.toLowerCase() + "." + apellido.toLowerCase() + "@mail.com";

        DatosContacto dc = new DatosContacto(telefono, correo);

        // --- Datos alojado ---
        DatosAlojado da = new DatosAlojado(dc, dr, dp);

        // Los campos CICO se suelen inicializar a null o con una lista vacía para datos de prueba
        da.setCheckIns(new ArrayList<DatosCheckIn>());
        da.setCheckOuts(new ArrayList<DatosCheckOut>());

        return da;
    }

    /**
     Genera un objeto Huesped con DatosAlojado aleatorios.
     @return un nuevo objeto Huesped.
    */

    public static Huesped generarHuespedAleatorio() {
        return new Huesped(generarDatosAlojadoAleatorio());
    }
}