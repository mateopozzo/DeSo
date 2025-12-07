package ddb.deso.presentacion;

import java.time.LocalDate;
import java.util.BitSet;
import java.util.Scanner;

import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.JSON.AlojadoDAOJSON;
import ddb.deso.service.alojamiento.Alojado;
import ddb.deso.service.alojamiento.DatosAlojado;
import ddb.deso.service.alojamiento.FactoryAlojado;
import ddb.deso.gestores.GestorAlojamiento;
import ddb.deso.service.alojamiento.Validador;

/**
 * Clase que representa la interfaz de consola para dar de alta a un nuevo huésped
 * en el sistema de alojamiento.
 
 * Permite al usuario ingresar los datos de un huésped, validar la información
 * ingresada, verificar duplicados y registrar correctamente el alta en el sistema.
 
 * 
 * @author 
 */
public class InterfazDarAlta {
    
    /** Entrada estándar utilizada para leer los datos del usuario. */
    private final Scanner entrada;

    /** BitSet que indica los campos personales inválidos del huésped. */
    private final BitSet camposInvalidos;

    /** BitSet que indica los campos inválidos de la dirección del huésped. */
    private final BitSet camposDireccionInvalida;

    /**
     * Constructor por defecto.
     * Inicializa los BitSet de validación y el lector de entrada estándar.
     */
    public InterfazDarAlta() {
        this.entrada = new Scanner(System.in);
        this.camposInvalidos = new BitSet(12);
        this.camposDireccionInvalida = new BitSet(8);
    }

