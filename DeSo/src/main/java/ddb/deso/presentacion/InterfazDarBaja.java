package ddb.deso.presentacion;

import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.almacenamiento.JSON.AlojadoDAOJSON;
import ddb.deso.service.alojamiento.Alojado;
import ddb.deso.gestores.GestorAlojamiento;

import java.io.IOException;
import java.util.Scanner;


/**
 * Clase estática (utility class) que gestiona la **interfaz de usuario para dar de baja
 * (eliminar)** un huésped del sistema (Caso de Uso 11, CU11).
 * <p>
 * Se encarga de la lógica de presentación, como solicitar confirmación, mostrar mensajes
 * de éxito/error, y delegar la lógica de negocio (historial y eliminación) en
 * {@link GestorAlojamiento}.
 * </p>
 *
 * @author mat
 * @see GestorAlojamiento
 */
public class InterfazDarBaja {

    private static Scanner scanner;

    /**
     * Muestra el mensaje de éxito de la baja de un huésped y espera la confirmación del usuario
     * (pulsa tecla) para finalizar el Caso de Uso 11 (CU11).
     *
     * @param eliminarAlojado El huésped que ha sido eliminado.
     * @author mat
     */
    public static void terminarCU11(Alojado eliminarAlojado) {
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
    public static enum BajaCliente {
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
    public static BajaCliente avisoBajaAlojado(Alojado eliminarAlojado) {
        System.out.println("Los datos del huésped " +
                eliminarAlojado.getDatos().getDatos_personales().getNombre() + " " +
                eliminarAlojado.getDatos().getDatos_personales().getApellido() + " " +
                eliminarAlojado.getDatos().getDatos_personales().getTipoDoc() + " " +
                eliminarAlojado.getDatos().getDatos_personales().getNroDoc() + " " +
                "serán ELIMINADOS del sistema"
        );
        System.out.println("Ingresa 1 para aceptar y 0 para cancelar");
        var inp = scanner.nextLine();
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
    public static void noSePuedeDarBaja() {
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
    public static void noExisteHuesped() {
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
     * @param scannerExterno Scanner abierto para el input
     * @author mat
     * @see GestorAlojamiento#historialHuesped(Alojado)
     * @see GestorAlojamiento#eliminarAlojado(Alojado)
     * @see #noSePuedeDarBaja()
     * @see #noExisteHuesped()
     * @see #avisoBajaAlojado(Alojado)
     * @see #terminarCU11(Alojado)
     */
    public static void eliminarHuesped(Alojado alojadoParaEliminar, Scanner scannerExterno){

        scanner = scannerExterno;

        AlojadoDAO json = new AlojadoDAOJSON();
        GestorAlojamiento gestorAlojamiento = new GestorAlojamiento(json);

        //  Flujo secundario, el huesped no se puede eliminar
        var historialAlojado=gestorAlojamiento.historialHuesped(alojadoParaEliminar);
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

        AlojadoDTO aljoadoDTOParaEliminar = new AlojadoDTO(alojadoParaEliminar);

        gestorAlojamiento.eliminarAlojado(aljoadoDTOParaEliminar);

        terminarCU11(alojadoParaEliminar);
    }

}
