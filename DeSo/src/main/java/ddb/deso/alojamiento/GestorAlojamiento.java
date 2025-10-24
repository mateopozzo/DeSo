package ddb.deso.alojamiento;

import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

// @author mat

public class GestorAlojamiento {

    private List<Huesped> huespedes = new LinkedList<>();

    public boolean dniExiste(String dni, TipoDoc tipo) {
        for (Huesped h : huespedes) {
            DatosPersonales dp = h.getDatos().getDatos_personales();
            
            if (dp.getNroDoc().equals(dni) && dp.getTipoDoc().equals(tipo)) {
                return true;
            }
        }
        return false;
    }

    private static TipoDoc convertirTipoDoc(String opcion) {
        return switch (opcion) {
            case "1" -> TipoDoc.DNI;
            case "2" -> TipoDoc.CI;
            case "3" -> TipoDoc.LE;
            case "4" -> TipoDoc.PASAPORTE;
            case "5" -> TipoDoc.OTRO;
            default -> null;
        };
    }

    public void darDeAltaHuesped(){

       Scanner entrada = new Scanner(System.in);
       boolean b=true;
       while(b){
       System.out.println("Apellido:");
       String apellido = entrada.nextLine();

       System.out.println("Nombre:");
       String nombre = entrada.nextLine();

         // Menú para elegir tipo de documento
       System.out.println("Seleccione tipo de documento:");
       System.out.println("1. DNI");
       System.out.println("2. CI");
       System.out.println("3. LE");
       System.out.println("4. PASAPORTE");
       System.out.println("5. OTRO");
       String opcion = entrada.nextLine();
       TipoDoc tipoDoc = convertirTipoDoc(opcion);

       System.out.println("Numero de documento:");
       String num_documento = entrada.nextLine();

       System.out.println("CUIT:");
       String cuit = entrada.nextLine(); //no obligatorio

       System.out.println("Posición frente al IVA:");
       String iva = entrada.nextLine();

        //!
       System.out.println("Fecha de Nacimiento:");
       System.out.println("AÑO:");
       String ano = entrada.nextLine();
       System.out.println("MES:");
       String mes = entrada.nextLine();
       System.out.println("DIA:");
       String dia = entrada.nextLine();

       LocalDate fecha_nacimiento= LocalDate.of(
       Integer.parseInt(ano),
       Integer.parseInt(mes),
       Integer.parseInt(dia));

       System.out.println("Dirección:");
       System.out.println("Calle:");
       String calle = entrada.nextLine();

       System.out.println("Numero:");
       String numero = entrada.nextLine();

       System.out.println("Departamento:");
       String departamento = entrada.nextLine();

       System.out.println("Piso:");
       String piso = entrada.nextLine();

       System.out.println("Código:");
       String codigo = entrada.nextLine();

       System.out.println("postal:");
       String postal = entrada.nextLine();

       System.out.println("Localidad:");
       String localidad = entrada.nextLine();

       System.out.println("Provincia:");
       String provincia = entrada.nextLine();

       System.out.println("Pais:");
       String pais = entrada.nextLine();



       System.out.println("Teléfono:");
       String telefono = entrada.nextLine();

       System.out.println("Email:");
       String email = entrada.nextLine(); //no obligatorio

       System.out.println("Ocupación:");
       String ocupacion = entrada.nextLine();

       System.out.println("Nacionalidad");
       String nacionalidad = entrada.nextLine();

       //ver si lo pongo dsp e comprobar si están vacíos
       DatosResidencia dr= new DatosResidencia(calle,departamento,localidad,provincia,pais,numero,piso,postal);

       //Huesped h=new Huesped(null);
       String boton="-1";

          while(!(boton.equals("1")||boton.equals("2"))){
             System.out.println("Presione 1 para SIGUIENTE o 2 para CANCELAR" );
             boton=entrada.nextLine();

             if(boton.equals("2")){

                String boton3="-1";
                while(!(boton3.equals("1")||boton3.equals("2"))){

                   System.out.println("¿Desea cancelar el alta de huesped?");
                   System.out.println("SI (1) o NO (2) ");
                   boton3=entrada.nextLine();
                }

                if(boton3.equals("1")){
                   return;//sale del bucle principal y el CU termina
                }

                else if (boton3.equals("2")){
                   boton="1";
                }

             }

          }

          if(boton.equals("1")){
                //faltan datos
             if (apellido.isEmpty()  || nombre.isEmpty()   || tipoDoc==null ||
                 num_documento.isEmpty()    || fecha_nacimiento == null    || calle.isEmpty()   ||
                 localidad.isEmpty() || provincia.isEmpty() || pais.isEmpty()|| telefono.isEmpty()
                 || iva.isEmpty() || ocupacion.isEmpty()|| nacionalidad.isEmpty()) {
                    System.out.println("\n ERROR: Faltan datos obligatorios.");
                    continue;
                }

                long tel = telefono.isEmpty() ? 0 : Long.parseLong(telefono);
                Long nroDoc = Long.parseLong(num_documento);
                Long cuitInt = cuit.isEmpty() ? 0 : Long.parseLong(cuit);

                DatosContacto dc=new DatosContacto(tel, email);

                DatosPersonales dp= new DatosPersonales(nombre, apellido, nacionalidad, iva, ocupacion,
                nroDoc.toString(), tipoDoc,cuitInt.toString() , fecha_nacimiento);

                DatosAlojado da = new DatosAlojado(dc,dr,dp);

                Huesped h= new Huesped(da);


             if(dniExiste(nroDoc.toString(), tipoDoc)){
             String boton2= "-1";
                while(!(boton2.equals("1")||boton2.equals("2"))){
                System.out.println("\n¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
                System.out.print("¿Desea ACEPTAR IGUALMENTE (1) o CORREGIR (2)? ");
                boton2= entrada.nextLine();
                }
                if(boton2.equals("2")) continue;
             }

             huespedes.add(h);
             System.out.print("El Huésped " + h.datos.getDatos_personales().getNombre() + " " +
             h.datos.getDatos_personales().getApellido()  + " se ha cargado correctamente.");
             String boton4 = "-1";
                while(!(boton4.equals("1")||boton4.equals("2"))){
                   System.out.print("¿Desea cargar otro? SI (1) NO (2):  ");
                   boton4 = entrada.nextLine();
                }
                if (boton4.equals("2")) b = false;


          }

       }//while


    }//método

    public void buscarHuesped (CriteriosBusq criterios_busq){
        /* Recibe los paŕametros de búsqueda en criterios_busq (String apellido, String nombre, TipoDoc tipoDoc, String nroDoc)
        Llama al DAO, que llama a DAOJSON y busca todos los alojados
        Cuando los encuentra, crea un DTO y los va colando en una lista "encontrados"
        Si no encuentra coincidencias, encontrados is empty y se ejecuta darDeAltaHuesped() -> Fin CU

        Si encuentra, se muestra de 1 al n la cantidad de coincidencias
        Usuario ingresa opción input_user y se parsea a un int seleccion
        Se busca en la lista quien es el huesped seleccion-1 y se almacena
        Se llama a modificarHuesped() con la instancia de huesped_seleccionado -> Fin CU
        */

        List<AlojadoDTO> encontrados;
        encontrados = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        AlojadoDTO huesped_seleccionado;
        String input_user;
        int seleccion=-1;

        // Cargar los criterios de búsqueda
        System.out.println("Hotel Premier - Buscar huésped ----------------------------------------------------------------------------");
        System.out.println("Seleccione un huésped y podrá modificarlo. Si no se encuentran coincidencias, podrá crear un nuevo huésped");
        System.out.println("Si desea crear un nuevo huésped incluso habiendo encontrado coincidencias, presione ENTER y luego SIGUIENTE");

        // Ingreso de los filtros de búsqueda
        System.out.println("Ingrese el nombre: ");
        String nombre = scanner.nextLine();

        System.out.println("Ingrese el apellido: ");
        String apellido = scanner.nextLine();

        System.out.println("Seleccione tipo de documento:");
        System.out.println("1. DNI");
        System.out.println("2. CI");
        System.out.println("3. LE");
        System.out.println("4. PASAPORTE");
        System.out.println("5. OTRO");

        String tipo_doc = scanner.nextLine();
        TipoDoc tipoDoc = convertirTipoDoc(tipo_doc);

        System.out.println("Ingrese el número de documento: ");
        String num_documento = scanner.nextLine();

        // cargar_criterios valida qué criterios se ingresaron y actualiza los criterios del objeto
        cargar_criterios (nombre, apellido, tipoDoc, num_documento, criterios_busq);

        // BÚSQUEDA EN JSON: Llamo al DAO -> Busca en la BDD -> Crea los DTO -> Devuelve lista de DTO a DAO -> DAO devuelve lista a gestor

        if (encontrados.isEmpty()) {
            System.out.println("No se encontraron coincidencias de búsqueda.");
            scanner.close();
            darDeAltaHuesped();
            // FIN DE CASO DE USO
        }
        else {
            System.out.println("Ingrese el número de huesped que desea seleccionar.");
            System.out.println("Si no desea seleccionar alguno, presione ENTER y luego 1. (SIGUIENTE)");
            input_user = scanner.nextLine();
            seleccion = Integer.parseInt(input_user);
            System.out.println("Presione 1 para SIGUIENTE");
            // Que lea el input, lo guarde sea enter o nro. If nro, llamo a modificar. If enter, llamo a dar alta
            scanner.close();
        }

        if (seleccion<=encontrados.size() && seleccion>0) {
            huesped_seleccionado = encontrados.get(seleccion-1);
            System.out.println("Huesped seleccionado con éxito.");
            if (huesped_seleccionado != null){
                // modificarHuesped(huesped_seleccionado);
                // FIN DE CASO DE USO
            }
        }

        else {
            darDeAltaHuesped();
        }

    }

    private void cargar_criterios (String nombre, String apellido, TipoDoc tipoDoc, String num_documento, CriteriosBusq criterio) {
        if (no_es_vacio(nombre)){
            criterio.setNombre(nombre);
        }
        if (no_es_vacio(apellido)){
            criterio.setApellido(apellido);
        }
        if (no_es_vacio(tipoDoc.toString())){
            criterio.setTipoDoc(tipoDoc);
        }
        if (no_es_vacio(num_documento)){
            criterio.setNroDoc(num_documento);
        }
    }

    // Esto es laburo del DAO y probablemente no deba ir acá
    private boolean cumpleCriterio (Huesped huesped, CriteriosBusq criterio) {
        // Criterios de búsqueda que pueden o no estar vacíos -> Hechos con clase plantilla CriteriosBusq
        String apellido_b = criterio.getApellido();
        String nombres_b = criterio.getNombre();
        TipoDoc tipoDoc_b = criterio.getTipoDoc();
        String nroDoc_b = criterio.getNroDoc();

        // Atributos reales del huesped
        DatosPersonales datos_h = huesped.getDatos().getDatos_personales();

        String apellido_h = datos_h.getApellido();
        String nombre_h = datos_h.getNombre();
        TipoDoc tipoDoc_h = datos_h.getTipoDoc();
        String nroDoc_h = datos_h.getNroDoc();

        if (no_es_vacio(apellido_b) && !apellido_h.equalsIgnoreCase(apellido_b)) {
            return false;
        }
        if (no_es_vacio(nombres_b) && !nombre_h.equalsIgnoreCase(nombres_b)) {
            return false;
        }
        if (no_es_vacio(tipoDoc_b.toString()) && !tipoDoc_h.equals(tipoDoc_b)) {
            return false;
        }
        if (no_es_vacio(nroDoc_b) && !nroDoc_h.equals(nroDoc_b)) {
            return false;
        }

        return true;
    }

    private boolean no_es_vacio (String contenido){
        boolean flag = (contenido==null || contenido.isEmpty());
        return !flag;
    }

}

