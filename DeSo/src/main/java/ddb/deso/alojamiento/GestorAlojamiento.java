package ddb.deso.alojamiento;
import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.JSON.AlojadoDAOJSON;
import ddb.deso.presentacion.InterfazDarBaja;

import ddb.deso.contabilidad.ResponsablePago;
import java.nio.MappedByteBuffer;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.BitSet;
import java.util.Scanner;

import static java.lang.Integer.parseInt;


public class GestorAlojamiento {
    private final AlojadoDAO alojadoDAO;
    private List<Huesped> huespedes = new LinkedList<>();

    /*
    Inyección de dependencias porque si no no me deja importar el metodo del DAO
    Inyección por constructor: final, la dependencia es explícita, ayuda al testing
    */

//    public GestorAlojamiento() {}

    public GestorAlojamiento(AlojadoDAO alojadoDAO) {
        this.alojadoDAO = alojadoDAO;
    }

    public boolean dniExiste(String dni, TipoDoc tipo) {
        for (Huesped h : huespedes) {
            DatosPersonales dp = h.getDatos().getDatos_personales();
            
            if (dp.getNroDoc().equals(dni) && dp.getTipoDoc().equals(tipo)) {
                return true;
            }
        }
        return false;
    }

    public void darDeAltaHuesped(){

    Huesped datosModificados = new Huesped(null);
    Scanner entrada = new Scanner(System.in);
    boolean bandera=true;
    BitSet camposInvalidos = new BitSet(12); // BitSet para indicar que campos son invalidos
    BitSet camposDireccionInvalidos = new BitSet(8); // BitSet para indicar que campos de direccion son invalidos
    camposInvalidos.set(0,12); // Inicializo todos los bits en falso
    camposDireccionInvalidos.set(0,8); // Inicializo todos los bits en falso

     while(bandera){
        System.out.println("entro");
        listaDatosHuesped(datosModificados);

        System.out.print("Campos invalidos:");
        for (int i = 0; i < 12; i++) {
            if (camposInvalidos.get(i)) {
                System.out.print(" " + (i + 1));
            }
        }
        System.out.println();
        System.out.print("Seleccione el número del campo que desea modificar: ");
        String opcion = entrada.nextLine();

        System.out.print("\033[H\033[2J"); System.out.flush(); // <<-- BORRA LA TERMINAL (ANSI)
        switch (opcion.toLowerCase()) {
            case "1":
                 System.out.print("Apellido: ");
                 String nuevoApellido = entrada.nextLine();
                 datosModificados.getDatos().getDatos_personales().setApellido(nuevoApellido);
                 if(Validador.isApellidoValido(nuevoApellido)){
                     camposInvalidos.clear(0);
                 } else {
                     camposInvalidos.set(0);
                 }
                 break;
            case "2":
                 System.out.print("Nombre: ");
                 String nuevoNombre = entrada.nextLine();
                 datosModificados.getDatos().getDatos_personales().setNombre(nuevoNombre);
                 if(Validador.isNombreValido(nuevoNombre)){
                     camposInvalidos.clear(1);
                 } else {
                     camposInvalidos.set(1);
                 }
                 break;
            case "3":
                 System.out.print("Tipo de Documento: ");
                 TipoDoc nuevoTipoDoc = menuTipoDoc();
                 datosModificados.getDatos().getDatos_personales().setTipoDoc(nuevoTipoDoc);
                 if(Validador.isTipoDocumentoValido(nuevoTipoDoc)){
                     camposInvalidos.clear(2);
                 } else {
                     camposInvalidos.set(2);
                 }
                 break;
            case "4":
                 System.out.print("Número de Documento: ");
                 String nuevoNroDoc = entrada.nextLine();
                 datosModificados.getDatos().getDatos_personales().setNroDoc(nuevoNroDoc);
                 if(Validador.isNumeroDocumentoValido(nuevoNroDoc, datosModificados.getDatos().getDatos_personales().getTipoDoc())){
                     camposInvalidos.clear(3);
                 } else {
                     camposInvalidos.set(3);
                 }
                 break;
            case "5"://No Obligatorio
                 System.out.print("Cuit: ");
                 String nuevoCuit = entrada.nextLine();
                 datosModificados.getDatos().getDatos_personales().setCUIT(nuevoCuit);
                 if(Validador.isCuitValidoOpcional(nuevoCuit)){
                     camposInvalidos.clear(4);
                 } else {
                     camposInvalidos.set(4);
                 }
                 break;
            case "6":
                 System.out.print("Posición frente al IVA: ");
                 String nuevaPosIva = entrada.nextLine();
                 datosModificados.getDatos().getDatos_personales().setPosicionIva(nuevaPosIva);
                 if(Validador.isPosicionIvaValida(nuevaPosIva)){
                     camposInvalidos.clear(5);
                 } else {
                     camposInvalidos.set(5);
                 }
                 break;
            case "7":
                 System.out.print("Fecha de Nacimiento (AAAA-MM-DD): ");
                 String nuevaFechaStr = entrada.nextLine();
                 LocalDate nuevaFecha = LocalDate.parse(nuevaFechaStr);
                 datosModificados.getDatos().getDatos_personales().setFechanac(nuevaFecha);
                    if(Validador.isFechaNacimientoValida(nuevaFecha)){
                        camposInvalidos.clear(6);
                    } else {
                        camposInvalidos.set(6);
                    }
                 break;
            case "8":
                 System.out.println("1 Calle, 2 Número, 3 Piso, 4 Departamento, 5 Localidad, 6 Provincia, 7 País, 8 Código Postal ");
                 System.out.print("Campos invalidos:");
                 for (int i = 0; i < 8; i++) {
                     if (camposDireccionInvalidos.get(i)) {
                         System.out.print(" " + (i + 1));
                     }
                 }
                 System.out.println();
                 System.out.print("Seleccione el número del campo que desea modificar: ");
                 opcion = entrada.nextLine();
                 switch (opcion) {
                    case "1":
                       System.out.print("Calle: ");
                       String nuevaCalle = entrada.nextLine();
                       datosModificados.getDatos().getDatos_residencia().setCalle(nuevaCalle);
                       if(Validador.isCalleValida(nuevaCalle)){
                           camposDireccionInvalidos.clear(0);
                       } else {
                           camposDireccionInvalidos.set(0);
                       }
                       break;
                    case "2":
                       System.out.print("Número: ");
                       String nuevoNumero = entrada.nextLine();
                       datosModificados.getDatos().getDatos_residencia().setNro_calle(nuevoNumero);
                       if(Validador.isNumeroCalleValido(nuevoNumero)){
                           camposDireccionInvalidos.clear(1);
                       } else {
                           camposDireccionInvalidos.set(1);
                       }
                       break;
                    case "3":
                       System.out.print("Piso: ");
                       String nuevoPiso = entrada.nextLine();
                       datosModificados.getDatos().getDatos_residencia().setPiso(nuevoPiso);
                       if(Validador.isNumeroCalleValido(nuevoPiso)){
                           camposDireccionInvalidos.clear(2);
                       } else {
                           camposDireccionInvalidos.set(2);
                       }
                       break;
                    case "4":
                       System.out.print("Departamento: ");
                       String nuevoDepto = entrada.nextLine();
                       datosModificados.getDatos().getDatos_residencia().setDepto(nuevoDepto);
                       if(Validador.isNumeroCalleValido(nuevoDepto)){
                           camposDireccionInvalidos.clear(3);
                       } else {
                           camposDireccionInvalidos.set(3);
                       }
                       break;
                    case "5":
                       System.out.print("Localidad: ");
                       String nuevaLocalidad = entrada.nextLine();
                       datosModificados.getDatos().getDatos_residencia().setLocalidad(nuevaLocalidad);
                       if(Validador.isLocalidadValida(nuevaLocalidad)){
                           camposDireccionInvalidos.clear(4);
                       } else {
                           camposDireccionInvalidos.set(4);
                       }
                       break;
                    case "6":
                       System.out.print("Provincia: ");
                       String nuevaProvincia = entrada.nextLine();
                       datosModificados.getDatos().getDatos_residencia().setProv(nuevaProvincia);
                       if(Validador.isProvinciaValida(nuevaProvincia)){
                           camposDireccionInvalidos.clear(5);
                       } else {
                           camposDireccionInvalidos.set(5);
                       }
                       break;
                    case "7":
                       System.out.print("País: ");
                       String nuevoPais = entrada.nextLine();
                       datosModificados.getDatos().getDatos_residencia().setPais(nuevoPais);
                       if(Validador.isPaisValido(nuevoPais)){
                           camposDireccionInvalidos.clear(6);
                       } else {
                           camposDireccionInvalidos.set(6);
                       }
                       break;
                    case "8":
                       System.out.print("Código Postal: ");
                       String nuevoCodPost = entrada.nextLine();
                       datosModificados.getDatos().getDatos_residencia().setCod_post(nuevoCodPost);
                       if(Validador.isCodigoPostalValido(nuevoCodPost)){
                           camposDireccionInvalidos.clear(7);
                       } else {
                           camposDireccionInvalidos.set(7);
                       }
                       break;
                 }
                 if(camposDireccionInvalidos.isEmpty()){
                     camposInvalidos.clear(7);
                 } else {
                     camposInvalidos.set(7);
                 }
                 break;
            case "9":
                 System.out.print("Teléfono: ");
                 String nuevoTelefono = entrada.nextLine();
                 datosModificados.getDatos().getDatos_contacto().setTelefono(nuevoTelefono);
                 if(Validador.isTelefonoValido(nuevoTelefono)){
                     camposInvalidos.clear(8);
                 } else {
                     camposInvalidos.set(8);
                 }
                 break;
            case "10":
                 System.out.print("Email: ");
                 String nuevoEmail = entrada.nextLine();
                 datosModificados.getDatos().getDatos_contacto().setEmail(nuevoEmail);
                    if(Validador.isEmailValidoOpcional(nuevoEmail)){
                        camposInvalidos.clear(9);
                    } else {
                        camposInvalidos.set(9);
                    }
                 break;
            case "11":
                 System.out.print("Ocupación: ");
                 String nuevaOcupacion = entrada.nextLine();
                 datosModificados.getDatos().getDatos_personales().setOcupacion(nuevaOcupacion);
                 if(Validador.isOcupacionValida(nuevaOcupacion)){
                     camposInvalidos.clear(10);
                 } else {
                     camposInvalidos.set(10);
                 }
                 break;
            case "12":
                 System.out.print("Nacionalidad: ");
                 String nuevaNacionalidad = entrada.nextLine();
                 datosModificados.getDatos().getDatos_personales().setNacionalidad(nuevaNacionalidad);
                 if(Validador.isNacionalidadValida(nuevaNacionalidad)){
                     camposInvalidos.clear(11);
                 } else {
                     camposInvalidos.set(11);
                 }
                 break;
            case "siguiente":
            case "s":
                 if (camposInvalidos.isEmpty()) {
                     //System.out.println("Los datos del huésped han sido  correctamente.");
                    /*
                     if(auxDTO.buscarPorDNI(nuevoNroDoc,nuevoNroDoc)){
                         String boton2= "-1";
                         while(!(boton2.equals("1")||boton2.equals("2"))){
                             System.out.println("\n¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
                             System.out.print("¿Desea ACEPTAR IGUALMENTE (1) o CORREGIR (2)? ");
                             boton2= entrada.nextLine();
                         }
                }
            if(boton2.equals("1")) {
                    //guardo datos con dni repettido
             System.out.print("El Huésped " + h.datos.getDatos_personales().getNombre() + " " +
             h.datos.getDatos_personales().getApellido()  + " se ha cargado correctamente.");
                String boton4 = "-1";
                while(!(boton4.equals("1")||boton4.equals("2"))){
                   System.out.print("¿Desea cargar otro? SI (1) NO (2):  ");
                   boton4 = entrada.nextLine();
                }
                    if (boton4.equals("2")) bandera = false;
                    else darDeAltaHuesped();
                }
            else {
                camposInvalidos.set(4);

            }
             }

             else{
                //guardar datos
    */
            System.out.print("El Huésped " + datosModificados.datos.getDatos_personales().getNombre() + " " +
            datosModificados.datos.getDatos_personales().getApellido()  + " se ha cargado correctamente.");
            String boton4 = "-1";
                while(!(boton4.equals("1")||boton4.equals("2"))){
                   System.out.print("¿Desea cargar otro? SI (1) NO (2):  ");
                   boton4 = entrada.nextLine();
                }
                    if (boton4.equals("2")) bandera = false;
                    else darDeAltaHuesped();

             }


          //  }
             else {
                     System.out.println("No se pueden guardar los cambios. Hay campos inválidos.");
                 }
                 break;
            case "cancelar":
            case "c":
                String boton3="-1";
                while(!(boton3.equals("1")||boton3.equals("2"))){

                   System.out.println("¿Desea cancelar el alta de huesped?");
                   System.out.println("SI (1) o NO (2) ");
                   boton3=entrada.nextLine();
                }

                if(boton3.equals("1")){
                  bandera=false;//sale del bucle principal y el CU termina
                }
                //else vuelve al menu

             break;

       }

}

}//darAltaHuesped

