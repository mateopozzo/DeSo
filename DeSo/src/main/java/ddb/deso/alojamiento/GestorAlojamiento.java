package ddb.deso.alojamiento;
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
    private static final AlojadoDAO alojadoDAO = new AlojadoDAOJSON();
    private List<Huesped> huespedes = new LinkedList<>();

    /*
    Inyección de dependencias porque si no no me deja importar el metodo del DAO
    Inyección por constructor: final, la dependencia es explícita, ayuda al testing
    */

    public static boolean dniExiste(String dni, TipoDoc tipo) {
        if (tipo == null || dni == null || dni.isBlank()) {
            return false; // o lanzar IllegalArgumentException según la política del proyecto
        }
        CriteriosBusq criterios_busq = new CriteriosBusq(null, null, tipo, dni);
        List<AlojadoDTO> encontrados = alojadoDAO.buscarHuespedDAO(criterios_busq);
        return encontrados != null && !encontrados.isEmpty();
    }


    public void darDeAltaHuesped() {

        Huesped datosModificados = new Huesped(null);
        Scanner entrada = new Scanner(System.in);
        boolean bandera = true;
        BitSet camposInvalidos = new BitSet(12); // BitSet para indicar que campos son invalidos
        BitSet camposDireccionInvalidos = new BitSet(8); // BitSet para indicar que campos de direccion son invalidos
        camposInvalidos.set(0, 12); // Inicializo todos los bits en falso
        camposDireccionInvalidos.set(0, 8); // Inicializo todos los bits en falso

        while (bandera) {
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

            System.out.print("\033[H\033[2J");
            System.out.flush(); // <<-- BORRA LA TERMINAL (ANSI)
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
                                datosModificados.datos.getDatos_personales().getApellido() + " se ha cargado correctamente.");
                        String boton4 = "-1";
                        while (!(boton4.equals("1") || boton4.equals("2"))) {
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
                    String boton3 = "-1";
                    while (!(boton3.equals("1") || boton3.equals("2"))) {

                        System.out.println("¿Desea cancelar el alta de huesped?");
                        System.out.println("SI (1) o NO (2) ");
                        boton3 = entrada.nextLine();
                    }

                    if (boton3.equals("1")) {
                        bandera = false;//sale del bucle principal y el CU termina
                    }
                    //else vuelve al menu

                    break;
                default:
                    //datosModificados = cargarCampo(datosModificados, opcion, camposInvalidos, camposDireccionInvalidos);
                    break;


            }
            System.out.print("\033[H\033[2J");
            System.out.flush(); // <<-- BORRA LA TERMINAL (ANSI)
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

    // Esto debería hacerlo la interfaz -> cuando termine juli su cu plis borrar grax
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

    public static void buscarHuesped(CriteriosBusq criterios_busq) {
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
        } else {
            ui.seleccion(encontrados);
        }
    }   


    private static TipoDoc menuTipoDoc(Scanner scanner){
        System.out.println("Seleccione tipo de documento:");
        System.out.println("1. DNI");
        System.out.println("2. LE");
        System.out.println("3. LC");
        System.out.println("4. PASAPORTE");
        System.out.println("5. OTRO");

        TipoDoc tipoDoc;
        String opcion = scanner.nextLine();

        switch (opcion) {
            case "2" -> tipoDoc =TipoDoc.LC;
            case "3" -> tipoDoc =TipoDoc.LE;
            case "4" -> tipoDoc =TipoDoc.PASAPORTE;
            case "5" -> tipoDoc =TipoDoc.OTRO;
            default -> tipoDoc = TipoDoc.DNI;
        }
        return tipoDoc;
    }

    public static void modificarHuesped(Alojado alojadoOriginal, Alojado aljadoModificado){
        AlojadoDAO alojadoDAO = new AlojadoDAOJSON();
        AlojadoDTO datosOriginalesDTO = new AlojadoDTO(alojadoOriginal);
        AlojadoDTO datosModificadosDTO = new AlojadoDTO(aljadoModificado );
        alojadoDAO.actualizarAlojado(datosOriginalesDTO, datosModificadosDTO);
    }

    /*
     Verifica si un huésped, basado en ciertos criterios de búsqueda, se ha alojado alguna vez en el hotel.
     @param criterios Criterios de búsqueda (Nombre, Apellido, NroDoc, TipoDoc) para identificar al huésped.
     @return El estado del historial del huésped según {@link ResumenHistorialHuesped}.
     */
    private static ResumenHistorialHuesped huespedSeAlojo(CriteriosBusq criterios) {
        // Logica de "huesped se alojó"

        AlojadoDAOJSON DAO = new AlojadoDAOJSON();
        List<AlojadoDTO> listaDTO = DAO.buscarHuespedDAO(criterios);

        if (listaDTO == null || listaDTO.isEmpty()) {
            return ResumenHistorialHuesped.NO_PERSISTIDO;
        }

        AlojadoDTO huespedBaja = listaDTO.getFirst();

        // estos dos ifs se pueden hacer excepcion
        if (huespedBaja == null) {
            return ResumenHistorialHuesped.NO_PERSISTIDO;
        }

        // Si tiene algun check-in, el huesped se alojó
        if (huespedBaja.getId_check_in().isEmpty() && huespedBaja.getId_check_out().isEmpty()) {
            return ResumenHistorialHuesped.SE_ALOJO;
        }
        return ResumenHistorialHuesped.NO_SE_ALOJO;
    }

    /*
     Elimina el registro de un huésped de la base de datos (persistencia).
     Este metodo se llama solo si el huésped nunca se alojó en el hotel.

     @param alojado La instancia de {@code Alojado} a eliminar.
     */

    private static void eliminarAlojado(Alojado alojado) {
        AlojadoDAOJSON DAO = new AlojadoDAOJSON();
        AlojadoDTO eliminar = new AlojadoDTO(alojado);
        DAO.eliminarAlojado(eliminar);
    }

    /**
     * Enumerador ResumenHistorialHuesped  informa el estado del huesped en el sistema
     */
    enum ResumenHistorialHuesped {
        /**
         * Tuvo alguna estadia en el hotel
         */
        SE_ALOJO,
        /**
         * No tuvo ninguna estadia pero sus datos estan persistidos
         */
        NO_SE_ALOJO,
        /**
         * Sus datos no estan presentes en la base del sistema
         */
        NO_PERSISTIDO
    }

    /*
     Clase que implementa CU11: Dar de baja Huesped**
     Implementa el **CU11: Dar de baja Huesped**.
     <p>
     Elimina huésped de la base de datos si y solo si no tiene registros de alojamiento
     Verificaciones que realiza:
     <ul>
     <li>Verifica si el huésped tuvo estadías.</li>
     <li>Verifica si el huésped **no existe** en la base de datos.</li>
     </ul>
     Si ninguna de las condiciones anteriores se cumple, solicita **confirmación** al usuario antes de proceder
     con la eliminación. Si el usuario cancela la operación, el CU finaliza sin cambios.
     </p>

     @param alojado El objeto {@code Alojado} que contiene los datos del huésped que se desea dar de baja.
     @return void
     */
    public static void darDeBajaHuesped(Alojado alojado) {
        var nombre = alojado.getDatos().getDatos_personales().getNombre();
        var apellido = alojado.getDatos().getDatos_personales().getApellido();
        var nroDoc = alojado.getDatos().getDatos_personales().getNroDoc();
        var tipoDoc = alojado.getDatos().getDatos_personales().getTipoDoc();
        CriteriosBusq criterios = new CriteriosBusq(nombre, apellido, tipoDoc, nroDoc);

        InterfazDarBaja IO = new InterfazDarBaja();

        ResumenHistorialHuesped seAlojo = huespedSeAlojo(criterios);

        // Flujo secundario de CU, el huesped se alojó o no existe en la base
        if (seAlojo == ResumenHistorialHuesped.SE_ALOJO) {
            IO.noSePuedeDarBaja();
            return;
        } else if (seAlojo == ResumenHistorialHuesped.NO_PERSISTIDO) {
            IO.noExisteHuesped(criterios);
            return;
        }

        // El usuario desea cancelar la baja
        if (IO.avisoBajaAlojado(criterios) == InterfazDarBaja.BajaCliente.CANCELAR) {
            return;
        }

        eliminarAlojado(alojado);
        IO.terminarCU(criterios);
    }
}

