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


    public static void modificarHuesped(Alojado alojadoOriginal, Alojado aljadoModificado) {

        System.out.println("Modificando huesped...");
        AlojadoDAO alojadoDAO = new AlojadoDAOJSON();
        AlojadoDTO datosOriginalesDTO = new AlojadoDTO(alojadoOriginal);
        AlojadoDTO datosModificadosDTO = new AlojadoDTO(aljadoModificado);
        alojadoDAO.actualizarAlojado(datosOriginalesDTO, datosModificadosDTO);
    }


    public static void darDeAltaHuesped(Alojado alojadoNuevo){
        AlojadoDAO aDao=new AlojadoDAOJSON();
        AlojadoDTO aDTO=new AlojadoDTO(alojadoNuevo);
        aDao.crearAlojado(aDTO);
    }

    /**
     * Verifica si un huésped, basado en los criterios de búsqueda, se alojó previamente.
     *
     * @param criterios Criterios de búsqueda del huésped (nombre, apellido, tipo y número de documento).
     * @return Estado del historial del huésped.
     * <ul>
     * <li>{@link ResumenHistorialHuesped#SE_ALOJO}: El huésped tiene check-in o check-out registrados.</li>
     * <li>{@link ResumenHistorialHuesped#NO_SE_ALOJO}: El huésped está persistido pero no tiene check-in/out.</li>
     * <li>{@link ResumenHistorialHuesped#NO_PERSISTIDO}: El huésped no fue encontrado.</li>
     * </ul>
     */
    private static ResumenHistorialHuesped huespedSeAlojo(CriteriosBusq criterios) {
        AlojadoDAOJSON DAO = new AlojadoDAOJSON();
        List<AlojadoDTO> listaDTO = DAO.buscarHuespedDAO(criterios);

        if (listaDTO == null || listaDTO.isEmpty()) {
            return ResumenHistorialHuesped.NO_PERSISTIDO;
        }

        AlojadoDTO huespedBaja = listaDTO.getFirst();

        if (huespedBaja == null) {
            return ResumenHistorialHuesped.NO_PERSISTIDO;
        }

        if (huespedBaja.getId_check_in().isEmpty() && huespedBaja.getId_check_out().isEmpty()) {
            return ResumenHistorialHuesped.SE_ALOJO;
        }
        return ResumenHistorialHuesped.NO_SE_ALOJO;
    }


    /**
     * Elimina un registro de huésped del sistema.
     *
     * @param alojado Objeto {@code Alojado} que contiene los datos del huésped a eliminar.
     */
    public static void eliminarAlojado(Alojado alojado) {
        AlojadoDAOJSON DAO = new AlojadoDAOJSON();
        AlojadoDTO eliminar = new AlojadoDTO(alojado);
        DAO.eliminarAlojado(eliminar);
    }

    /**
     * Enumerador ResumenHistorialHuesped  informa el estado del huesped en el sistema
     */
    public enum ResumenHistorialHuesped {
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

    /**
     * Obtiene el resumen del historial de alojamiento de un huésped.
     *
     * @param alojado Objeto {@code Alojado} con los datos del huésped.
     * @return El estado del historial del huésped, uno de los valores de {@link ResumenHistorialHuesped}.
     */
    public static ResumenHistorialHuesped historialHuesped(Alojado alojado){
        var nombre = alojado.getDatos().getDatos_personales().getNombre();
        var apellido = alojado.getDatos().getDatos_personales().getApellido();
        var nroDoc = alojado.getDatos().getDatos_personales().getNroDoc();
        var tipoDoc = alojado.getDatos().getDatos_personales().getTipoDoc();
        CriteriosBusq criterios = new CriteriosBusq(nombre, apellido, tipoDoc, nroDoc);
        ResumenHistorialHuesped seAlojo = huespedSeAlojo(criterios);

        // Flujo secundario de CU, el huesped se alojó o no existe en la base
        if (seAlojo == ResumenHistorialHuesped.SE_ALOJO) {
            return ResumenHistorialHuesped.SE_ALOJO;
        } else if (seAlojo == ResumenHistorialHuesped.NO_PERSISTIDO) {
            return ResumenHistorialHuesped.NO_PERSISTIDO;
        }
        return ResumenHistorialHuesped.NO_SE_ALOJO;
    }

    public static obtenerAlojado(){
        
    }
}

