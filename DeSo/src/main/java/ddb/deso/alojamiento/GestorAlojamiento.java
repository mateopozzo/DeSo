package ddb.deso.alojamiento;
import static java.lang.Integer.parseInt;
import java.time.LocalDate;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.almacenamiento.JSON.AlojadoDAOJSON;
import ddb.deso.presentacion.InterfazBusqueda;
import ddb.deso.presentacion.InterfazDarBaja;


public class GestorAlojamiento {
    private static AlojadoDAO alojadoDAO;
    private List<Huesped> huespedes = new LinkedList<>();

    /*
    Inyección de dependencias porque si no no me deja importar el metodo del DAO
    Inyección por constructor: final, la dependencia es explícita, ayuda al testing
    */

//    public GestorAlojamiento() {}

    public GestorAlojamiento(AlojadoDAO alojadoDAO) {
        GestorAlojamiento.alojadoDAO = alojadoDAO;
    }

   public boolean dniExiste(String dni, TipoDoc tipo) {
    if (tipo == null || dni == null || dni.isBlank()) {
        return false; // o lanzar IllegalArgumentException según la política del proyecto
    }
    CriteriosBusq criterios_busq = new CriteriosBusq(null, null, tipo, dni);
    List<AlojadoDTO> encontrados = alojadoDAO.buscarHuespedDAO(criterios_busq);
    return encontrados != null && !encontrados.isEmpty();
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
            default:
                //datosModificados = cargarCampo(datosModificados, opcion, camposInvalidos, camposDireccionInvalidos);
            break;


       }
       System.out.print("\033[H\033[2J"); System.out.flush(); // <<-- BORRA LA TERMINAL (ANSI)
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

    public static void buscarHuesped(CriteriosBusq criterios_busq){
        /* Recibe los paŕametros de búsqueda en criterios_busq (String apellido, String nombre, TipoDoc tipoDoc, String nroDoc)
        Llama al DAO, que llama a DAOJSON y busca todos los alojados
        Cuando los encuentra, crea un DTO y los va colando en una lista "encontrados"
        Si no encuentra coincidencias, encontrados is empty y se ejecuta darDeAltaHuesped() -> Fin CU

        Si encuentra, se muestra de 1 al n la cantidad de coincidencias
        Usuario ingresa opción input_user y se parsea a un int seleccion
        Se busca en la lista quien es el huesped seleccion-1 y se almacena
        Se llama a modificarHuesped() con la instancia de huesped_seleccionado -> Fin CU
        */

        InterfazBusqueda ui = new InterfazBusqueda();
        List<AlojadoDTO> encontrados;

        // BÚSQUEDA EN JSON: Llamo al DAO -> Busca en la BDD -> Crea los DTO -> Devuelve lista de DTO a DAO -> DAO devuelve lista a gestor
        encontrados = alojadoDAO.buscarHuespedDAO(criterios_busq);

        if (encontrados.isEmpty()) {
            ui.sin_coincidencias();
            // FIN DE CASO DE USO
        }
        else {
            ui.seleccion(encontrados);
        }
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
            case "2" -> tipoDoc =TipoDoc.LC;
            case "3" -> tipoDoc =TipoDoc.LE;
            case "4" -> tipoDoc =TipoDoc.PASAPORTE;
            case "5" -> tipoDoc =TipoDoc.OTRO;
            default -> tipoDoc = TipoDoc.DNI;
        }
        scanner.close();
        return tipoDoc;
    }

    // Modificar huesped debería modificar alojados, no huesped (también puede modificar invitados)
    public void modificarHuesped(Alojado alojado){
        // creo un huesped auxiliar para modificar
      Alojado datosModificados = alojado;
      AlojadoDTO datosOriginalesDTO = new AlojadoDTO(alojado);
      AlojadoDTO alojadoDTO;
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
                case "siguiente":
                case "s":
                    if (camposInvalidos.isEmpty()) {
                        if( !dniExiste(datosModificados.getDatos().getDatos_personales().getNroDoc(), datosModificados.getDatos().getDatos_personales().getTipoDoc()) ){
                            //guardo datos modificados
                            alojadoDTO = new AlojadoDTO(datosModificados);
                            alojadoDAO.actualizarAlojado(datosOriginalesDTO, alojadoDTO);
                            System.out.print("Los datos del huésped han sido modificados correctamente.");
                            bandera=false;//sale del bucle principal y el CU termina
                        } else {
                            System.out.println("\n¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
                            String boton2= "-1";
                            while(!(boton2.equals("1")||boton2.equals("2"))){
                                System.out.print("¿Desea ACEPTAR IGUALMENTE (1) o CORREGIR (2)? ");
                                boton2= entrada.nextLine();
                            }
                            if(boton2.equals("1")) {
                                //guardo datos con dni repettido
                                alojadoDTO = new AlojadoDTO(datosModificados);
                                alojadoDAO.actualizarAlojado(datosOriginalesDTO, alojadoDTO);
                                System.out.print("Los datos del huésped han sido modificados correctamente.");
                                bandera=false;//sale del bucle principal y el CU termina
                            }
                            else {
                                camposInvalidos.set(3); // marco nro doc como invalido
                            }
                        }
                        bandera=false;//sale del bucle principal y el CU termina
                    }
                break;
                case "cancelar":
                case "c":
                    String boton3="-1";
                    while(!(boton3.equals("1")||boton3.equals("2"))){

                       System.out.println("¿Desea cancelar la modificación del huésped?");
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
                    darDeBajaHuesped(alojado);
                    bandera=false;//sale del bucle principal y el CU termina
                    break;
                default:
                    datosModificados = cargarCampo(datosModificados, opcion, camposInvalidos, camposDireccionInvalidos);
                break;
        }
            System.out.print("\033[H\033[2J"); System.out.flush(); // <<-- BORRA LA TERMINAL (ANSI)
        }
        System.out.print("El caso de uso 10 termina");
        entrada.close();
    }//modificarHuesped

    //esta funcion es para el cu9 y cu10. contien la logica que comparten los casos de uso para ir cargando los campos
    private Alojado cargarCampo(Alojado alojado, String opcion, BitSet camposInvalidos, BitSet camposDireccionInvalidos){
        Alojado datosModificados = alojado;
      Scanner entrada = new Scanner(System.in);

        switch (opcion){
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

        }
        entrada.close();
        return datosModificados;

    }

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

        var nombre=alojado.getDatos().getDatos_personales().getNombre();
        var apellido=alojado.getDatos().getDatos_personales().getApellido();
        var nroDoc=alojado.getDatos().getDatos_personales().getNroDoc();
        var tipoDoc=alojado.getDatos().getDatos_personales().getTipoDoc();
        CriteriosBusq criterios = new CriteriosBusq(nombre, apellido, tipoDoc, nroDoc);

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

