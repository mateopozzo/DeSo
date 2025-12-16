package ddb.deso.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ddb.deso.almacenamiento.DTO.DatosCheckOutDTO;
import ddb.deso.negocio.TipoDoc;
import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.negocio.alojamiento.Alojado;
import ddb.deso.almacenamiento.DTO.CriteriosBusq;
import ddb.deso.negocio.alojamiento.DatosCheckOut;
import ddb.deso.negocio.alojamiento.FactoryAlojado;
import ddb.deso.negocio.alojamiento.Huesped;
import ddb.deso.service.enumeradores.ResumenHistorialHuesped;
import ddb.deso.service.excepciones.AlojadoInvalidoException;
import ddb.deso.service.excepciones.AlojadoNoEliminableException;
import ddb.deso.service.excepciones.AlojadoPreExistenteException;
import ddb.deso.service.excepciones.AlojadosSinCoincidenciasException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GestorAlojamiento {
    private final AlojadoDAO alojadoDAO;

    /**
     * Constructor para la inyección de dependencias.
     * @param alojadoDAO Implementación del DAO para acceso a datos de Alojados.
     */
    @Autowired
    public GestorAlojamiento(AlojadoDAO alojadoDAO) {
        this.alojadoDAO = alojadoDAO;
    }

    /**
     * Verifica la existencia de un alojado en el sistema mediante su documento.
     *
     * @param dni Número de documento a verificar.
     * @param tipo Tipo de documento.
     * @return {@code true} si existe un alojado con ese documento, {@code false} en caso contrario o si los parámetros son nulos.
     */
    public boolean dniExiste(String dni, TipoDoc tipo) {
        if (tipo == null || dni == null || dni.isBlank()) {
            return false;
        }
        CriteriosBusq criterios_busq = new CriteriosBusq(null, null, tipo, dni);
        List<Alojado> encontrados = alojadoDAO.buscarAlojado(criterios_busq);
        return encontrados != null && !encontrados.isEmpty();
    }

    /**
     * Busca coincidencias en la base de datos basándose en los criterios de búsqueda proporcionados.
     *
     * @param criterios_busq Criterios de búsqueda (nombre, apellido, tipo y número de documento).
     * @return Lista de objetos {@link CriteriosBusq} con los datos básicos de los alojados encontrados.
     * @throws AlojadosSinCoincidenciasException Si no se encuentran registros que coincidan con el criterio.
     */
    public List<CriteriosBusq> buscarCriteriosAlojado(CriteriosBusq criterios_busq) throws AlojadosSinCoincidenciasException {

        List<Alojado> encontrados = alojadoDAO.buscarAlojado(criterios_busq);

        if(encontrados == null || encontrados.isEmpty())
            throw new AlojadosSinCoincidenciasException("No hay ocurrencias de Alojado disponibles para el criterio dado");

        return conversionAlojadoToCriterio(encontrados);
    }

    /**
     * Busca específicamente Huéspedes (aquellos responsables de reservas) que coincidan con los criterios.
     * Filtra los resultados para excluir a los Invitados.
     *
     * @param criterios_busq Criterios de búsqueda.
     * @return Lista de {@link CriteriosBusq} correspondientes a los Huéspedes encontrados.
     * @throws AlojadosSinCoincidenciasException Si no se encuentran huéspedes o la lista inicial es vacía.
     */
    public List<CriteriosBusq> buscarCriteriosHuesped(CriteriosBusq criterios_busq) throws AlojadosSinCoincidenciasException {
        try{
            var listaAlojados = alojadoDAO.buscarAlojado(criterios_busq);
            if(listaAlojados == null || listaAlojados.isEmpty()){
                throw new AlojadosSinCoincidenciasException("No hay ocurrencias de Alojados disponibles para el criterio dado");
            }
            List<Huesped> listaHuespedes = listaAlojados.stream()
                    .filter(alojado -> alojado instanceof Huesped)
                    .map(alojado -> (Huesped) alojado)
                    .toList();
            if(listaHuespedes == null || listaHuespedes.isEmpty()){
                throw new AlojadosSinCoincidenciasException("No hay ocurrencias de Huesped disponibles para el criterio dado");
            }
            return conversionAlojadoToCriterio(listaHuespedes);
        } catch (AlojadosSinCoincidenciasException e){
            throw e;
        }
    }

    /**
     * Gestiona la modificación de los datos de un huésped en el sistema.
     *
     * Este metodo recibe el objeto Alojado original y el objeto Alojado con los datos
     * ya modificados (provenientes de la capa de presentación). Se encarga de
     * convertir ambos objetos de dominio a sus respectivos DTO (Data Transfer Objects)
     * y luego invoca al DAO ({@link AlojadoDAO}) para que persista la actualización.
     *
     * @param dtoAlojadoOriginal El objeto {@link AlojadoDTO} con los datos tal como se encontraban
     * antes de la modificación.
     * @param dtoAlojadoModificado El objeto {@link AlojadoDTO} que contiene los nuevos datos
     * a guardar en el sistema.
     * @return La entidad que persiste en la base
     */
    public AlojadoDTO modificarHuesped(AlojadoDTO dtoAlojadoOriginal, AlojadoDTO dtoAlojadoModificado, boolean forzar) throws AlojadoPreExistenteException {


        {   // parafenralia de verificaciones
            if (dtoAlojadoOriginal == null) {
                throw new AlojadoInvalidoException("El alojado original es nulo");
            }

            if (dtoAlojadoModificado == null) {
                throw new AlojadoInvalidoException("El alojado modificado es null");
            }

            System.out.println("NONULOS");

            if (!dtoAlojadoOriginal.verificarCamposObligatorios()) {
                throw new AlojadoInvalidoException("El alojado original no cumple con los campos obligatorios");
            }

            if (!dtoAlojadoModificado.verificarCamposObligatorios()) {
                throw new AlojadoInvalidoException("El alojado modificado no cumple con los campos obligatorios");
            }

            System.out.println("CAMPOS");

            if (dtoAlojadoOriginal.getTipoDoc() == null || dtoAlojadoOriginal.getNroDoc() == null) {
                throw new AlojadoInvalidoException("La identidad del alojado original es invalida");
            }

            if (dtoAlojadoModificado.getTipoDoc() == null || dtoAlojadoModificado.getNroDoc() == null) {
                throw new AlojadoInvalidoException("La identidad del alojado modificado es invalida");
            }

            System.out.println("IDENTIDADES");

            if(!forzar
                && (!dtoAlojadoModificado.getNroDoc().equals(dtoAlojadoOriginal.getNroDoc())
                       || !dtoAlojadoModificado.getTipoDoc().equals(dtoAlojadoOriginal.getTipoDoc()) )
                && dniExiste(dtoAlojadoModificado.getNroDoc(), dtoAlojadoModificado.getTipoDoc())){
                throw new AlojadoPreExistenteException("El alojado " + dtoAlojadoModificado.getTipoDoc().toString()
                        + " " + dtoAlojadoModificado.getNroDoc() + "ya existe en el sistema");
            }

            System.out.println("EXISTENCIA");
        }

        var alojadoOriginal = FactoryAlojado.createFromDTO(dtoAlojadoOriginal);
        var alojadoModificado = FactoryAlojado.createFromDTO(dtoAlojadoModificado);

        alojadoDAO.actualizarAlojado(alojadoOriginal, alojadoModificado);

        {// check de modificacion
            var alojadoGuardado = alojadoDAO.buscarPorDNI(dtoAlojadoModificado.getNroDoc(), dtoAlojadoModificado.getTipoDoc());

            if (alojadoGuardado == null) {
                throw new AlojadoInvalidoException("El alojado no se persiste");
            }

            if (!alojadoGuardado.comparteDatos(alojadoModificado)) {
                System.out.println("LOS DATOS EN LA BASE NO SE MODIFICARON");
                throw new AlojadoInvalidoException("El alojado se guardo incorrectamente");
            }

            return new AlojadoDTO(alojadoGuardado);
        }
    }

    /**
     * Registra un nuevo alojado en el sistema.
     * Valida que los campos obligatorios estén presentes antes de delegar la creación al DAO.
     *
     * @param alojadoDTO Objeto de transferencia con los datos del nuevo alojado.
     * @throws AlojadoInvalidoException Si el DTO es nulo o faltan campos obligatorios.
     */
    public void darDeAltaHuesped(AlojadoDTO alojadoDTO) throws AlojadoInvalidoException {
        if(alojadoDTO == null || !alojadoDTO.verificarCamposObligatorios()){
            throw new AlojadoInvalidoException("Los campos obligatorios no están completos");
        }
        alojadoDAO.crearAlojado(FactoryAlojado.createFromDTO(alojadoDTO));
    }

    /**
     * Verifica si un huésped se alojó previamente basado en los criterios de búsqueda.
     *
     * @param criterios: Criterios de búsqueda del huésped (nombre, apellido, tipo y número de documento).
     * @return Estado del historial del huésped.
     *
     * <ul>
     * <li>{@link ResumenHistorialHuesped#SE_ALOJO}: El huésped tiene check-in o check-out registrados.</li>
     * <li>{@link ResumenHistorialHuesped#NO_SE_ALOJO}: El huésped está persistido, pero no tiene check-in/out.</li>
     * <li>{@link ResumenHistorialHuesped#NO_PERSISTIDO}: El huésped no fue encontrado.</li>
     * </ul>
     */
    private ResumenHistorialHuesped huespedSeAlojo(CriteriosBusq criterios) {

        List<Alojado> listaAlojados = alojadoDAO.buscarAlojado(criterios);

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
     * Verifica si un huésped se alojó previamente basado en los criterios de búsqueda.
     *
     * @param alojadoHistorico: Debe definir un alojado univocamente
     * @return Estado del historial del huésped.
     *
     * <ul>
     * <li>{@link ResumenHistorialHuesped#SE_ALOJO}: El huésped tiene check-in o check-out registrados.</li>
     * <li>{@link ResumenHistorialHuesped#NO_SE_ALOJO}: El huésped está persistido, pero no tiene check-in/out.</li>
     * <li>{@link ResumenHistorialHuesped#NO_PERSISTIDO}: El huésped no fue encontrado.</li>
     * </ul>
     */
    private ResumenHistorialHuesped huespedSeAlojo(Alojado alojadoHistorico) {

        if (alojadoHistorico == null) {
            return ResumenHistorialHuesped.NO_PERSISTIDO;
        }

        // No es nulo ni vacio
        boolean tieneCheckIns =
                alojadoHistorico.getDatos().getCheckIns() != null
                && !alojadoHistorico.getDatos().getCheckIns().isEmpty();
        boolean tieneCheckOuts =
                alojadoHistorico.getDatos().getCheckOuts() != null
                && !alojadoHistorico.getDatos().getCheckOuts().isEmpty();

        if (tieneCheckIns || tieneCheckOuts) {
            return ResumenHistorialHuesped.SE_ALOJO;
        }

        return ResumenHistorialHuesped.NO_SE_ALOJO;
    }

    /**
     * Elimina un registro de huésped del sistema.
     * Busca la entidad por DNI y solicita su eliminación al DAO.
     *
     * @param dtoAlojadoEliminacion Objeto {@code CriteriosBusq} que contiene los datos claves (tipo y nro doc) del huésped a eliminar.
     */
    public void eliminarAlojado(AlojadoDTO dtoAlojadoEliminacion) {

        if(dtoAlojadoEliminacion == null){
            throw new AlojadoInvalidoException("El parametro no puede ser unico");
        }
        if(dtoAlojadoEliminacion.getNroDoc() == null || dtoAlojadoEliminacion.getNroDoc().isEmpty()){
            throw new AlojadoInvalidoException("La identidad del alojado no existe");
        }
        if(dtoAlojadoEliminacion.getTipoDoc() == null){
            throw new AlojadoInvalidoException("La identidad del alojado no existe");
        }

        var entidadEliminable = alojadoDAO.buscarPorDNI(dtoAlojadoEliminacion.getNroDoc(),dtoAlojadoEliminacion.getTipoDoc());

        if(entidadEliminable == null){
            throw new AlojadoInvalidoException("No existe la entidad en la base de datos");
        }
        if(!(entidadEliminable.getId().getNroDoc().equals(dtoAlojadoEliminacion.getNroDoc()))){
            throw new AlojadoInvalidoException("Error de identidad en la base de datos");
        }
        if(!(entidadEliminable.getId().getTipoDoc().equals(dtoAlojadoEliminacion.getTipoDoc()))){
            throw new AlojadoInvalidoException("Error de identidad en la base de datos");
        }

        ResumenHistorialHuesped estadoDeAlojado = historialHuesped(entidadEliminable);

        if(estadoDeAlojado != ResumenHistorialHuesped.NO_SE_ALOJO){
            throw new AlojadoNoEliminableException("El huésped no puede ser " +
                    "eliminado pues se ha alojado en el Hotel en alguna oportunidad\n");
        }

        alojadoDAO.eliminarAlojado(entidadEliminable);
    }

    /**
     * Obtiene el resumen del historial de alojamiento de un huésped a partir de su entidad.
     * Extrae los datos necesarios y consulta el estado de sus check-ins/check-outs.
     *
     * @param alojado Objeto {@code Alojado} con los datos del huésped.
     * @return El estado del historial del huésped, uno de los valores de {@link ResumenHistorialHuesped}.
     */
    public ResumenHistorialHuesped historialHuesped(Alojado alojado){

        ResumenHistorialHuesped seAlojo = this.huespedSeAlojo(alojado);

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
    public AlojadoDTO obtenerAlojadoPorDNI(String dni, TipoDoc tipo){
        if (tipo == null || dni == null || dni.isBlank()) {
            return null;
        }
        CriteriosBusq criterios_busq = new CriteriosBusq(null, null, tipo, dni);
        List<Alojado> encontrados = alojadoDAO.buscarAlojado(criterios_busq);
        Alojado encontrado = encontrados.stream()
                .findFirst()
                .orElse(null);

        assert encontrado != null;
        AlojadoDTO retorno = new AlojadoDTO(encontrado);

        return retorno;
    }

    /**
     * Método auxiliar para convertir una lista de entidades Alojado a DTOs de criterio de búsqueda.
     *
     * @param listaAlojado Lista de entidades {@link Alojado} (o sus subclases).
     * @return Lista de {@link CriteriosBusq} mapeados con los datos básicos.
     */
    private List<CriteriosBusq> conversionAlojadoToCriterio(List<? extends Alojado> listaAlojado) {
        List<CriteriosBusq>retornoEncontrados = new ArrayList<>();
        for(var a: listaAlojado){
            retornoEncontrados.add(
                    new CriteriosBusq(
                            a.getDatos().getDatos_personales().getApellido(),
                            a.getDatos().getDatos_personales().getNombre(),
                            a.getDatos().getTipoDoc(),
                            a.getId().getNroDoc()
                    )
            );
        }
        return retornoEncontrados;
    }

    public List<DatosCheckOutDTO> generarCheckOut(List<CriteriosBusq> criteriosBusq){

        if(criteriosBusq == null){return null;}

        List<DatosCheckOutDTO> listaRetorno = new ArrayList<>();

        criteriosBusq.forEach(crit -> {
            DatosCheckOutDTO dtoCO = null;
            try{
                dtoCO=generarCheckOut(crit);
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
            if(dtoCO != null) listaRetorno.add(dtoCO);
        });

        return listaRetorno;
    }

    public DatosCheckOutDTO generarCheckOut(CriteriosBusq criteriosBusq){

        if(criteriosBusq == null){
            throw new AlojadoInvalidoException("Criterio de busqueda nulo");
        }

        if(criteriosBusq.getNroDoc() == null || criteriosBusq.getNroDoc().isEmpty()){
            throw new AlojadoInvalidoException("Numero de documento nulo");
        }

        if(criteriosBusq.getTipoDoc() == null){
            throw new AlojadoInvalidoException("Tipo de documento nulo");
        }

        DatosCheckOut cout = new DatosCheckOut(LocalDateTime.now());
        cout.setFecha_hora_out(LocalDateTime.now());

        var alojado = alojadoDAO.buscarPorDNI(criteriosBusq.getNroDoc(),criteriosBusq.getTipoDoc());

        if(alojado == null){
            return null;
        }

        cout.setAlojado(alojado.getDatos());

        alojado.getDatos().nuevoCheckOut(cout);

        return new DatosCheckOutDTO(cout);

    }

    /**
     * Funcion que devuelve DTO de un alojado seleccionado por cliente
     * La seleccion en la interfaz asegura que solo sea uno
     */
    private AlojadoDTO obtenerAlojadoDTO(CriteriosBusq criterioBusqueda){

        if(criterioBusqueda == null){
            throw new AlojadoInvalidoException("Criterio invalido");
        }

        if(criterioBusqueda.getNroDoc() == null || criterioBusqueda.getTipoDoc() == null){
            throw new AlojadoInvalidoException("Identificaion invalida");
        }

        if(!dniExiste(criterioBusqueda.getNroDoc(), criterioBusqueda.getTipoDoc())){
            throw new AlojadoInvalidoException("El alojado solicitrado no existe");
        }

        var alojado = alojadoDAO.buscarPorDNI(criterioBusqueda.getNroDoc(), criterioBusqueda.getTipoDoc());

        if(alojado == null){
            throw new AlojadoInvalidoException("No se encontro el alojado en la base");
        }

        return new AlojadoDTO(alojado);

    }

    public List<CriteriosBusq> buscarCriteriosALojadoDeEstadia(long idEstadia) {
        var listaAlojados = alojadoDAO.buscarAlojado(idEstadia);
        List<CriteriosBusq> listaRetorno = new ArrayList<>();
        listaAlojados.forEach(a ->
        {
            listaRetorno.add(new CriteriosBusq(
                    a.getDatos().getDatos_personales().getApellido(),
                    a.getDatos().getDatos_personales().getNombre(),
                    a.getDatos().getTipoDoc(),
                    a.getDatos().getNroDoc())
            );
        });
        return listaRetorno;
    }

    public CriteriosBusq buscarCriteriosAlojadoPorCuit(String CUIT){
        if(CUIT == null || CUIT.isEmpty()) return null;

        Alojado entidadRetorno = alojadoDAO.buscarAlojado(CUIT);

        if(entidadRetorno == null) return null;

        if(entidadRetorno.getDatos().getDatos_personales().getCUIT() == null
                || entidadRetorno.getDatos().getDatos_personales().getCUIT().isEmpty()) return null;

        if(!entidadRetorno.getDatos().getDatos_personales().getCUIT().equals(CUIT)) return null;

        return new CriteriosBusq(entidadRetorno);
    }
}

