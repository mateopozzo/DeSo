package ddb.deso.alojamiento;
import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.JSON.AlojadoDAOJSON;
import ddb.deso.presentacion.InterfazDarBaja;

import java.nio.MappedByteBuffer;
import java.time.LocalDate;
import java.util.*;

// @author mat

public class GestorAlojamiento {
    private final AlojadoDAO alojadoDAO;
    private List<Huesped> huespedes = new LinkedList<>();

    /*
    Inyección de dependencias porque si no no me deja importar el metodo del DAO
    Inyección por constructor: final, la dependencia es explícita, ayuda al testing
    */

    public GestorAlojamiento() {}

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

       Scanner entrada = new Scanner(System.in);
       boolean b=true;
       while(b){
       System.out.println("Apellido:");
       String apellido = entrada.nextLine();

       System.out.println("Nombre:");
       String nombre = entrada.nextLine();

       // Menú para elegir tipo de documento
       TipoDoc tipoDoc = menuTipoDoc();

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

       } // while


    } // metodo

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
        System.out.println("Hotel Premier - Buscar huésped ----------------------------------------------------------------------------");
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
            darDeAltaHuesped();
            // FIN DE CASO DE USO
        }
        else {
            System.out.println("Ingrese el número de huesped que desea seleccionar.");
            System.out.println("Si no desea seleccionar alguno, presione ENTER y luego 1. (SIGUIENTE)");
            input_user = scanner.nextLine();

            // Si presionó ENTER, entonces voy a dar de alta huesped
            if (input_user.trim().isEmpty()) {
                System.out.println("Presione 1 para SIGUIENTE");
                String siguiente = scanner.nextLine();
                if ("1".equals(siguiente)) {
                    darDeAltaHuesped();
                    // FIN DE CASO DE USO
                }
            }
            // Si presionó un número, entonces voy a modificar huesped
            else {
                try {
                    seleccion = Integer.parseInt(input_user);
                    // Si la selección está dentro del rango, lo busco en mi lista
                    if (seleccion<=encontrados.size() && seleccion>0) {
                        huesped_seleccionado = encontrados.get(seleccion-1);
                        System.out.println("Huesped seleccionado con éxito.");
                        if (huesped_seleccionado != null){
                            // modificarHuesped(huesped_seleccionado);
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
        };

        scanner.close();
        return tipoDoc;
    }

    private AlojadoDTO encontrarPrimerOcurrenciaDTO(List<AlojadoDTO> listaDTO, AlojadoDTO comparadorAlojadoDTO){
        if(listaDTO.isEmpty()){
            return null;
        } else {
            return  listaDTO.stream()
                    .filter(dto -> dto.equals(comparadorAlojadoDTO))
                    .findFirst()
                    .orElse(null);
        }
    }

    private boolean huespedSeAlojo(CriteriosBusq criterios, Alojado alojado){
        /*
         * Logica de "huesped se alojó"
         * */
        AlojadoDAOJSON DAO = new AlojadoDAOJSON();
        List<AlojadoDTO> listaDTO = DAO.buscarHuespedDAO(criterios);
        // Esta variable puede existir o no, depende del caso de uso 10
        AlojadoDTO comparadorAlojadoDTO = new AlojadoDTO(alojado);
        alojado.completarDTO(comparadorAlojadoDTO);
        AlojadoDTO huespedBaja = encontrarPrimerOcurrenciaDTO(listaDTO, comparadorAlojadoDTO);

        // estos dos ifs se pueden hacer excepcion
        if(huespedBaja == null) {
            System.out.println("No se ha encontrado el alojado.");
            return false;
        }
        if(huespedBaja.getId_check_in().isEmpty() && huespedBaja.getId_check_out().isEmpty()){
            return false;
        }
        return true;
    }

    private void eliminarAlojado(Alojado alojado){
        AlojadoDAOJSON DAO = new AlojadoDAOJSON();
        AlojadoDTO eliminar = new AlojadoDTO(alojado);
        DAO.eliminarAlojado(eliminar);
    }

    public void darDeBajaHuesped(Alojado alojado){

        CriteriosBusq criterios = new CriteriosBusq();

        var nombre=alojado.getDatos().getDatos_personales().getNombre();
        var apellido=alojado.getDatos().getDatos_personales().getApellido();
        var nroDoc=alojado.getDatos().getDatos_personales().getNroDoc();
        var tipoDoc=alojado.getDatos().getDatos_personales().getTipoDoc();
        cargar_criterios(nombre, apellido, tipoDoc, nroDoc, criterios);

        boolean seAlojo = huespedSeAlojo(criterios, alojado);

        InterfazDarBaja IO = new InterfazDarBaja();
        if(seAlojo){
            IO.noSePuedeDarBaja();
        }
        if(IO.avisoBajaAlojado(criterios) == InterfazDarBaja.BajaCliente.CANCELAR){
            return;
        }

        eliminarAlojado(alojado);

        IO.terminarCU(criterios);
    }

}

