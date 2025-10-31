package ddb.deso.presentacion;

import java.io.IOException;
import java.time.LocalDate;
import java.util.BitSet;
import java.util.Scanner;

import ddb.deso.TipoDoc;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.GestorAlojamiento;
import ddb.deso.alojamiento.Validador;

public class InterfazModificarHuesped {
    private Scanner entrada;
    private BitSet camposInvalidos;
    private BitSet camposDireccionInvalida;

    /**
     * Constructor de la clase InterfazModificarHuesped.
     * Inicializa el Scanner para la entrada de usuario y los BitSets utilizados
     * para rastrear los campos inválidos en el formulario.
     */
    public InterfazModificarHuesped() {
        this.entrada = new Scanner(System.in);
        this.camposInvalidos = new BitSet(12);
        this.camposDireccionInvalida = new BitSet(8);
    }

    /**
     * [cite_start]Método principal para ejecutar el Caso de Uso 10: Modificar Huésped[cite: 417].
     * <p>
     * Muestra un bucle interactivo donde el usuario puede ver los datos actuales
     * del huésped, seleccionar un campo para modificar, o elegir una acción
     * (Siguiente, Cancelar, Borrar).
     * <p>
     * Al seleccionar "Siguiente", valida que no haya campos inválidos y gestiona
     * [cite_start]la lógica de duplicación de documentos (CU-10 Flujo Alternativo 2.B)[cite: 417, 422].
     * [cite_start]Al seleccionar "Borrar", delega al CU11 ({@link #eliminarHuesped(Alojado)})[cite: 422].
     *
     * @param alojadoOriginal El objeto {@link Alojado} que se desea modificar,
     * cargado desde el CU02 (Buscar Huésped).
     */
    public void ejecutarModiHuesped(Alojado alojadoOriginal) {
        // PUNTO DE INGRESO PRINCIPAL -> LLAMADO DESDE LA LLAMADA MAIN O OTRO CASO DE USO
        System.out.println("Interfaz de modificación de huésped - En desarrollo");
        System.out.println("Este es un apartado especialmente para que el usuario" + "\n" +
                " pueda modificar o eliminar los datos de un huésped ya existente.");

        // Lógica de modificación de huésped aquí
        Alojado datosModificados = alojadoOriginal;
        boolean bandera = true;

        while (bandera) {
            listaDatosHuesped(datosModificados);

            System.out.println();
            System.out.print("Seleccione el número del campo que desea modificar: ");
            String opcion = entrada.nextLine();

            System.out.print("\033[H\033[2J");
            System.out.flush(); // <<-- BORRA LA TERMINAL (ANSI)

            switch (opcion.toLowerCase()) {
                case "siguiente":
                case "s":
                    if (camposInvalidos.isEmpty()) {
                        if (!GestorAlojamiento.dniExiste(datosModificados.getDatos().getDatos_personales().getNroDoc(), datosModificados.getDatos().getDatos_personales().getTipoDoc())) {
                            //guardo datos modificados
                            System.out.println("Guardando cambios...");
                            GestorAlojamiento.modificarHuesped(alojadoOriginal, datosModificados);
                            System.out.println("Cambios guardados.");
                            System.out.print("Los datos del huésped han sido modificados correctamente.");
                            bandera = false;//sale del bucle principal y el CU termina
                        } else {
                            System.out.println("\n¡CUIDADO! El tipo y número de documento ya existen en el sistema.");
                            String boton2 = "-1";
                            while (!(boton2.equals("1") || boton2.equals("2"))) {
                                System.out.print("¿Desea ACEPTAR IGUALMENTE (1) o CORREGIR (2)? ");
                                boton2 = entrada.nextLine();
                            }
                            if (boton2.equals("1")) {
                                //guardo datos con dni repettido
                                GestorAlojamiento.modificarHuesped(alojadoOriginal, datosModificados);
                                System.out.print("Los datos del huésped han sido modificados correctamente.");
                                bandera = false;//sale del bucle principal y el CU termina
                            } else {
                                camposInvalidos.set(3); // marco nro doc como invalido
                            }
                        }
                        //sale del bucle principal y el CU termina
                    }
                    break;
                case "cancelar":
                case "c":
                    String boton3 = "-1";
                    while (!(boton3.equals("1") || boton3.equals("2"))) {

                        System.out.println("¿Desea cancelar la modificación del huésped?");
                        System.out.println("SI (1) o NO (2) ");
                        boton3 = entrada.nextLine();
                    }
                    if (boton3.equals("1")) {
                        bandera = false;//sale del bucle principal y el CU termina
                    }
                    //else vuelve al menu

                    //System.out.print("\033[H\033[2J"); System.out.flush(); // <<-- BORRA LA TERMINAL (ANSI)
                    break;
                case "borrar":
                case "b":
                    eliminarHuesped(alojadoOriginal);
                    bandera = false;//sale del bucle principal y el CU termina
                    break;
                default:
                    datosModificados = cargarCampo(datosModificados, opcion, camposInvalidos, camposDireccionInvalida);
                    //System.out.print("\033[H\033[2J"); System.out.flush(); // <<-- BORRA LA TERMINAL (ANSI)
            }

        }
        System.out.print("El caso de uso 10 termina");

    }

