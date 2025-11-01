package ddb.deso.presentacion;

import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.alojamiento.*;

import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

/**
 * Clase que gestiona la interfaz de usuario para la **búsqueda de un huésped**
 * existente (Caso de Uso 02 o CU02).
 * <p>Permite al usuario ingresar criterios (nombre, apellido, DNI) y, basado en los
 * resultados:
 * <ul>
 * <li>Si hay coincidencias, permite seleccionar un huésped para **modificarlo**
 * (llamando a {@code InterfazModificarHuesped}).</li>
 * <li>Si no hay coincidencias o el usuario decide no seleccionar, inicia el
 * flujo de **alta de un nuevo huésped** (llamando a {@code InterfazDarAlta}).</li>
 * </ul>
 * <p>
 *
 * @author Gael
 * @see GestorAlojamiento#buscarHuesped(CriteriosBusq)
 */
public class InterfazBusqueda {
    private final Scanner scanner;

    /**
     * Constructor. Inicializa el objeto {@link Scanner} para la entrada de usuario.
     */
    public InterfazBusqueda() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * **Flujo principal:** Punto de entrada para el caso de uso de búsqueda.
     * <p>
     * Solicita al usuario ingresar criterios de búsqueda (nombre, apellido, tipo y número de documento).
     * Crea un objeto {@link CriteriosBusq} y llama a {@code GestorAlojamiento.buscarHuesped}
     * para iniciar el proceso de búsqueda.
     * </p>
     */
    public void busqueda_huesped() {
        // PUNTO DE INGRESO PRINCIPAL -> LLAMADO DESDE MAIN
        System.out.println("Hotel Premier - Buscar huésped --------------------------------------------------------");
        System.out.println("Seleccione un huésped y podrá modificarlo. Si no se encuentran coincidencias, podrá crear un nuevo huésped");
        System.out.println("Si desea crear un nuevo huésped incluso habiendo encontrado coincidencias, presione ENTER y luego SIGUIENTE");

        // Ingreso de los filtros de búsqueda
        System.out.println("Ingrese el nombre: ");
        String nombre = scanner.nextLine();
        if(nombre.isEmpty()) nombre = null;

        System.out.println("Ingrese el apellido: ");
        String apellido = scanner.nextLine();
        if(apellido.isEmpty()) nombre = null;

        TipoDoc tipoDoc = menuTipoDoc(scanner);

        System.out.println("Ingrese el número de documento: ");
        String num_documento = scanner.nextLine();
        if(num_documento.isEmpty()) nombre = null;

        // cargar_criterios valida qué criterios se ingresaron y actualiza los criterios del objeto
        CriteriosBusq criterios_busq = new CriteriosBusq(apellido, nombre, tipoDoc, num_documento);
        GestorAlojamiento.buscarHuesped(criterios_busq);
    }

    /**
     * Muestra un menú de opciones para que el usuario seleccione el tipo de documento.
     *
     * @param scanner El {@link Scanner} abierto para la entrada del usuario.
     * @return El {@link TipoDoc} seleccionado. Retorna {@code null} si el usuario presiona ENTER
     */
    private TipoDoc menuTipoDoc(Scanner scanner){
        System.out.println("Ingrese número correspondiente para seleccionar tipo de documento:");
        System.out.println("1. DNI");
        System.out.println("2. LE");
        System.out.println("3. LC");
        System.out.println("4. PASAPORTE");
        System.out.println("5. OTRO");
        System.out.println("Enter. NINGUNO");

        TipoDoc tipoDoc;

        String opcion = scanner.nextLine();

        switch (opcion) {
            case "1" -> tipoDoc =TipoDoc.DNI;
            case "2" -> tipoDoc =TipoDoc.LE;
            case "3" -> tipoDoc =TipoDoc.LC;
            case "4" -> tipoDoc =TipoDoc.PASAPORTE;
            case "5" -> tipoDoc =TipoDoc.OTRO;
            default -> tipoDoc = null;  //  el actor puede no elegir tipo
        }

        return tipoDoc;
    }

