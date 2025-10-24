   /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ddb.deso.alojamiento;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

import ddb.deso.TipoDoc;
/**
 *
 * @author mat
 */
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
        switch (opcion) {
            case "1": return TipoDoc.DNI;
            case "2": return TipoDoc.CI;
            case "3": return TipoDoc.LE;
            case "4": return TipoDoc.PASAPORTE;
            case "5": return TipoDoc.OTRO;
            default:
               return null;
        }
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

       System.out.println("Cuit:");
       String cuit = entrada.nextLine(); //no obligatorio

       System.out.println("Posicion frente al IVA:");
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

       System.out.println("Direccion:");
       System.out.println("Calle:");
       String calle = entrada.nextLine();

       System.out.println("Numero:");
       String numero = entrada.nextLine();

       System.out.println("Departamento:");
       String departamento = entrada.nextLine();

       System.out.println("Piso:");
       String piso = entrada.nextLine();

       System.out.println("Codigo:");
       String codigo = entrada.nextLine();

       System.out.println("postal:");
       String postal = entrada.nextLine();

       System.out.println("Localidad:");
       String localidad = entrada.nextLine();

       System.out.println("Provincia:");
       String provincia = entrada.nextLine();

       System.out.println("Pais:");
       String pais = entrada.nextLine();



       System.out.println("Telefono:");
       String telefono = entrada.nextLine();

       System.out.println("Email:");
       String email = entrada.nextLine(); //no obligatorio

       System.out.println("Ocupacion:");
       String ocupacion = entrada.nextLine();

       System.out.println("Nacionalidad");
       String nacionalidad = entrada.nextLine();

       //ver si lo pongo dsp e comprobar si estan vacios
       DatosResidencia dr= new DatosResidencia(calle,departamento,localidad,provincia,pais,numero,piso,postal);

       //Huesped h=new Huesped(null);
       String boton="-1";

          while(!(boton.equals("1")||boton.equals("2"))){
             System.out.println("Presione 1 para SIGUIENTE o 2 para CANCELAR" );
             boton=entrada.nextLine();

             if(boton.equals("2")){

                String boton3="-1";
                while(!(boton3.equals("1")||boton3.equals("2"))){

                   System.out.println("¿Desea canclear el alta de huesped?");
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


    }//metodo

    public void buscarHuesped (CriteriosBusq criterios_busq){
        /* Recibe los paŕametros de búsqueda en criterios (String apellido, String nombre, TipoDoc tipoDoc, String nroDoc) y busca sobre los JSON
        Cuando los encuentra, los va colando en encontrados
        Si no encuentra coincidencias, encontrados is empty y se ejecuta darDeAltaHuesped() -> Fin CU

        Si encuentra, se muestra del 1 al inf la cantidad de coincidencias
        Usuario ingresa opción input_user y se parsea a un int seleccion
        Se busca en la lista quien es el huesped seleccion+1 y se almacena
        Se llama a modificarHuesped() con la instancia de huesped_seleccionado -> Fin CU
        */

        List<Huesped> encontrados;
        encontrados = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        Huesped huesped_seleccionado;
        String input_user;
        int seleccion=-1;

        // BUSQUEDA EN JSON -> Llamo al DAO -> Busca en la BDD -> Crea los DTO -> DTO devuelve lista a DAO -> DAO devuelve lista a gestor

        if (encontrados.isEmpty()) {
            System.out.println("No se encontraron coincidencias de búsqueda.");
            scanner.close();
            darDeAltaHuesped();
            // FIN DE CASO DE USO
        }
        else {
            input_user = scanner.nextLine();
            seleccion = Integer.parseInt(input_user);

            // Control de selección válida, loopea hasta que haya seleccionado un nro. de la grilla
            while (seleccion<0 || seleccion>encontrados.size()) {
                System.out.println("La opción no es válida. Intente nuevamente.");
                input_user = scanner.nextLine();
                seleccion = Integer.parseInt(input_user);
            }

            scanner.close();
        }

        if (seleccion<=encontrados.size() && seleccion>0) {
            huesped_seleccionado = encontrados.get(seleccion-1);
            System.out.println("Huesped seleccionado con éxito.");

            if (huesped_seleccionado != null){
                modificarHuesped(huesped_seleccionado);
                // FIN DE CASO DE USO
            }
        }

    }

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
        if (no_es_vacio(nroDoc_b) && !tipoDoc_h.equals(tipoDoc_b)) {
            return false;
        }

        return true;
    }

    private boolean no_es_vacio (String contenido){
        boolean flag = (contenido==null || contenido.isEmpty());
        return !flag;
    }

}

