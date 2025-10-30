package ddb.deso.presentacion;
import java.time.LocalDate;
import java.util.BitSet;
import java.util.Scanner;

import ddb.deso.TipoDoc;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.GestorAlojamiento;
import ddb.deso.alojamiento.Validador;

public class InterfazModiHues {
    private Scanner entrada;
    private BitSet camposInvalidos;
    private BitSet camposDireccionInvalida;

    public InterfazModiHues(){
        this.entrada=new Scanner(System.in);
        this.camposInvalidos = new BitSet(12) ;
        this.camposDireccionInvalida = new BitSet(8);
    }

    public void ejecutarModiHuesped(Alojado alojadoOriginal){
        // PUNTO DE INGRESO PRINCIPAL -> LLAMADO DESDE LA LLAMADA MAIN O OTRO CASO DE USO
        System.out.println("Interfaz de modificación de huésped - En desarrollo");
        System.out.println("Este es un apartado especialmente para que el usuario" + "\n" +
                " pueda modificar o eliminar los datos de un huésped ya existente.");

        // Lógica de modificación de huésped aquí
        Alojado datosModificados = alojadoOriginal;
        boolean bandera= true;
        
        while(bandera){
            listaDatosHuesped(datosModificados);

            System.out.println();
            System.out.print("Seleccione el número del campo que desea modificar: ");
            String opcion = entrada.nextLine();

            System.out.print("\033[H\033[2J"); System.out.flush(); // <<-- BORRA LA TERMINAL (ANSI)

            switch (opcion.toLowerCase()) {
                case "siguiente":
                case "s":
                    if (camposInvalidos.isEmpty()) {
                        if( !GestorAlojamiento.dniExiste(datosModificados.getDatos().getDatos_personales().getNroDoc(), datosModificados.getDatos().getDatos_personales().getTipoDoc()) ){
                            //guardo datos modificados
                            System.out.println("Guardando cambios...");
                            GestorAlojamiento.modificarHuesped(alojadoOriginal, datosModificados);
                            System.out.println("Cambios guardados.");
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
                                GestorAlojamiento.modificarHuesped(alojadoOriginal, datosModificados);
                                System.out.print("Los datos del huésped han sido modificados correctamente.");
                                bandera=false;//sale del bucle principal y el CU termina
                            }
                            else {
                                camposInvalidos.set(3); // marco nro doc como invalido
                            }
                        }
                        //sale del bucle principal y el CU termina
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

                     //System.out.print("\033[H\033[2J"); System.out.flush(); // <<-- BORRA LA TERMINAL (ANSI)
                break;
                case "borrar":
                case "b":
                    GestorAlojamiento.darDeBajaHuesped(alojadoOriginal);
                    bandera=false;//sale del bucle principal y el CU termina
                    break;
                default:
                    datosModificados = cargarCampo(datosModificados, opcion, camposInvalidos, camposDireccionInvalida);
                     //System.out.print("\033[H\033[2J"); System.out.flush(); // <<-- BORRA LA TERMINAL (ANSI)
        }
           
        }
        System.out.print("El caso de uso 10 termina");

    }

    private TipoDoc menuTipoDoc(){
        System.out.println("Seleccione tipo de documento:");
        System.out.println("1. DNI");
        System.out.println("2. LE");
        System.out.println("3. LC");
        System.out.println("4. PASAPORTE");
        System.out.println("5. OTRO");

        TipoDoc tipoDoc;
        String opcion = entrada.nextLine();

        switch (opcion) {
            case "2" -> tipoDoc =TipoDoc.LE;
            case "3" -> tipoDoc =TipoDoc.LC;
            case "4" -> tipoDoc =TipoDoc.PASAPORTE;
            case "5" -> tipoDoc =TipoDoc.OTRO;
            default -> tipoDoc = TipoDoc.DNI;
        }
        return tipoDoc;
    }

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

            System.out.println("Escriba Siguiente (s), Cancelar (c) o Borrar (b)");
            System.out.print("Campos inválidos en: ");
            for (int i = 0; i < 12; i++) {
                if (camposInvalidos.get(i)) {
                    System.out.print(" " + (i + 1));
                }
            }
 }

    private Alojado cargarCampo(Alojado alojado, String opcion, BitSet camposInvalidos, BitSet camposDireccionInvalidos){

        Alojado datosModificados = alojado;

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
        //entrada.close();
        return datosModificados;

    }

}