    /**
     * [cite_start]Muestra un menú interactivo para que el usuario seleccione un tipo de documento[cite: 417].
     *
     * @return El {@link TipoDoc} seleccionado por el usuario. Devuelve DNI por defecto
     * si la opción no es válida.
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
     * [cite_start]Muestra por consola todos los datos personales del huésped[cite: 417].
     * <p>
     * También informa al usuario sobre las acciones disponibles (Siguiente, Cancelar, Borrar)
     * e imprime una lista de los campos que actualmente están marcados como inválidos
     * según el BitSet {@code camposInvalidos}.
     *
     * @param alojado El objeto {@link Alojado} cuyos datos se van a mostrar.
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

        System.out.println("Escriba Siguiente (s), Cancelar (c) o Borrar (b)");
        System.out.print("Campos inválidos en: ");
        for (int i = 0; i < 12; i++) {
            if (camposInvalidos.get(i)) {
                System.out.print(" " + (i + 1));
            }
        }
    }

    /**
     * Gestiona la lógica para modificar un campo específico del huésped.
     * <p>
     * Basado en la {@code opcion} seleccionada, solicita al usuario el nuevo valor
     * para ese campo, actualiza el objeto {@code Alojado} (en memoria),
     * valida el nuevo dato usando la clase {@link Validador}, y actualiza los
     * BitSets {@code camposInvalidos} y {@code camposDireccionInvalida}
     * para reflejar el estado de validez del campo.
     *
     * @param alojado El objeto {@link Alojado} que está siendo modificado.
     * @param opcion El número (como String) del campo a modificar.
     * @param camposInvalidos El BitSet que rastrea la validez de los campos principales.
     * @param camposDireccionInvalidos El BitSet que rastrea la validez de los sub-campos de dirección.
     * @return El objeto {@link Alojado} actualizado con el nuevo dato (aún no persistido).
     */
    private Alojado cargarCampo(Alojado alojado, String opcion, BitSet camposInvalidos, BitSet camposDireccionInvalidos) {

        Alojado datosModificados = alojado;

        switch (opcion) {
            case "1":
                System.out.print("Nuevo apellido: ");
                String nuevoApellido = entrada.nextLine();
                datosModificados.getDatos().getDatos_personales().setApellido(nuevoApellido);
                if (Validador.isApellidoValido(nuevoApellido)) {
                    camposInvalidos.clear(0);
                } else {
                    camposInvalidos.set(0);
                }
                break;
            case "2":
                System.out.print("Nuevo nombre: ");
                String nuevoNombre = entrada.nextLine();
                datosModificados.getDatos().getDatos_personales().setNombre(nuevoNombre);
                if (Validador.isNombreValido(nuevoNombre)) {
                    camposInvalidos.clear(1);
                } else {
                    camposInvalidos.set(1);
                }
                break;
            case "3":
                System.out.print("Nuevo tipo de documento: ");
                TipoDoc nuevoTipoDoc = menuTipoDoc();
                datosModificados.getDatos().getDatos_personales().setTipoDoc(nuevoTipoDoc);
                if (Validador.isTipoDocumentoValido(nuevoTipoDoc)) {
                    camposInvalidos.clear(2);
                } else {
                    camposInvalidos.set(2);
                }
                break;
            case "4":
                System.out.print("Nuevo número de documento: ");
                String nuevoNroDoc = entrada.nextLine();
                datosModificados.getDatos().getDatos_personales().setNroDoc(nuevoNroDoc);
                if (Validador.isNumeroDocumentoValido(nuevoNroDoc, datosModificados.getDatos().getDatos_personales().getTipoDoc())) {
                    camposInvalidos.clear(3);
                } else {
                    camposInvalidos.set(3);
                }
                break;
            case "5":
                System.out.print("Nuevo CUIT (sin guiones ni espacios): ");
                String nuevoCuit = entrada.nextLine();
                datosModificados.getDatos().getDatos_personales().setCUIT(nuevoCuit);
                if (Validador.isCuitValidoOpcional(nuevoCuit)) {
                    camposInvalidos.clear(4);
                } else {
                    camposInvalidos.set(4);
                }
                break;
            case "6":
                System.out.print("Nueva posición frente al IVA: ");
                String nuevaPosIva = entrada.nextLine();
                datosModificados.getDatos().getDatos_personales().setPosicionIva(nuevaPosIva);
                if (Validador.isPosicionIvaValida(nuevaPosIva)) {
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
                if (Validador.isFechaNacimientoValida(nuevaFecha)) {
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
                        if (Validador.isCalleValida(nuevaCalle)) {
                            camposDireccionInvalidos.clear(0);
                        } else {
                            camposDireccionInvalidos.set(0);
                        }
                        break;
                    case "2":
                        System.out.print("Nuevo número: ");
                        String nuevoNumero = entrada.nextLine();
                        datosModificados.getDatos().getDatos_residencia().setNro_calle(nuevoNumero);
                        if (Validador.isNumeroCalleValido(nuevoNumero)) {
                            camposDireccionInvalidos.clear(1);
                        } else {
                            camposDireccionInvalidos.set(1);
                        }
                        break;
                    case "3":
                        System.out.print("Nuevo piso: ");
                        String nuevoPiso = entrada.nextLine();
                        datosModificados.getDatos().getDatos_residencia().setPiso(nuevoPiso);
                        if (Validador.isNumeroCalleValido(nuevoPiso)) {
                            camposDireccionInvalidos.clear(2);
                        } else {
                            camposDireccionInvalidos.set(2);
                        }
                        break;
                    case "4":
                        System.out.print("Nuevo departamento: ");
                        String nuevoDepto = entrada.nextLine();
                        datosModificados.getDatos().getDatos_residencia().setDepto(nuevoDepto);
                        if (Validador.isNumeroCalleValido(nuevoDepto)) {
                            camposDireccionInvalidos.clear(3);
                        } else {
                            camposDireccionInvalidos.set(3);
                        }
                        break;
                    case "5":
                        System.out.print("Nueva localidad: ");
                        String nuevaLocalidad = entrada.nextLine();
                        datosModificados.getDatos().getDatos_residencia().setLocalidad(nuevaLocalidad);
                        if (Validador.isLocalidadValida(nuevaLocalidad)) {
                            camposDireccionInvalidos.clear(4);
                        } else {
                            camposDireccionInvalidos.set(4);
                        }
                        break;
                    case "6":
                        System.out.print("Nueva provincia: ");
                        String nuevaProvincia = entrada.nextLine();
                        datosModificados.getDatos().getDatos_residencia().setProv(nuevaProvincia);
                        if (Validador.isProvinciaValida(nuevaProvincia)) {
                            camposDireccionInvalidos.clear(5);
                        } else {
                            camposDireccionInvalidos.set(5);
                        }
                        break;
                    case "7":
                        System.out.print("Nuevo país: ");
                        String nuevoPais = entrada.nextLine();
                        datosModificados.getDatos().getDatos_residencia().setPais(nuevoPais);
                        if (Validador.isPaisValido(nuevoPais)) {
                            camposDireccionInvalidos.clear(6);
                        } else {
                            camposDireccionInvalidos.set(6);
                        }
                        break;
                    case "8":
                        System.out.print("Nuevo código postal: ");
                        String nuevoCodPost = entrada.nextLine();
                        datosModificados.getDatos().getDatos_residencia().setCod_post(nuevoCodPost);
                        if (Validador.isCodigoPostalValido(nuevoCodPost)) {
                            camposDireccionInvalidos.clear(7);
                        } else {
                            camposDireccionInvalidos.set(7);
                        }
                        break;
                }
                if (camposDireccionInvalidos.isEmpty()) {
                    camposInvalidos.clear(7);
                } else {
                    camposInvalidos.set(7);
                }
                break;
            case "9":
                System.out.print("Nuevo teléfono: ");
                String nuevoTelefono = entrada.nextLine();
                datosModificados.getDatos().getDatos_contacto().setTelefono(nuevoTelefono);
                if (Validador.isTelefonoValido(nuevoTelefono)) {
                    camposInvalidos.clear(8);
                } else {
                    camposInvalidos.set(8);
                }
                break;
            case "10":
                System.out.print("Nuevo email: ");
                String nuevoEmail = entrada.nextLine();
                datosModificados.getDatos().getDatos_contacto().setEmail(nuevoEmail);
                if (Validador.isEmailValidoOpcional(nuevoEmail)) {
                    camposInvalidos.clear(9);
                } else {
                    camposInvalidos.set(9);
                }
                break;
            case "11":
                System.out.print("Nueva ocupación: ");
                String nuevaOcupacion = entrada.nextLine();
                datosModificados.getDatos().getDatos_personales().setOcupacion(nuevaOcupacion);
                if (Validador.isOcupacionValida(nuevaOcupacion)) {
                    camposInvalidos.clear(10);
                } else {
                    camposInvalidos.set(10);
                }
                break;
            case "12":
                System.out.print("Nueva nacionalidad: ");
                String nuevaNacionalidad = entrada.nextLine();
                datosModificados.getDatos().getDatos_personales().setNacionalidad(nuevaNacionalidad);
                if (Validador.isNacionalidadValida(nuevaNacionalidad)) {
                    camposInvalidos.clear(11);
                } else {
                    camposInvalidos.set(11);
                }
                break;

        }
        //entrada.close();
        return datosModificados;

    }

    /**
     * Muestra el mensaje de éxito de la baja de un huésped y espera la confirmación del usuario
     * (pulsa tecla) para finalizar el Caso de Uso 11 (CU11).
     *
     * @param eliminarAlojado El huésped que ha sido eliminado.
     * @author mat
     */
    public void terminarCU11(Alojado eliminarAlojado) {
        System.out.println("El huesped " +
                eliminarAlojado.getDatos().getDatos_personales().getNombre() + " " +
                eliminarAlojado.getDatos().getDatos_personales().getApellido() +
                " Se dio de baja correctamente\n" +
                "Pulse cualquier tecla para continuar"
        );
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opciones para la confirmación de la baja de un cliente.
     *
     * @author mat
     */
    public enum BajaCliente {
        /** Confirma la eliminación. */
        ELIMINAR,
        /** Cancela la operación. */
        CANCELAR
    }

    /**
     * Solicita al usuario la confirmación para eliminar los datos del huésped.
     * Muestra una advertencia de eliminación de datos y espera una entrada ('1' para aceptar, '0' para cancelar).
     *
     * @param eliminarAlojado El huésped que se intenta eliminar.
     * @return La opción elegida: {@code BajaCliente.ELIMINAR} o {@code BajaCliente.CANCELAR}.
     * @author mat
     */
    public BajaCliente avisoBajaAlojado(Alojado eliminarAlojado) {
        System.out.println("Los datos del huésped " +
                eliminarAlojado.getDatos().getDatos_personales().getNombre() + " " +
                eliminarAlojado.getDatos().getDatos_personales().getApellido() + " " +
                eliminarAlojado.getDatos().getDatos_personales().getTipoDoc() + " " +
                eliminarAlojado.getDatos().getDatos_personales().getNroDoc() + " " +
                "serán ELIMINADOS del sistema"
        );
        System.out.println("Ingresa 1 para aceptar y 0 para cancelar");
        var inp = entrada.nextLine();
        /*
         * LOGICA DE VALIDADOR
         *   ES VACIO
         *   ES SOLO 0 O 1
         * */
        if (inp.equals("1")) {
            return BajaCliente.ELIMINAR;
        }

        return BajaCliente.CANCELAR;
    }

    /**
     * Muestra un mensaje de error cuando la baja no es posible porque el huésped
     * tiene historial de alojamiento. Espera la pulsación de una tecla para continuar.
     *
     * @author mat
     */
    public void noSePuedeDarBaja() {
        System.out.println("El " +
                "huésped no puede ser eliminado pues se " +
                "ha alojado en el Hotel en alguna " +
                "oportunidad. PRESIONE CUALQUIER " +
                "TECLA PARA CONTINUAR");
        // Entiendo que esto lee el primer byte y vuelve, hay que testearlo
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * Muestra un mensaje de error indicando que el huésped no existe en la base de datos.
     *
     * @author mat
     */
    public void noExisteHuesped() {
        System.out.println("El huesped no existe en la base de datos");
        return;
    }


    /**
     * Elimina un huésped del sistema
     * <p>
     * Este método primero verifica el historial del huésped a través de {@code GestorAlojamiento.historialHuesped}.
     * Si el huésped {@code SE_ALOJO} (tiene historial de alojamiento persistido), no se permite la eliminación
     * y se llama a {@code noSePuedeDarBaja()}.
     * Si el huésped {@code NO_PERSISTIDO} (no existe en el sistema o no tiene registro), se llama a {@code noExisteHuesped()}.
     * <p>
     * Si la eliminación es posible, se pide confirmación al usuario a través de {@code avisoBajaAlojado}.
     * Si el usuario cancela, el proceso se detiene. De lo contrario,
     * se procede a la eliminación utilizando {@code GestorAlojamiento.eliminarAlojado(alojadoParaEliminar)}
     * y luego se invoca {@code terminarCU11(alojadoParaEliminar)} para finalizar la operación.
     *
     * @param alojadoParaEliminar El objeto {@code Alojado} que se intenta eliminar del sistema.
     * @author mat
     * @see GestorAlojamiento#historialHuesped(Alojado)
     * @see GestorAlojamiento#eliminarAlojado(Alojado)
     * @see #noSePuedeDarBaja()
     * @see #noExisteHuesped()
     * @see #avisoBajaAlojado(Alojado)
     * @see #terminarCU11(Alojado)
     */
    private void eliminarHuesped(Alojado alojadoParaEliminar){

        //  Flujo secundario, el huesped no se puede eliminar
        var historialAlojado=GestorAlojamiento.historialHuesped(alojadoParaEliminar);
        if(historialAlojado==(GestorAlojamiento.ResumenHistorialHuesped.SE_ALOJO)){
            noSePuedeDarBaja();
            return;
        } else if (historialAlojado==(GestorAlojamiento.ResumenHistorialHuesped.NO_PERSISTIDO)) {
            noExisteHuesped();
            return;
        }

        // Flujo principal, avisa a usuario a quien se elimina
        if(avisoBajaAlojado(alojadoParaEliminar).equals(BajaCliente.CANCELAR)){
            System.out.println("Se cancela la baja del huesped");
            return;
        }

        GestorAlojamiento.eliminarAlojado(alojadoParaEliminar);

        terminarCU11(alojadoParaEliminar);
    }

    public void close(){
        entrada.close();
    }


}
