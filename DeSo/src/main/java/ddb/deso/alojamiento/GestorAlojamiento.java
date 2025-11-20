package ddb.deso.alojamiento;

import java.util.List;
import java.util.Optional;

import ddb.deso.TipoDoc;
import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.presentacion.InterfazBusqueda;
import ddb.deso.repository.AlojadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GestorAlojamiento {
    private final AlojadoDAO alojadoDAO;
    private final AlojadoRepository alojadoRepository;

    public GestorAlojamiento(AlojadoDAO alojadoDAO, AlojadoRepository alojadoRepository) {
        this.alojadoRepository = alojadoRepository;
        this.alojadoDAO = alojadoDAO;
    }

    public boolean dniExiste(String dni, TipoDoc tipo) {
        if (tipo == null || dni == null || dni.isBlank()) {
            return false;
        }
        CriteriosBusq criterios_busq = new CriteriosBusq(null, null, tipo, dni);
        List<AlojadoDTO> encontrados = alojadoDAO.buscarHuespedDAO(criterios_busq);
        return encontrados != null && !encontrados.isEmpty();
    }

    /**
     Busca coincidencias en la base de datos con base en los criterios de búsqueda.

     @param criterios_busq Criterios de búsqueda del huésped opcionales (nombre, apellido, tipo y número de documento).
     */

    public void buscarHuesped(CriteriosBusq criterios_busq) {
        /* Recibe los paŕametros de búsqueda en criterios_busq (String apellido, String nombre, TipoDoc tipoDoc, String nroDoc)
        Llama al DAO, que llama a DAOJSON y busca todos los alojados
        Cuando los encuentra, crea un DTO y los va colando en una lista "encontrados"

        Si no encuentra coincidencias, encontrados is empty y se ejecuta la interfaz sin_coincidencias
        Si encuentra, se ejecuta la interfaz selección y se le pasa la lista de coincidencias
        */

        InterfazBusqueda ui = new InterfazBusqueda();
        List<AlojadoDTO> encontrados;

        encontrados = alojadoDAO.buscarHuespedDAO(criterios_busq);

        if (encontrados == null || encontrados.isEmpty()) {
            ui.sin_coincidencias();
        } else {
            ui.seleccion(encontrados);
        }
    }

    /**
     * Gestiona la modificación de los datos de un huésped en el sistema.
     *
     * Este método recibe el objeto Alojado original y el objeto Alojado con los datos
     * ya modificados (provenientes de la capa de presentación). Se encarga de
     * convertir ambos objetos de dominio a sus respectivos DTO (Data Transfer Objects)
     * y luego invoca al DAO ({@link AlojadoDAO}) para que persista la actualización.
     *
     * @param alojadoOriginal El objeto {@link Alojado} con los datos tal como se encontraban
     * antes de la modificación.
     * @param aljadoModificado El objeto {@link Alojado} que contiene los nuevos datos
     * a guardar en el sistema.
     */

    public void modificarHuesped(Alojado alojadoOriginal, Alojado aljadoModificado) {
        System.out.println("Modificando huesped...");
        AlojadoDTO datosOriginalesDTO = new AlojadoDTO(alojadoOriginal);
        AlojadoDTO datosModificadosDTO = new AlojadoDTO(aljadoModificado);
        // Usa el campo 'alojadoDAO' inyectado
        alojadoDAO.actualizarAlojado(datosOriginalesDTO, datosModificadosDTO);
    }


    public void darDeAltaHuesped(Alojado alojadoNuevo){
        // AlojadoDAO aDao=new AlojadoDAOJSON();
        AlojadoDTO aDTO=new AlojadoDTO(alojadoNuevo);
        alojadoDAO.crearAlojado(aDTO);
    }

    /*
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
        // AlojadoDAOJSON DAO = new AlojadoDAOJSON();
        List<AlojadoDTO> listaDTO = alojadoDAO.buscarHuespedDAO(criterios);

        if (listaDTO == null || listaDTO.isEmpty()) {
            return ResumenHistorialHuesped.NO_PERSISTIDO;
        }

        AlojadoDTO huespedBaja = listaDTO.getFirst();

        if (huespedBaja == null) {
            return ResumenHistorialHuesped.NO_PERSISTIDO;
        }

        boolean tieneCheckIns = huespedBaja.getId_check_in() != null && !huespedBaja.getId_check_in().isEmpty();
        boolean tieneCheckOuts = huespedBaja.getId_check_out() != null && !huespedBaja.getId_check_out().isEmpty();

        if (tieneCheckIns || tieneCheckOuts) {
            return ResumenHistorialHuesped.SE_ALOJO;
        }

        return ResumenHistorialHuesped.NO_SE_ALOJO;
    }


    /*
     Elimina un registro de huésped del sistema.

     @param alojado Objeto {@code Alojado} que contiene los datos del huésped a eliminar.
     */

    public void eliminarAlojado(Alojado alojado) {
        // AlojadoDAOJSON DAO = new AlojadoDAOJSON();
        AlojadoDTO eliminar = new AlojadoDTO(alojado);
        alojadoDAO.eliminarAlojado(eliminar);
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

    /*
     Obtiene el resumen del historial de alojamiento de un huésped.

     @param alojado Objeto {@code Alojado} con los datos del huésped.
     @return El estado del historial del huésped, uno de los valores de {@link ResumenHistorialHuesped}.
     */

    public ResumenHistorialHuesped historialHuesped(Alojado alojado){
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

    /*
     Busca el primer registro de alojado que coincide con el número y tipo de documento especificado
     y lo retorna como un objeto de dominio {@code Alojado}.

     <p>Utiliza el patrón DAO para la búsqueda y el patrón Factory para la conversión del DTO
     a la entidad de dominio {@code Alojado}.

     @param dni El número de documento del alojado.
     @param tipo El tipo de documento (p. ej., DNI, Pasaporte).
     @return La entidad de dominio {@code Alojado} encontrada (que puede ser {@code Huesped} o {@code Invitado}),
     o {@code null} si los parámetros son inválidos o no se encuentra ningún registro.
     @author mat
     @author gael
     */

    public Alojado obtenerAlojadoPorDNI(String dni, TipoDoc tipo){
        if (tipo == null || dni == null || dni.isBlank()) {
            return null;
        }
        CriteriosBusq criterios_busq = new CriteriosBusq(null, null, tipo, dni);
        List<AlojadoDTO> encontrados = alojadoDAO.buscarHuespedDAO(criterios_busq);
        AlojadoDTO encontradoDTO = encontrados.stream()
                .findFirst()
                .orElse(null);
        if(encontradoDTO == null) return null;
        return FactoryAlojado.createFromDTO(encontradoDTO);
    }

    public Alojado crearAlojadoAPI (Alojado nuevo_alojado){
        TipoDoc tipo_doc = nuevo_alojado.getDatos().getTipoDoc();
        String nro_doc = nuevo_alojado.getDatos().getNroDoc();

        Optional<Alojado> alojado_enbdd = alojadoRepository.findByDatos_DatosPersonales_TipoDocAndDatos_DatosPersonales_NroDoc(tipo_doc, nro_doc);
        if (alojado_enbdd.isPresent()) {
            throw new DocDuplicadoException(alojado_enbdd.get());
        }

        return alojadoRepository.save(nuevo_alojado);
    }

    public Alojado sustituirAlojadoAPI (Long id_anterior, Alojado nuevo_alojado){
        Alojado alojado_ant = alojadoRepository.findById(id_anterior).orElseThrow(() -> new RuntimeException("Alojado no encontrado para sustituir: " + id_anterior));
        alojado_ant.setDatos(nuevo_alojado.getDatos());
        return alojadoRepository.save(alojado_ant);
    }
}