 /*
      DatosResidencia dr= new DatosResidencia(calle,departamento,localidad,provincia,
      pais,numero,piso,postal);

      DatosContacto dc=new DatosContacto(Long.parseLong(telefono), email);

      DatosPersonales dp= new DatosPersonales(nombre, apellido, nacionalidad, iva, ocupacion,
      num_documento, tipoDoc,cuit , fecha_nacimiento);

      DatosAlojado da = new DatosAlojado(dc,dr,dp);

      Huesped h= new Huesped(da);
      Huesped hdto =new Huesped(null);
      AlojadoDTO auxDTO = new AlojadoDTO(hdto);

      AlojadoDTO aDTO = new AlojadoDTO(h);
      AlojadoDAO aDAO= new AlojadoDAOJSON();
      aDAO.crearAlojado(aDTO);
 */

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
        Scanner scanner = new Scanner(System.in);
        AlojadoDTO huesped_seleccionado;
        String input_user;
        int seleccion=-1;

        // Cargar los criterios de búsqueda
        System.out.println("Hotel Premier - Buscar huésped --------------------------------------------------------");
        System.out.println("Seleccione un huésped y podrá modificarlo. Si no se encuentran coincidencias, podrá crear un nuevo huésped");
        System.out.println("Si desea crear un nuevo huésped incluso habiendo encontrado coincidencias, presione ENTER y luego SIGUIENTE");