    /**
     * Ejecuta el flujo completo para dar de alta un nuevo huésped.

     * Este método guía al usuario a través de la carga de datos, validación,
     * verificación de duplicados y almacenamiento final del huésped en el sistema.

     */
    public void ejecutarDarAlta() {

        camposInvalidos.set(0, 12);
        camposDireccionInvalida.set(0, 8);
        AlojadoDAO json = new AlojadoDAOJSON();
        GestorAlojamiento gestorAlojamiento = new GestorAlojamiento(json);

        boolean bandera = true;
        DatosAlojado da = new DatosAlojado();
        Alojado nuevoAlojado = FactoryAlojado.create(FactoryAlojado.HUESPED, da);

        while (bandera) {
            System.out.println("CREAR HUESPED NUEVO --------------------------------------");
            listaDatosHuesped(nuevoAlojado);
            System.out.println("Presione una opcion numérica o SIGUIENTE (s) CANCELAR (c) ");
            System.out.println();
            String opcion = entrada.nextLine();

            System.out.print("\033[H\033[2J");
            System.out.flush(); // Limpia la consola (ANSI)

            switch (opcion.toLowerCase()) {

                case "siguiente":
                case "s":
                    if (!camposInvalidos.isEmpty()) {
                        muestraCamposValidos();
                        continue;
                    } else {
                        String nro_doc = nuevoAlojado.getDatos().getDatos_personales().getNroDoc();
                        TipoDoc tipo_doc = nuevoAlojado.getDatos().getDatos_personales().getTipoDoc();
                        boolean existe_dni = gestorAlojamiento.dniExiste(nro_doc, tipo_doc);

                        if (existe_dni) {
                            System.out.println("\n¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
                            String boton2 = "-1";
                            while (!(boton2.equals("1") || boton2.equals("2"))) {
                                System.out.print("¿Desea ACEPTAR IGUALMENTE (1) o CORREGIR (2)? ");
                                boton2 = entrada.nextLine();
                            }

                            if (boton2.equals("2")) {
                                camposInvalidos.set(3); // marca nro doc como inválido
                                continue;
                            } else {
                                Alojado alojadoAnterior = gestorAlojamiento.obtenerAlojadoPorDNI(nro_doc, tipo_doc);
                                gestorAlojamiento.eliminarAlojado(alojadoAnterior);
                            }
                        }
                        // Guarda los datos
                        gestorAlojamiento.darDeAltaHuesped(nuevoAlojado);
                        System.out.print("El Huésped " +
                                nuevoAlojado.getDatos().getDatos_personales().getNombre() + " " +
                                nuevoAlojado.getDatos().getDatos_personales().getApellido() +
                                " se ha cargado correctamente.");
                        bandera = false;
                        String boton3 = "-1";
                        while (!(boton3.equals("1") || boton3.equals("2"))) {
                            System.out.print("¿Desea cargar otro? SI(1)/ NO(2) ");
                            boton3 = entrada.nextLine();
                        }
                        if (boton3.equals("1")) {
                            System.out.print("\033[H\033[2J");
                            System.out.flush();
                            bandera = true;
                            da = new DatosAlojado();
                            nuevoAlojado.setDatos(da);
                        }
                    }
                    break;

                case "cancelar":
                case "c":
                    String boton3 = "-1";
                    while (!(boton3.equals("1") || boton3.equals("2"))) {
                        System.out.println("¿Desea cancelar el alta del huésped?");
                        System.out.println("SI (1) o NO (2) ");
                        boton3 = entrada.nextLine();
                    }
                    if (boton3.equals("1")) {
                        bandera = false;
                    }
                    break;

                default:
                    cargarCampo(nuevoAlojado, opcion, camposInvalidos, camposDireccionInvalida);
            }
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    /**
     * Muestra por pantalla todos los datos actuales del huésped.
     *
     * @param alojado objeto {@link Alojado} cuyos datos se mostrarán.
     */
    private void listaDatosHuesped(Alojado alojado) {
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

    /**
     * Muestra los campos inválidos detectados durante la validación.
     */
    private void muestraCamposValidos() {
        System.out.print("Vuelva a ingresar los datos. Campos inválidos en: ");
        for (int i = 0; i < 12; i++) {
            if (camposInvalidos.get(i)) {
                System.out.print(" " + (i + 1));
            }
        }
        System.out.println();
        if (camposInvalidos.get(7)) {
            System.out.print("Campos inválidos Dirección:");
            for (int j = 0; j < 8; j++) {
                if (camposDireccionInvalida.get(j)) {
                    System.out.print(" " + (j + 1));
                }
            }
            System.out.println();
        }
    }

       /**
     * Permite cargar o modificar un campo específico de los datos de un huésped.
     * <p>
     * Según la opción numérica ingresada, este método solicita al usuario el valor
     * correspondiente, lo valida mediante la clase {@link Validador} y actualiza
     * el objeto {@link Alojado}. En caso de que el valor sea inválido, se marca el
     * bit correspondiente en los {@link BitSet} de validación.
     * </p>
     *
     * @param alojado el objeto {@link Alojado} cuyos datos se van a completar o modificar.
     * @param opcion número del campo que el usuario desea modificar (entre 1 y 12).
     * @param camposInvalidos conjunto de bits que marca los campos personales inválidos.
     * @param camposDireccionInvalida conjunto de bits que marca los campos de dirección inválidos.
     * @return el objeto {@link Alojado} actualizado con los nuevos valores.
     */
    private Alojado cargarCampo(Alojado alojado, String opcion,
                                BitSet camposInvalidos, BitSet camposDireccionInvalida) {

        switch (opcion) {
            case "1":
                System.out.print("Apellido: ");
                String apellido = entrada.nextLine();
                alojado.getDatos().getDatos_personales().setApellido(apellido);
                if (Validador.isApellidoValido(apellido)) {
                    camposInvalidos.clear(0);
                } else {
                    camposInvalidos.set(0);
                }
                break;

            case "2":
                System.out.print("Nombre: ");
                String nombre = entrada.nextLine();
                alojado.getDatos().getDatos_personales().setNombre(nombre);
                if (Validador.isNombreValido(nombre)) {
                    camposInvalidos.clear(1);
                } else {
                    camposInvalidos.set(1);
                }
                break;

            case "3":
                System.out.print("Tipo de documento: ");
                TipoDoc nuevoTipoDoc = menuTipoDoc();
                alojado.getDatos().getDatos_personales().setTipoDoc(nuevoTipoDoc);

                if (Validador.isTipoDocumentoValido(nuevoTipoDoc)) {
                    camposInvalidos.clear(2);
                } else {
                    camposInvalidos.set(2);
                }
                break;

            case "4":
                System.out.print("Número de documento: ");
                String nroDoc = entrada.nextLine();
                alojado.getDatos().getDatos_personales().setNroDoc(nroDoc);

                if (Validador.isNumeroDocumentoValido(nroDoc, alojado.getDatos().getDatos_personales().getTipoDoc())) {
                    camposInvalidos.clear(3);
                } else {
                    camposInvalidos.set(3);
                }
                break;

            case "5":
                System.out.print("CUIT (con el formato XX-XXXXXXXX-X): ");
                String cuit = entrada.nextLine();
                alojado.getDatos().getDatos_personales().setCUIT(cuit);
                if (Validador.isCuitValidoOpcional(cuit)) {
                    camposInvalidos.clear(4);
                } else {
                    camposInvalidos.set(4);
                }
                break;

            case "6":
                System.out.print("Posición frente al IVA: ");
                String posIva = entrada.nextLine();
                alojado.getDatos().getDatos_personales().setPosicionIva(posIva);

                if (Validador.isPosicionIvaValida(posIva)) {
                    camposInvalidos.clear(5);
                } else {
                    camposInvalidos.set(5);
                }
                break;

            case "7":
                System.out.print("Fecha de nacimiento (AAAA-MM-DD): ");
                String fechaStr = entrada.nextLine();
                LocalDate nuevaFecha = LocalDate.parse(fechaStr);
                alojado.getDatos().getDatos_personales().setFechanac(nuevaFecha);

                if (Validador.isFechaNacimientoValida(nuevaFecha)) {
                    camposInvalidos.clear(6);
                } else {
                    camposInvalidos.set(6);
                }
                break;

            case "8":
                System.out.println(" 1. Calle\n 2. Número\n 3. Piso\n 4. Departamento\n 5. Localidad\n 6. Provincia\n 7. País\n 8. Código postal");
                System.out.print("Seleccione el número del campo que desea modificar: ");

                opcion = entrada.nextLine();

                switch (opcion) {
                    case "1":
                        System.out.print("Calle: ");
                        String nuevaCalle = entrada.nextLine();
                        alojado.getDatos().getDatos_residencia().setCalle(nuevaCalle);
                        if (Validador.isCalleValida(nuevaCalle)) {
                            camposDireccionInvalida.clear(0);
                        } else {
                            camposDireccionInvalida.set(0);
                        }
                        break;

                    case "2":
                        System.out.print("Número: ");
                        String nuevoNumero = entrada.nextLine();
                        alojado.getDatos().getDatos_residencia().setNro_calle(nuevoNumero);
                        if (Validador.isNumeroCalleValido(nuevoNumero)) {
                            camposDireccionInvalida.clear(1);
                        } else {
                            camposDireccionInvalida.set(1);
                        }
                        break;

                    case "3":
                        System.out.print("Piso: ");
                        String nuevoPiso = entrada.nextLine();
                        alojado.getDatos().getDatos_residencia().setPiso(nuevoPiso);
                        if (Validador.isNumeroCalleValido(nuevoPiso)) {
                            camposDireccionInvalida.clear(2);
                        } else {
                            camposDireccionInvalida.set(2);
                        }
                        break;

                    case "4":
                        System.out.print("Departamento: ");
                        String nuevoDepto = entrada.nextLine();
                        alojado.getDatos().getDatos_residencia().setDepto(nuevoDepto);
                        if (Validador.isNumeroCalleValido(nuevoDepto)) {
                            camposDireccionInvalida.clear(3);
                        } else {
                            camposDireccionInvalida.set(3);
                        }
                        break;

                    case "5":
                        System.out.print("Localidad: ");
                        String nuevaLocalidad = entrada.nextLine();
                        alojado.getDatos().getDatos_residencia().setLocalidad(nuevaLocalidad);
                        if (Validador.isLocalidadValida(nuevaLocalidad)) {
                            camposDireccionInvalida.clear(4);
                        } else {
                            camposDireccionInvalida.set(4);
                        }
                        break;

                    case "6":
                        System.out.print("Provincia: ");
                        String nuevaProvincia = entrada.nextLine();
                        alojado.getDatos().getDatos_residencia().setProv(nuevaProvincia);
                        if (Validador.isProvinciaValida(nuevaProvincia)) {
                            camposDireccionInvalida.clear(5);
                        } else {
                            camposDireccionInvalida.set(5);
                        }
                        break;

                    case "7":
                        System.out.print("País: ");
                        String nuevoPais = entrada.nextLine();
                        alojado.getDatos().getDatos_residencia().setPais(nuevoPais);
                        if (Validador.isPaisValido(nuevoPais)) {
                            camposDireccionInvalida.clear(6);
                        } else {
                            camposDireccionInvalida.set(6);
                        }
                        break;

                    case "8":
                        System.out.print("Código postal: ");
                        String nuevoCodPost = entrada.nextLine();
                        alojado.getDatos().getDatos_residencia().setCod_post(nuevoCodPost);
                        if (Validador.isCodigoPostalValido(nuevoCodPost)) {
                            camposDireccionInvalida.clear(7);
                        } else {
                            camposDireccionInvalida.set(7);
                        }
                        break;
                }

                if (camposDireccionInvalida.isEmpty()) {
                    camposInvalidos.clear(7);
                } else {
                    camposInvalidos.set(7);
                }
                break;

            case "9":
                System.out.print("Teléfono: ");
                String telefono = entrada.nextLine();
                alojado.getDatos().getDatos_contacto().setTelefono(telefono);
                if (Validador.isTelefonoValido(telefono)) {
                    camposInvalidos.clear(8);
                } else {
                    camposInvalidos.set(8);
                }
                break;

            case "10":
                System.out.print("Email: ");
                String email = entrada.nextLine();
                alojado.getDatos().getDatos_contacto().setEmail(email);
                if (Validador.isEmailValidoOpcional(email)) {
                    camposInvalidos.clear(9);
                } else {
                    camposInvalidos.set(9);
                }
                break;

            case "11":
                System.out.print("Ocupación: ");
                String ocupacion = entrada.nextLine();
                alojado.getDatos().getDatos_personales().setOcupacion(ocupacion);
                if (Validador.isOcupacionValida(ocupacion)) {
                    camposInvalidos.clear(10);
                } else {
                    camposInvalidos.set(10);
                }
                break;

            case "12":
                System.out.print("Nacionalidad: ");
                String nacionalidad = entrada.nextLine();
                alojado.getDatos().getDatos_personales().setNacionalidad(nacionalidad);
                if (Validador.isNacionalidadValida(nacionalidad)) {
                    camposInvalidos.clear(11);
                } else {
                    camposInvalidos.set(11);
                }
                break;
        }

        return alojado;
    }


    /**
     * Muestra un menú de selección de tipo de documento y devuelve la opción elegida.
     *
     * @return el tipo de documento seleccionado.
     */
    private TipoDoc menuTipoDoc() {
        System.out.println("Seleccione tipo de documento:");
        System.out.println("1. DNI");
        System.out.println("2. LE");
        System.out.println("3. LC");
        System.out.println("4. PASAPORTE");
        System.out.println("5. OTRO");

        TipoDoc tipoDoc;
        String opcion = entrada.nextLine();

        switch (opcion) {
            case "2" -> tipoDoc = TipoDoc.LE;
            case "3" -> tipoDoc = TipoDoc.LC;
            case "4" -> tipoDoc = TipoDoc.PASAPORTE;
            case "5" -> tipoDoc = TipoDoc.OTRO;
            default -> tipoDoc = TipoDoc.DNI;
        }
        return tipoDoc;
    }

    /**
     * Cierra el objeto {@link Scanner} utilizado para la entrada de datos.
     */
    public void close() {
        entrada.close();
    }
}
