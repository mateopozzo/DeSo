package ddb.deso.presentacion;

import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.alojamiento.CriteriosBusq;
import ddb.deso.alojamiento.GestorAlojamiento;

import java.util.List;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class InterfazBusqueda {
    public void busqueda_huesped() {
        // PUNTO DE INGRESO PRINCIPAL -> LLAMADO DESDE MAIN
        System.out.println("Hotel Premier - Buscar huésped --------------------------------------------------------");
        System.out.println("Seleccione un huésped y podrá modificarlo. Si no se encuentran coincidencias, podrá crear un nuevo huésped");
        System.out.println("Si desea crear un nuevo huésped incluso habiendo encontrado coincidencias, presione ENTER y luego SIGUIENTE");

        Scanner scanner = new Scanner(System.in);

        // Ingreso de los filtros de búsqueda
        System.out.println("Ingrese el nombre: ");
        String nombre = scanner.nextLine();

        System.out.println("Ingrese el apellido: ");
        String apellido = scanner.nextLine();

        TipoDoc tipoDoc = menuTipoDoc();

        System.out.println("Ingrese el número de documento: ");
        String num_documento = scanner.nextLine();

        // cargar_criterios valida qué criterios se ingresaron y actualiza los criterios del objeto
        CriteriosBusq criterios_busq = new CriteriosBusq(nombre, apellido, tipoDoc, num_documento);
        GestorAlojamiento.buscarHuesped(criterios_busq);
        scanner.close();
    }

    private TipoDoc menuTipoDoc(){
        System.out.println("Seleccione tipo de documento:");
        System.out.println("1. DNI");
        System.out.println("2. CI");
        System.out.println("3. LE");
        System.out.println("4. PASAPORTE");
        System.out.println("5. OTRO");

        TipoDoc tipoDoc;

        Scanner scanner = new Scanner(System.in);
        String opcion = scanner.nextLine();

        switch (opcion) {
            case "2" -> tipoDoc =TipoDoc.CI;
            case "3" -> tipoDoc =TipoDoc.LE;
            case "4" -> tipoDoc =TipoDoc.PASAPORTE;
            case "5" -> tipoDoc =TipoDoc.OTRO;
            default -> tipoDoc = TipoDoc.DNI;
        }

        return tipoDoc;
    }

    public void sin_coincidencias(){
        System.out.println("No se encontraron coincidencias de búsqueda.");
        System.out.println("DAR DE ALTA HUESPED ---- FROM CU02");
        // darDeAltaHuesped();
    }

    public void seleccion (List<AlojadoDTO> encontrados){
        AlojadoDTO huesped_seleccionado;
        String input_user;

        // Si nunca se actualiza el valor de control me lo muestra
        int seleccion=-1;

        System.out.println("Ingrese el número de huesped que desea seleccionar.");
        System.out.println("Si no desea seleccionar alguno, presione ENTER y luego 1. (SIGUIENTE)");
        for (int i = 0; i < encontrados.size(); i++) {
            // nombre, apellido, tipoDoc, num_documento
            huesped_seleccionado = encontrados.get(i);
            System.out.println(STR."\{i + 1}. \{huesped_seleccionado.getNombre()} \{huesped_seleccionado.getApellido()} - \{huesped_seleccionado.getTipoDoc()}: \{huesped_seleccionado.getNroDoc()}");
        }

        Scanner scanner = new Scanner(System.in);
        input_user = scanner.nextLine();

        // Si presionó ENTER, entonces voy a dar de alta huesped
        if (input_user.trim().isEmpty()) {
            System.out.println("Presione 1 para SIGUIENTE");
            String siguiente = scanner.nextLine();

            if ("1".equals(siguiente)) {
                System.out.println("DAR DE ALTA HUESPED ---- FROM CU02");
                // darDeAltaHuesped();
                // FIN DE CASO DE USO
            }
        }
        // Si presionó un número, entonces voy a modificar huesped
        else {
            try {
                seleccion = parseInt(input_user);
                // Si la selección está dentro del rango, lo busco en mi lista
                if (seleccion<=encontrados.size() && seleccion>0) {
                    huesped_seleccionado = encontrados.get(seleccion-1);
                    System.out.println("Huesped seleccionado con éxito.");
                    if (huesped_seleccionado != null){
                        System.out.println("MODIFICAR HUESPED ---- FROM CU02");
                        //modificarHuesped(huesped_seleccionado);
                        // FIN DE CASO DE USO
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Debe ser un número o ENTER.");
            }
        }
        scanner.close();
    }

}