    /**
     * Muestra el mensaje de que **no se encontraron coincidencias** con los criterios
     * de búsqueda e inicia inmediatamente el proceso de **alta de un nuevo huésped**
     * (llamando a {@code InterfazDarAlta.ejecutarDarAlta()}).
     */
    public void sin_coincidencias(){
        System.out.println("No se encontraron coincidencias de búsqueda.\n");
        InterfazDarAlta ui_alta = new InterfazDarAlta();
        ui_alta.ejecutarDarAlta();
        // FIN DE CASO DE USO
    }

    /**
     * Muestra la lista de huéspedes encontrados y solicita al usuario que seleccione uno
     * para modificar, o que continúe para dar de alta uno nuevo.
     *
     * <ul>
     * <li>Si selecciona un número válido: Convierte el DTO a un objeto {@code Alojado} completo
     * y llama a {@code InterfazModificarHuesped.ejecutarModiHuesped()}.</li>
     * <li>Si presiona ENTER + SIGUIENTE ('1'): Llama a {@code InterfazDarAlta.ejecutarDarAlta()}.</li>
     * </ul>
     *
     * @param encontrados Una lista de {@link AlojadoDTO} que coinciden con la búsqueda.
     */
    public void seleccion (List<AlojadoDTO> encontrados){
        AlojadoDTO h_encontrado;
        String input_user;

        // Si nunca se actualiza el valor bandera me lo muestra
        int seleccion=-1;

        System.out.println("Ingrese el número de huesped que desea seleccionar.");
        System.out.println("Si no desea seleccionar alguno, presione ENTER y luego 1. (SIGUIENTE)");
        for (int i = 0; i < encontrados.size(); i++) {
            // nombre, apellido, tipoDoc, num_documento
            h_encontrado = encontrados.get(i);
            System.out.println((i+1) + ". " + h_encontrado.getNombre() + " " + h_encontrado.getApellido() + " - " + h_encontrado.getTipoDoc() + ": " + h_encontrado.getNroDoc());
        }

        Scanner scanner = new Scanner(System.in);
        input_user = scanner.nextLine();

        // Si presionó ENTER, entonces voy a dar de alta huesped
        if (input_user.trim().isEmpty()) {
            System.out.println("Presione 1 para SIGUIENTE");
            String siguiente = scanner.nextLine();

            if ("1".equals(siguiente)) {
                InterfazDarAlta ui_alta = new InterfazDarAlta();
                ui_alta.ejecutarDarAlta();
                // FIN DE CASO DE USO
            }
        }
        // Si presionó un número, entonces voy a modificar huesped
        else {
            try {
                seleccion = parseInt(input_user);
                // Si la selección está dentro del rango, lo busco en mi lista
                if (seleccion<=encontrados.size() && seleccion>0) {
                    h_encontrado = encontrados.get(seleccion-1);
                    System.out.println("Huesped seleccionado con éxito.");
                    if (h_encontrado != null){
                        System.out.println("MODIFICAR HUESPED ---- FROM CU02");
                        DatosContacto cont = new DatosContacto(h_encontrado.getTelefono(), h_encontrado.getEmail());
                        DatosResidencia res = new DatosResidencia(h_encontrado.getCalle(), h_encontrado.getDepto(), h_encontrado.getLocalidad(), h_encontrado.getProv(), h_encontrado.getPais(), h_encontrado.getNro_calle(), h_encontrado.getPiso(), h_encontrado.getCod_post());
                        DatosPersonales per = new DatosPersonales(h_encontrado.getNombre(), h_encontrado.getApellido(), h_encontrado.getNacionalidad(), h_encontrado.getPosicionIva(), h_encontrado.getOcupacion(), h_encontrado.getNroDoc(), h_encontrado.getTipoDoc(), h_encontrado.getCUIT(), h_encontrado.getFechanac());
                        DatosAlojado datos_huesped = new DatosAlojado(cont, res, per);
                        Alojado huesped_h = FactoryAlojado.create(1, datos_huesped);

                        InterfazModificarHuesped ui_modif = new InterfazModificarHuesped();
                        ui_modif.ejecutarModiHuesped(huesped_h);
                        // FIN DE CASO DE USO
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ser un número o ENTER.");
            }
        }
    }

    /**
     * Cierra el objeto {@link Scanner} interno. Se utiliza para evitar fugas de recursos.
     * @deprecated
     */
    // Metodo aparte para cerrar el scanner porque si no suele tener problemas de cierre prematuro
    public void close() {
        /*scanner.close();*/
    }

}
