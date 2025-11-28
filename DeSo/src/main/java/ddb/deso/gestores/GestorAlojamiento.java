package ddb.deso.gestores;

import java.util.ArrayList;
import java.util.List;

import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.CriteriosBusq;
import ddb.deso.alojamiento.FactoryAlojado;
import ddb.deso.presentacion.InterfazBusqueda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GestorAlojamiento {
    private final AlojadoDAO alojadoDAO;

    /*Inyeccion de dependencia*/
    @Autowired
    public GestorAlojamiento(AlojadoDAO alojadoDAO) {
        this.alojadoDAO = alojadoDAO;
    }

    public boolean dniExiste(String dni, TipoDoc tipo) {
        if (tipo == null || dni == null || dni.isBlank()) {
            return false;
        }
        CriteriosBusq criterios_busq = new CriteriosBusq(null, null, tipo, dni);
        List<Alojado> encontrados = alojadoDAO.buscarHuespedDAO(criterios_busq);
        return encontrados != null && !encontrados.isEmpty();
    }

    /**
     Busca coincidencias en la base de datos con base en los criterios de búsqueda.

     @param criterios_busq Criterios de búsqueda del huésped opcionales (nombre, apellido, tipo y número de documento).
     */

    public List<Alojado> buscarHuesped(CriteriosBusq criterios_busq) {
        /* Recibe los paŕametros de búsqueda en criterios_busq (String apellido, String nombre, TipoDoc tipoDoc, String nroDoc)
        Llama al DAO, busca todos los alojados

        Si no encuentra coincidencias, encontrados is empty y se ejecuta la interfaz sin_coincidencias
        Si encuentra, se ejecuta la interfaz selección y se le pasa la lista de coincidencias
        */

        List<Alojado> encontrados;
        if(encontrados == null || )
        return alojadoDAO.buscarHuespedDAO(criterios_busq);
    }

    /**
     * Gestiona la modificación de los datos de un huésped en el sistema.
     *
     * Este metodo recibe el objeto Alojado original y el objeto Alojado con los datos
     * ya modificados (provenientes de la capa de presentación). Se encarga de
     * convertir ambos objetos de dominio a sus respectivos DTO (Data Transfer Objects)
     * y luego invoca al DAO ({@link AlojadoDAO}) para que persista la actualización.
     *
     * @param alojadoOriginal El objeto {@link ddb.deso.alojamiento.Alojado} con los datos tal como se encontraban
     * antes de la modificación.
     * @param alojadoModificado El objeto {@link ddb.deso.alojamiento.Alojado} que contiene los nuevos datos
     * a guardar en el sistema.
     */

    public void modificarHuesped(ddb.deso.alojamiento.Alojado alojadoOriginal, ddb.deso.alojamiento.Alojado alojadoModificado) {
        System.out.println("Modificando huésped...");
        // Usa el campo 'alojadoDAO' inyectado
        alojadoDAO.actualizarAlojado(alojadoOriginal, alojadoModificado);
    }


    public void darDeAltaHuesped(Alojado alojadoNuevo){
        alojadoDAO.crearAlojado(alojadoNuevo);
    }

    /**
     Verifica si un huésped se alojó previamente basado en los criterios de búsqueda.

     @param criterios: Criterios de búsqueda del huésped (nombre, apellido, tipo y número de documento).
     @return Estado del historial del huésped.

     <ul>
     <li>{@link ResumenHistorialHuesped#SE_ALOJO}: El huésped tiene check-in o check-out registrados.</li>
     <li>{@link ResumenHistorialHuesped#NO_SE_ALOJO}: El huésped está persistido, pero no tiene check-in/out.</li>
     <li>{@link ResumenHistorialHuesped#NO_PERSISTIDO}: El huésped no fue encontrado.</li>
     </ul>
     */

    private ResumenHistorialHuesped huespedSeAlojo(CriteriosBusq criterios) {

        List<Alojado> listaAlojados = alojadoDAO.buscarHuespedDAO(criterios);

        if (listaAlojados == null || listaAlojados.isEmpty()) {
            return ResumenHistorialHuesped.NO_PERSISTIDO;
        }

        Alojado huespedBaja = listaAlojados.getFirst();

        if (huespedBaja == null) {
            return ResumenHistorialHuesped.NO_PERSISTIDO;
        }

        boolean tieneCheckIns = huespedBaja.getDatos().getCheckIns() != null && !huespedBaja.getDatos().getCheckIns().isEmpty();
        boolean tieneCheckOuts = huespedBaja.getDatos().getCheckOuts() != null && !huespedBaja.getDatos().getCheckOuts().isEmpty();

        if (tieneCheckIns || tieneCheckOuts) {
            return ResumenHistorialHuesped.SE_ALOJO;
        }

        return ResumenHistorialHuesped.NO_SE_ALOJO;
    }


    /**
     Elimina un registro de huésped del sistema.

     @param alojado Objeto {@code Alojado} que contiene los datos del huésped a eliminar.
     */

    public void eliminarAlojado(ddb.deso.alojamiento.Alojado alojado) {
        // AlojadoDAOJSON DAO = new AlojadoDAOJSON();
        alojadoDAO.eliminarAlojado(alojado);
    }

    // Enumerador ResumenHistorialHuesped informa el estado del huesped en el sistema

    public enum ResumenHistorialHuesped {
        // Tuvo alguna estadia en el hotel
        SE_ALOJO,

        // No tuvo ninguna estadia, pero sus datos están persistidos
        NO_SE_ALOJO,

        // Sus datos no están presentes en la base del sistema
        NO_PERSISTIDO
    }

    /**
     Obtiene el resumen del historial de alojamiento de un huésped.

     @param alojado Objeto {@code Alojado} con los datos del huésped.
     @return El estado del historial del huésped, uno de los valores de {@link ResumenHistorialHuesped}.
     */

    public ResumenHistorialHuesped historialHuesped(ddb.deso.alojamiento.Alojado alojado){
        var nombre = alojado.getDatos().getDatos_personales().getNombre();
        var apellido = alojado.getDatos().getDatos_personales().getApellido();
        var nroDoc = alojado.getDatos().getDatos_personales().getNroDoc();
        var tipoDoc = alojado.getDatos().getDatos_personales().getTipoDoc();

        CriteriosBusq criterios = new CriteriosBusq(apellido, nombre, tipoDoc, nroDoc);
        System.out.println(nombre);
        System.out.println(apellido);
        System.out.println(nroDoc);
        System.out.println(tipoDoc);

        ResumenHistorialHuesped seAlojo = this.huespedSeAlojo(criterios);

        if (seAlojo == ResumenHistorialHuesped.SE_ALOJO) {
            return ResumenHistorialHuesped.SE_ALOJO;
        } else if (seAlojo == ResumenHistorialHuesped.NO_PERSISTIDO) {
            return ResumenHistorialHuesped.NO_PERSISTIDO;
        }
        return ResumenHistorialHuesped.NO_SE_ALOJO;
    }

    /**
     Busca el primer registro de alojado que coincide con el número y tipo de documento especificado
     y lo retorna como un objeto de dominio {@code Alojado}.

     <p>Utiliza el patrón DAO para la búsqueda y el patrón Factory para la conversión del DTO
     a la entidad de dominio {@code Alojado}.

     @param dni El número de documento del alojado.
     @param tipo El tipo de documento (p. ej., DNI, Pasaporte).
     @return La entidad de dominio {@code Alojado} encontrada (que puede ser {@code Huesped} o {@code Invitado}),
     o {@code null} si los parámetros son inválidos o no se encuentra ningún registro.
     */

    public ddb.deso.alojamiento.Alojado obtenerAlojadoPorDNI(String dni, TipoDoc tipo){
        if (tipo == null || dni == null || dni.isBlank()) {
            return null;
        }
        CriteriosBusq criterios_busq = new CriteriosBusq(null, null, tipo, dni);
        List<Alojado> encontrados = alojadoDAO.buscarHuespedDAO(criterios_busq);
        Alojado encontrado = encontrados.stream()
                .findFirst()
                .orElse(null);
        return encontrado;
    }
}