        // Ingreso de los filtros de búsqueda
        System.out.println("Ingrese el nombre: ");
        String nombre = scanner.nextLine();

        System.out.println("Ingrese el apellido: ");
        String apellido = scanner.nextLine();

        TipoDoc tipoDoc = menuTipoDoc();

        System.out.println("Ingrese el número de documento: ");
        String num_documento = scanner.nextLine();

        // cargar_criterios valida qué criterios se ingresaron y actualiza los criterios del objeto
        cargar_criterios (nombre, apellido, tipoDoc, num_documento, criterios_busq);

        // BÚSQUEDA EN JSON: Llamo al DAO -> Busca en la BDD -> Crea los DTO -> Devuelve lista de DTO a DAO -> DAO devuelve lista a gestor
        encontrados = alojadoDAO.buscarHuespedDAO(criterios_busq);

        if (encontrados.isEmpty()) {
            System.out.println("No se encontraron coincidencias de búsqueda.");
            scanner.close();
            System.out.println("DAR DE ALTA HUESPED ---- FROM CU02");
            // darDeAltaHuesped();
            // FIN DE CASO DE USO
        }
        else {
            System.out.println("Ingrese el número de huesped que desea seleccionar.");
            System.out.println("Si no desea seleccionar alguno, presione ENTER y luego 1. (SIGUIENTE)");
            for (int i = 0; i < encontrados.size(); i++) {
                // nombre, apellido, tipoDoc, num_documento

            }
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

    private boolean no_es_vacio (String contenido){
        boolean flag = (contenido==null || contenido.isEmpty());
        return !flag;
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

    // Modificar huesped debería modificar alojados, no huesped (también puede modificar invitados)
    public void modificarHuesped(Alojado alojado){
        // creo un huesped auxiliar para modificar
      AlojadoDTO dto = new AlojadoDTO(alojado);
      Alojado datosModificados = alojado;
      Scanner entrada = new Scanner(System.in);

      boolean bandera=true;
      BitSet camposInvalidos = new BitSet(12); // BitSet para indicar que campos son invalidos
      BitSet camposDireccionInvalidos = new BitSet(8); // BitSet para indicar que campos de direccion son invalidos
      camposInvalidos.clear(); // Inicializo todos los bits en falso
      camposDireccionInvalidos.clear(); // Inicializo todos los bits en falso

  while(bandera){
        listaDatosHuesped(datosModificados);
        System.out.println("O escriba Siguiente (s) o Cancelar (c) o Borrar (b)");
        System.out.print("Campos invalidos:");
        for (int i = 0; i < 12; i++) {
            if (camposInvalidos.get(i)) {
                System.out.print(" " + (i + 1));
            }
        }
        System.out.println();
        System.out.print("Seleccione el número del campo que desea modificar: ");
        String opcion = entrada.nextLine();

        System.out.print("\033[H\033[2J"); System.out.flush(); // <<-- BORRA LA TERMINAL (ANSI)

        switch (opcion.toLowerCase()) {
            case "1":
                 System.out.print("Nuevo apellido: ");
                 String nuevoApellido = entrada.nextLine();
                 datosModificados.getDatos().getDatos_personales().setApellido(nuevoApellido);
                 if(Validador.isApellidoValido(nuevoApellido)){
                     camposInvalidos.clear(0);
                 } else {
                     camposInvalidos.set(0);
                 }
                 break;
            case "2":
                 System.out.print("Nuevo nombre: ");
                 String nuevoNombre = entrada.nextLine();
                 datosModificados.getDatos().getDatos_personales().setNombre(nuevoNombre);
                 if(Validador.isNombreValido(nuevoNombre)){
                     camposInvalidos.clear(1);
                 } else {
                     camposInvalidos.set(1);
                 }
                 break;
            case "3":
                 System.out.print("Nuevo tipo de documento: ");
                 TipoDoc nuevoTipoDoc = menuTipoDoc();
                 datosModificados.getDatos().getDatos_personales().setTipoDoc(nuevoTipoDoc);
                 if(Validador.isTipoDocumentoValido(nuevoTipoDoc)){
                     camposInvalidos.clear(2);
                 } else {
                     camposInvalidos.set(2);
                 }
                 break;
            case "4":
                 System.out.print("Nuevo número de documento: ");
                 String nuevoNroDoc = entrada.nextLine();
                 datosModificados.getDatos().getDatos_personales().setNroDoc(nuevoNroDoc);
                 if(Validador.isNumeroDocumentoValido(nuevoNroDoc, datosModificados.getDatos().getDatos_personales().getTipoDoc())){
                     camposInvalidos.clear(3);
                 } else {
                     camposInvalidos.set(3);
                 }
                 break;
            case "5":
                 System.out.print("Nuevo CUIT (sin guiones ni espacios): ");
                 String nuevoCuit = entrada.nextLine();
                 datosModificados.getDatos().getDatos_personales().setCUIT(nuevoCuit);
                 if(Validador.isCuitValidoOpcional(nuevoCuit)){
                     camposInvalidos.clear(4);
                 } else {
                     camposInvalidos.set(4);
                 }
                 break;
            case "6":
                 System.out.print("Nueva posición frente al IVA: ");
                 String nuevaPosIva = entrada.nextLine();
                 datosModificados.getDatos().getDatos_personales().setPosicionIva(nuevaPosIva);
                 if(Validador.isPosicionIvaValida(nuevaPosIva)){
                     camposInvalidos.clear(5);
                 } else {
                     camposInvalidos.set(5);
                 }
                 break;
            case "7":
                 System.out.print("Nueva fecha de nacimiento (AAAA-MM-DD): ");
                 String nuevaFechaStr = entrada.nextLine();
                 LocalDate nuevaFecha = LocalDate.parse(nuevaFechaStr);
                 datosModificados.getDatos().getDatos_personales().setFechanac(nuevaFecha);
                    if(Validador.isFechaNacimientoValida(nuevaFecha)){
                        camposInvalidos.clear(6);
                    } else {
                        camposInvalidos.set(6);
                    }
                 break;
            case "8":
                 System.out.println("1. Nueva calle, 2. Nuevo número, 3. Nuevo piso, 4. Nuevo departamento, 5. Nueva localidad, 6. Nueva provincia, 7. Nuevo país, 8. Nuevo código postal ");
                 System.out.print("Campos invalidos:");
                 for (int i = 0; i < 8; i++) {
                     if (camposDireccionInvalidos.get(i)) {
                         System.out.print(" " + (i + 1));
                     }
                 }
                 System.out.println();
                 System.out.print("Seleccione el número del campo que desea modificar: ");
                 opcion = entrada.nextLine();
                 switch (opcion) {
                    case "1":
                       System.out.print("Nueva calle: ");
                       String nuevaCalle = entrada.nextLine();
                       datosModificados.getDatos().getDatos_residencia().setCalle(nuevaCalle);
                       if(Validador.isCalleValida(nuevaCalle)){
                           camposDireccionInvalidos.clear(0);
                       } else {
                           camposDireccionInvalidos.set(0);
                       }
                       break;
                    case "2":
                       System.out.print("Nuevo número: ");
                       String nuevoNumero = entrada.nextLine();
                       datosModificados.getDatos().getDatos_residencia().setNro_calle(nuevoNumero);
                       if(Validador.isNumeroCalleValido(nuevoNumero)){
                           camposDireccionInvalidos.clear(1);
                       } else {
                           camposDireccionInvalidos.set(1);
                       }
                       break;
                    case "3":
                       System.out.print("Nuevo piso: ");
                       String nuevoPiso = entrada.nextLine();
                       datosModificados.getDatos().getDatos_residencia().setPiso(nuevoPiso);
                       if(Validador.isNumeroCalleValido(nuevoPiso)){
                           camposDireccionInvalidos.clear(2);
                       } else {
                           camposDireccionInvalidos.set(2);
                       }
                       break;
                    case "4":
                       System.out.print("Nuevo departamento: ");
                       String nuevoDepto = entrada.nextLine();
                       datosModificados.getDatos().getDatos_residencia().setDepto(nuevoDepto);
                       if(Validador.isNumeroCalleValido(nuevoDepto)){
                           camposDireccionInvalidos.clear(3);
                       } else {
                           camposDireccionInvalidos.set(3);
                       }
                       break;
                    case "5":
                       System.out.print("Nueva localidad: ");
                       String nuevaLocalidad = entrada.nextLine();
                       datosModificados.getDatos().getDatos_residencia().setLocalidad(nuevaLocalidad);
                       if(Validador.isLocalidadValida(nuevaLocalidad)){
                           camposDireccionInvalidos.clear(4);
                       } else {
                           camposDireccionInvalidos.set(4);
                       }
                       break;
                    case "6":
                       System.out.print("Nueva provincia: ");
                       String nuevaProvincia = entrada.nextLine();
                       datosModificados.getDatos().getDatos_residencia().setProv(nuevaProvincia);
                       if(Validador.isProvinciaValida(nuevaProvincia)){
                           camposDireccionInvalidos.clear(5);
                       } else {
                           camposDireccionInvalidos.set(5);
                       }
                       break;
                    case "7":
                       System.out.print("Nuevo país: ");
                       String nuevoPais = entrada.nextLine();
                       datosModificados.getDatos().getDatos_residencia().setPais(nuevoPais);
                       if(Validador.isPaisValido(nuevoPais)){
                           camposDireccionInvalidos.clear(6);
                       } else {
                           camposDireccionInvalidos.set(6);
                       }
                       break;
                    case "8":
                       System.out.print("Nuevo código postal: ");
                       String nuevoCodPost = entrada.nextLine();
                       datosModificados.getDatos().getDatos_residencia().setCod_post(nuevoCodPost);
                       if(Validador.isCodigoPostalValido(nuevoCodPost)){
                           camposDireccionInvalidos.clear(7);
                       } else {
                           camposDireccionInvalidos.set(7);
                       }
                       break;
                 }
                 if (camposDireccionInvalidos.isEmpty()){
                     camposInvalidos.clear(7);
                 } else {
                     camposInvalidos.set(7);
                 }
                 break;
            case "9":
                 System.out.print("Nuevo teléfono: ");
                 String nuevoTelefono = entrada.nextLine();
                 datosModificados.getDatos().getDatos_contacto().setTelefono(nuevoTelefono);
                 if(Validador.isTelefonoValido(nuevoTelefono)){
                     camposInvalidos.clear(8);
                 } else {
                     camposInvalidos.set(8);
                 }
                 break;
            case "10":
                 System.out.print("Nuevo email: ");
                 String nuevoEmail = entrada.nextLine();
                 datosModificados.getDatos().getDatos_contacto().setEmail(nuevoEmail);
                    if(Validador.isEmailValidoOpcional(nuevoEmail)){
                        camposInvalidos.clear(9);
                    } else {
                        camposInvalidos.set(9);
                    }
                 break;
            case "11":
                 System.out.print("Nueva ocupación: ");
                 String nuevaOcupacion = entrada.nextLine();
                 datosModificados.getDatos().getDatos_personales().setOcupacion(nuevaOcupacion);
                 if(Validador.isOcupacionValida(nuevaOcupacion)){
                     camposInvalidos.clear(10);
                 } else {
                     camposInvalidos.set(10);
                 }
                 break;
            case "12":
                 System.out.print("Nueva nacionalidad: ");
                 String nuevaNacionalidad = entrada.nextLine();
                 datosModificados.getDatos().getDatos_personales().setNacionalidad(nuevaNacionalidad);
                 if(Validador.isNacionalidadValida(nuevaNacionalidad)){
                     camposInvalidos.clear(11);
                 } else {
                     camposInvalidos.set(11);
                 }
                 break;
            case "siguiente":
            case "s":
                 if (camposInvalidos.isEmpty()) {
                     //System.out.println("Los datos del huésped han sido guardados correctamente.");

                /*if(auxDTO.buscarPorDNI(nuevoNroDoc,nuevoNroDoc)){
                    String boton2= "-1";
                    while(!(boton2.equals("1")||boton2.equals("2"))){
                        System.out.println("\n¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
                        System.out.print("¿Desea ACEPTAR IGUALMENTE (1) o CORREGIR (2)? ");
                        boton2= entrada.nextLine();
                    }
                    if(boton2.equals("1")) {
                        //guardo datos con dni repettido
                        System.out.println("Los datos del huésped han sido guardados correctamente.");
                    }
                 else {
                    camposInvalidos.set(4);
                 }
             }
             else{
                //guardar datos
                System.out.println("Los datos del huésped han sido guardados correctamente.");
             }
            }
             else {
                     System.out.println("No se pueden guardar los cambios. Hay campos inválidos.");
                 */
                 bandera = false; //sale del bucle principal y el CU termina
                 }
                 break;
            case "cancelar":
            case "c":
                String boton3="-1";
                while(!(boton3.equals("1")||boton3.equals("2"))){

                   System.out.println("¿Desea cancelar el alta de huesped?");
                   System.out.println("SI (1) o NO (2) ");
                   boton3=entrada.nextLine();
                }
                if(boton3.equals("1")){
                  bandera=false;//sale del bucle principal y el CU termina
                }
                //else vuelve al menu

             break;
            case "borrar":
            case "b":
                 System.out.println("Los datos del huésped serán eliminados del sistema.");

       }


}

}//modificarHuesped

    // Esto debería hacerlo la interfaz
    private void listaDatosHuesped(Alojado alojado){
     System.out.println("Datos del Huésped:\n" +
     "1. Apellido:" + alojado.getDatos().getDatos_personales().getApellido() + "\n" +
       "2. Nombre:" + alojado.getDatos().getDatos_personales().getNombre() + "\n" +
       "3. Tipo de Documento:" + alojado.getDatos().getDatos_personales().getTipoDoc() + "\n" +
       "4. Número de Documento:" + alojado.getDatos().getDatos_personales().getNroDoc() + "\n" +
       "5. Cuit:" + alojado.getDatos().getDatos_personales().getCUIT() + "\n" +
       "6. Posición frente al IVA:" + alojado.getDatos().getDatos_personales().getPosicionIva() + "\n" +
       "7. Fecha de Nacimiento:" + alojado.getDatos().getDatos_personales().getFechanac() + "\n" +
       "8. Dirección:\n" +
       "  Calle:" + alojado.getDatos().getDatos_residencia().getCalle() + "\n" +
       "  Número:" + alojado.getDatos().getDatos_residencia().getNro_calle() + "\n" +
       "  Piso:" + alojado.getDatos().getDatos_residencia().getPiso() + "\n" +
       "  Departamento:" + alojado.getDatos().getDatos_residencia().getDepto() + "\n" +
       "  Localidad:" + alojado.getDatos().getDatos_residencia().getLocalidad() + "\n" +
       "  Provincia:" + alojado.getDatos().getDatos_residencia().getProv() + "\n" +
       "  País:" + alojado.getDatos().getDatos_residencia().getPais() + "\n" +
       "  Código Postal:" + alojado.getDatos().getDatos_residencia().getCod_post() + "\n" +
       "9. Teléfono:" + alojado.getDatos().getDatos_contacto().getTelefono() + "\n" +
       "10. Email:" + alojado.getDatos().getDatos_contacto().getEmail() + "\n" +
       "11. Ocupación:" + alojado.getDatos().getDatos_personales().getOcupacion() + "\n" +
       "12. Nacionalidad:" + alojado.getDatos().getDatos_personales().getNacionalidad() + "\n");
 }

    private ResumenHistorialHuesped huespedSeAlojo(CriteriosBusq criterios){
        // Logica de "huesped se alojó"

        AlojadoDAOJSON DAO = new AlojadoDAOJSON();
        List<AlojadoDTO> listaDTO = DAO.buscarHuespedDAO(criterios);

        if(listaDTO == null || listaDTO.isEmpty()){
            return ResumenHistorialHuesped.NO_PERSISTIDO;
        }

        AlojadoDTO huespedBaja = listaDTO.getFirst();

        // estos dos ifs se pueden hacer excepcion
        if(huespedBaja == null) {
            return ResumenHistorialHuesped.NO_PERSISTIDO;
        }

        // Si tiene algun check-in, el huesped se alojó
        if(huespedBaja.getId_check_in().isEmpty() && huespedBaja.getId_check_out().isEmpty()){
            return ResumenHistorialHuesped.SE_ALOJO;
        }
        return ResumenHistorialHuesped.NO_SE_ALOJO;
    }

    private void eliminarAlojado(Alojado alojado){
        AlojadoDAOJSON DAO = new AlojadoDAOJSON();
        AlojadoDTO eliminar = new AlojadoDTO(alojado);
        DAO.eliminarAlojado(eliminar);
    }

    /**
     * Enumerador ResumenHistorialHuesped  informa el estado del huesped en el sistema
     */
    enum ResumenHistorialHuesped{
        /**
         * Tuvo alguna estadia en el hotel
         * */
        SE_ALOJO,
        /**
         * No tuvo ninguna estadia pero sus datos estan persistidos*/
        NO_SE_ALOJO,
        /**
         * Sus datos no estan presentes en la base del sistema
         */
        NO_PERSISTIDO
    }

    public void darDeBajaHuesped(Alojado alojado){
     /*
        Se lo llama desde el CU13 al presionar BORRAR y se le pasa una instancia RespPago
        Si el huesped alguna vez se quedó en el hotel, no puede borrarse
        Se buscan coincidencias entre CUIT y Alojados. Se muestra en pantalla:
        “El huésped no puede ser eliminado, pues se ha alojado en el Hotel en alguna oportunidad. PRESIONE CUALQUIER TECLA PARA CONTINUAR…”
        Si la lista de encontrados isEmpty, se muestra:
        “Los datos del huésped <nombre> y <apellido>, <tipoDeDoc> y <nroDeDoc> serán eliminados del sistema. PRESIONE CUALQUIER TECLA PARA CONTINUAR…”
        Se muestran dos botones: “ELIMINAR” y “CANCELAR”. En ambos el CU termina
       */

        CriteriosBusq criterios = new CriteriosBusq();

        var nombre=alojado.getDatos().getDatos_personales().getNombre();
        var apellido=alojado.getDatos().getDatos_personales().getApellido();
        var nroDoc=alojado.getDatos().getDatos_personales().getNroDoc();
        var tipoDoc=alojado.getDatos().getDatos_personales().getTipoDoc();
        cargar_criterios(nombre, apellido, tipoDoc, nroDoc, criterios);

        InterfazDarBaja IO = new InterfazDarBaja();

        ResumenHistorialHuesped seAlojo = huespedSeAlojo(criterios);

        // Flujo secundario de CU, el huesped se alojó o no existe en la base
        if(seAlojo==ResumenHistorialHuesped.SE_ALOJO){
            IO.noSePuedeDarBaja();
            return;
        } else if(seAlojo==ResumenHistorialHuesped.NO_PERSISTIDO) {
            IO.noExisteHuesped(criterios);
            return;
        }

        // El usuario desea cancelar la baja
        if(IO.avisoBajaAlojado(criterios) == InterfazDarBaja.BajaCliente.CANCELAR){
            return;
        }

        eliminarAlojado(alojado);
        IO.terminarCU(criterios);
    }

}

