package ddb.deso.presentacion;

import java.time.LocalDate;
import java.util.BitSet;
import java.util.Scanner;

import ddb.deso.TipoDoc;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.CriteriosBusq;
import ddb.deso.alojamiento.DatosAlojado;
import ddb.deso.alojamiento.FactoryAlojado;
import ddb.deso.alojamiento.GestorAlojamiento;
import ddb.deso.alojamiento.Invitado;
import ddb.deso.alojamiento.Validador;
import ddb.deso.presentacion.InterfazModiHues;

public class InterfazDarAlta {
    private Scanner entrada;
    private BitSet camposInvalidos;
    private BitSet camposDireccionInvalida;

    public InterfazDarAlta(){
        this.entrada=new Scanner(System.in);
        this.camposInvalidos = new BitSet(12) ;
        this.camposDireccionInvalida = new BitSet(8);
    }

    public void ejecutarDarAlta(){

        camposInvalidos.set(0, 12); // Inicializo todos los bits en falso
        camposDireccionInvalida.set(0, 8); // Inicializo todos los bits en falso

        boolean bandera= true;
        DatosAlojado da= new DatosAlojado();
        Alojado nuevoAlojado = FactoryAlojado.create(FactoryAlojado.INVITADO,da);


        while(bandera){
            listaDatosHuesped(nuevoAlojado);
            System.out.println("Presione una opcion numérica o SIGUIENTE (s) CANCELAR (c) ");
            System.out.println();
            String opcion = entrada.nextLine();

            System.out.print("\033[H\033[2J"); System.out.flush(); // <<-- BORRA LA TERMINAL (ANSI)
            
            switch (opcion.toLowerCase()) {
                
                case "siguiente":
                case "s":
                    if (!camposInvalidos.isEmpty()) {
                        
                        muestraCamposValidos();
                        continue;
                    }
                    else{
                        String nro_doc = nuevoAlojado.getDatos().getDatos_personales().getNroDoc();
                        TipoDoc tipo_doc = nuevoAlojado.getDatos().getDatos_personales().getTipoDoc();
                        boolean existe_dni = GestorAlojamiento.dniExiste(nro_doc,tipo_doc);
                       
                        if(existe_dni){
                            System.out.println("\n¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
                            String boton2= "-1";
                            while(!(boton2.equals("1")||boton2.equals("2"))){
                                System.out.print("¿Desea ACEPTAR IGUALMENTE (1) o CORREGIR (2)? ");
                                boton2= entrada.nextLine();
                            }
                            
                           if(boton2.equals("2")) {
                                camposInvalidos.set(3); // marco nro doc como invalido
                                continue;
                            }
                  /*           else{
                                //una vez con la bdd no se invocan los casos de uso, se modifica directamente
                                CriteriosBusq cBusq=new CriteriosBusq("", "", tipo_doc, nro_doc)
                                Alojado alojadoAnterior= GestorAlojamiento.obtenerHuesped(null);
                                GestorAlojamiento.eliminarAlojado(alojadoAnterior);
                            }
                 */  
                     }
                        //guardo datos 
                        GestorAlojamiento.darDeAltaHuesped(nuevoAlojado);
                        System.out.print("El Huésped " +
                        nuevoAlojado.getDatos().getDatos_personales().getNombre() + " " +
                        nuevoAlojado.getDatos().getDatos_personales().getApellido() + 
                        " se ha cargado correctamente.");
                        bandera=false;    
                            String boton3= "-1";
                            while(!(boton3.equals("1")||boton3.equals("2"))){
                                System.out.print("¿Desea cargar otro? SI(1)/ NO(2) ");
                                boton3= entrada.nextLine();
                            }
                            if(boton3.equals("1")){
                                System.out.print("\033[H\033[2J"); System.out.flush(); // <<-- BORRA LA TERMINAL (ANSI)
                                ejecutarDarAlta();
                            }
                        //else sale el bucle principal y el CU termina 
                    }
                    break;
                case "cancelar":
                case "c":
                    String boton3="-1";
                    while(!(boton3.equals("1")||boton3.equals("2"))){

                        System.out.println("¿Desea cancelar  el alta del huésped?");
                        System.out.println("SI (1) o NO (2) ");
                        boton3=entrada.nextLine();
                    }
                    if(boton3.equals("1")){
                        bandera=false;//sale del bucle principal y el CU termina
                    }
                 default:
                  cargarCampo(nuevoAlojado, opcion, camposInvalidos, camposDireccionInvalida);
                     
            }
            System.out.print("\033[H\033[2J"); System.out.flush(); // <<-- BORRA LA TERMINAL (ANSI)
        
        }
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
    }
    
    private void muestraCamposValidos(){ 
    
        System.out.print("Vuelva a ingresar los datos. Campos inválidos en: ");
        for (int i = 0; i < 12; i++) {
            if (camposInvalidos.get(i)) {
                System.out.print(" " + (i + 1));
            }
         
        }
           System.out.println();
            if (camposInvalidos.get(7)){
                    System.out.print("Campos invalidos Direccion:");
                for (int j = 0; j < 8; j++) {
                    if (camposDireccionInvalida.get(j)) {
                        System.out.print(" " + (j + 1));
                    }
                   
                }
                 System.out.println();
                }
            }
        
    

    private Alojado cargarCampo(Alojado alojado, String opcion,
     BitSet camposInvalidos, BitSet camposDireccionInvalida){

        Alojado datosModificados = alojado;

        switch (opcion){
            case "1":
                System.out.print("Apellido: "); // Modificado de "Nuevo apellido: "
                String apellido = entrada.nextLine();
                datosModificados.getDatos().getDatos_personales().setApellido(apellido);
                if(Validador.isApellidoValido(apellido)){
                    camposInvalidos.clear(0);
                } else {
                    camposInvalidos.set(0);
                }
                break;
            case "2":
                System.out.print("Nombre: "); // Modificado de "Nuevo nombre: "
                String nombre = entrada.nextLine();
                datosModificados.getDatos().getDatos_personales().setNombre(nombre);
                if(Validador.isNombreValido(nombre)){
                    camposInvalidos.clear(1);
                } else {
                    camposInvalidos.set(1);
                }
                break;
            case "3":
                System.out.print("Tipo de documento: "); // Modificado de "Nuevo tipo de documento: "
                TipoDoc nuevoTipoDoc = menuTipoDoc();
                datosModificados.getDatos().getDatos_personales().setTipoDoc(nuevoTipoDoc);
                if(Validador.isTipoDocumentoValido(nuevoTipoDoc)){
                    camposInvalidos.clear(2);
                } else {
                    camposInvalidos.set(2);
                }
                break;
            case "4":
                System.out.print("Número de documento: "); // Modificado de "Nuevo número de documento: "
                String nroDoc = entrada.nextLine();
                datosModificados.getDatos().getDatos_personales().setNroDoc(nroDoc);
                if(Validador.isNumeroDocumentoValido(nroDoc, datosModificados.getDatos().getDatos_personales().getTipoDoc())){
                    camposInvalidos.clear(3);
                } else {
                    camposInvalidos.set(3);
                }
                break;
            case "5":
                System.out.print("CUIT (sin guiones ni espacios): "); // Modificado de "Nuevo CUIT (sin guiones ni espacios): "
                String cuit = entrada.nextLine();
                datosModificados.getDatos().getDatos_personales().setCUIT(cuit);
                if(Validador.isCuitValidoOpcional(cuit)){
                    camposInvalidos.clear(4);
                } else {
                    camposInvalidos.set(4);
                }
                break;
            case "6":
                System.out.print("Posición frente al IVA: "); // Modificado de "Nueva posición frente al IVA: "
                String posIva = entrada.nextLine();
                datosModificados.getDatos().getDatos_personales().setPosicionIva(posIva);
                if(Validador.isPosicionIvaValida(posIva)){
                    camposInvalidos.clear(5);
                } else {
                    camposInvalidos.set(5);
                }
                break;
            case "7":
                System.out.print("Fecha de nacimiento (AAAA-MM-DD): "); // Modificado de "Nueva fecha de nacimiento (AAAA-MM-DD): "
                String fechaStr = entrada.nextLine();
                LocalDate nuevaFecha = LocalDate.parse(fechaStr);
                datosModificados.getDatos().getDatos_personales().setFechanac(nuevaFecha);
                if(Validador.isFechaNacimientoValida(nuevaFecha)){
                    camposInvalidos.clear(6);
                } else {
                    camposInvalidos.set(6);
                }
                break;
            case "8":
                System.out.println(" 1. Calle\n 2. Número\n 3. Piso\n 4. Departamento\n 5. Localidad\n 6. Provincia\n 7. País\n 8. Código postal"); // Modificado de "Nueva calle, 2. Nuevo número, ..."
                      
                System.out.print("Seleccione el número del campo que desea modificar: ");
                opcion = entrada.nextLine();
                switch (opcion) {
                    case "1":
                        System.out.print("Calle: "); // Modificado de "Nueva calle: "
                        String nuevaCalle = entrada.nextLine();
                        datosModificados.getDatos().getDatos_residencia().setCalle(nuevaCalle);
                        if(Validador.isCalleValida(nuevaCalle)){
                            camposDireccionInvalida.clear(0);
                        } else {
                            camposDireccionInvalida.set(0);
                        }
                        break;
                    case "2":
                        System.out.print("Número: "); // Modificado de "Nuevo número: "
                        String nuevoNumero = entrada.nextLine();
                        datosModificados.getDatos().getDatos_residencia().setNro_calle(nuevoNumero);
                        if(Validador.isNumeroCalleValido(nuevoNumero)){
                            camposDireccionInvalida.clear(1);
                        } else {
                            camposDireccionInvalida.set(1);
                        }
                        break;
                    case "3":
                        System.out.print("Piso: "); // Modificado de "Nuevo piso: "
                        String nuevoPiso = entrada.nextLine();
                        datosModificados.getDatos().getDatos_residencia().setPiso(nuevoPiso);
                        if(Validador.isNumeroCalleValido(nuevoPiso)){
                            camposDireccionInvalida.clear(2);
                        } else {
                            camposDireccionInvalida.set(2);
                        }
                        break;
                    case "4":
                        System.out.print("Departamento: "); // Modificado de "Nuevo departamento: "
                        String nuevoDepto = entrada.nextLine();
                        datosModificados.getDatos().getDatos_residencia().setDepto(nuevoDepto);
                        if(Validador.isNumeroCalleValido(nuevoDepto)){
                            camposDireccionInvalida.clear(3);
                        } else {
                            camposDireccionInvalida.set(3);
                        }
                        break;
                    case "5":
                        System.out.print("Localidad: "); // Modificado de "Nueva localidad: "
                        String nuevaLocalidad = entrada.nextLine();
                        datosModificados.getDatos().getDatos_residencia().setLocalidad(nuevaLocalidad);
                        if(Validador.isLocalidadValida(nuevaLocalidad)){
                            camposDireccionInvalida.clear(4);
                        } else {
                            camposDireccionInvalida.set(4);
                        }
                        break;
                    case "6":
                        System.out.print("Provincia: "); // Modificado de "Nueva provincia: "
                        String nuevaProvincia = entrada.nextLine();
                        datosModificados.getDatos().getDatos_residencia().setProv(nuevaProvincia);
                        if(Validador.isProvinciaValida(nuevaProvincia)){
                            camposDireccionInvalida.clear(5);
                        } else {
                            camposDireccionInvalida.set(5);
                        }
                        break;
                    case "7":
                        System.out.print("País: "); // Modificado de "Nuevo país: "
                        String nuevoPais = entrada.nextLine();
                        datosModificados.getDatos().getDatos_residencia().setPais(nuevoPais);
                        if(Validador.isPaisValido(nuevoPais)){
                            camposDireccionInvalida.clear(6);
                        } else {
                            camposDireccionInvalida.set(6);
                        }
                        break;
                    case "8":
                        System.out.print("Código postal: "); // Modificado de "Nuevo código postal: "
                        String nuevoCodPost = entrada.nextLine();
                        datosModificados.getDatos().getDatos_residencia().setCod_post(nuevoCodPost);
                        if(Validador.isCodigoPostalValido(nuevoCodPost)){
                            camposDireccionInvalida.clear(7);
                        } else {
                            camposDireccionInvalida.set(7);
                        }
                        break;
                }
                if (camposDireccionInvalida.isEmpty()){
                    camposInvalidos.clear(7);
                } else {
                    camposInvalidos.set(7);
                }
                break;
            case "9":
                System.out.print("Teléfono: "); // Modificado de "Nuevo teléfono: "
                String telefono = entrada.nextLine();
                datosModificados.getDatos().getDatos_contacto().setTelefono(telefono);
                if(Validador.isTelefonoValido(telefono)){
                    camposInvalidos.clear(8);
                } else {
                    camposInvalidos.set(8);
                }
                break;
            case "10":
                System.out.print("Email: "); // Modificado de "Nuevo email: "
                String email = entrada.nextLine();
                datosModificados.getDatos().getDatos_contacto().setEmail(email);
                if(Validador.isEmailValidoOpcional(email)){
                    camposInvalidos.clear(9);
                } else {
                    camposInvalidos.set(9);
                }
                break;
            case "11":
                System.out.print("Ocupación: "); // Modificado de "Nueva ocupación: "
                String ocupacion = entrada.nextLine();
                datosModificados.getDatos().getDatos_personales().setOcupacion(ocupacion);
                if(Validador.isOcupacionValida(ocupacion)){
                    camposInvalidos.clear(10);
                } else {
                    camposInvalidos.set(10);
                }
                break;
            case "12":
                System.out.print("Nacionalidad: "); // Modificado de "Nueva nacionalidad: "
                String nacionalidad = entrada.nextLine();
                datosModificados.getDatos().getDatos_personales().setNacionalidad(nacionalidad);
                if(Validador.isNacionalidadValida(nacionalidad)){
                    camposInvalidos.clear(11);
                } else {
                    camposInvalidos.set(11);
                }
                break;

        }
        //entrada.close();
        return datosModificados;

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

}

