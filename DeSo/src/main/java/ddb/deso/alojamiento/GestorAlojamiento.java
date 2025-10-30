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

