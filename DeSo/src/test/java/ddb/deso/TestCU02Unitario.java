package ddb.deso;

import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DTO.CriteriosBusq;
import ddb.deso.service.GestorAlojamiento;
import ddb.deso.service.excepciones.AlojadosSinCoincidenciasException;
import ddb.deso.negocio.TipoDoc;
import ddb.deso.negocio.alojamiento.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.function.Function;

import static org.hibernate.internal.util.collections.CollectionHelper.listOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Clase de pruebas unitarias para {@link GestorAlojamiento}.
 * <p>
 * Verifica la lógica de negocio asociada a la búsqueda y filtrado de alojados (Huéspedes e Invitados).
 * Utiliza {@link Mockito} para simular el comportamiento de {@link AlojadoDAO}, permitiendo validar
 * escenarios de:
 * <ul>
 * <li>Manejo de excepciones ante retornos nulos o listas vacías desde la persistencia.</li>
 * <li>Filtrado correcto de subtipos de {@link Alojado} (distinción entre Huesped e Invitado).</li>
 * <li>Coincidencia precisa de criterios de búsqueda (nombre, apellido, tipo y nro de documento).</li>
 * </ul>
 */

@ExtendWith(MockitoExtension.class)
public class TestCU02Unitario {

    @InjectMocks
    private GestorAlojamiento gestor;

    @Mock
    AlojadoDAO mockitoDAO;


    private List<Alojado> funcionMocoFiltro(CriteriosBusq criteriosBusq, List<? extends Alojado> listaInicial) {

        Function<Alojado, Boolean> funcionFiltro = (Alojado a) -> {
            if(criteriosBusq.getNombre() != null && !criteriosBusq.getNombre().isEmpty()){
                if(!(a.getDatos().getDatos_personales().getNombre().equals(criteriosBusq.getNombre()))){
                    return false;
                }
            }
            if(criteriosBusq.getApellido() != null && !criteriosBusq.getApellido().isEmpty()){
                if(!(a.getDatos().getDatos_personales().getApellido().equals(criteriosBusq.getApellido()))){
                    return false;
                }
            }
            if(criteriosBusq.getNroDoc() != null && !criteriosBusq.getNroDoc().isEmpty()){
                if(!(a.getDatos().getDatos_personales().getNroDoc().equals(criteriosBusq.getNroDoc()))){
                    return false;
                }
            }
            if(criteriosBusq.getTipoDoc() != null){
                return a.getDatos().getDatos_personales().getTipoDoc().equals(criteriosBusq.getTipoDoc());
            }
            return true;
        };


        List<Alojado> ret = new ArrayList<>();
        for(var a : listaInicial){
            if(funcionFiltro.apply(a)){
                ret.add(a);
            }
        }
        return ret;
    }

    /**
     * Verifica que se lance {@link AlojadosSinCoincidenciasException} cuando el DAO
     * retorna {@code null} al buscar huéspedes.
     * <p>
     * Este test asegura que el gestor maneje defensivamente el retorno nulo
     * desde la capa de persistencia para el método {@code buscarHuesped}.
     * </p>
     */
    @Test
    public void buscarHuespedArrojaExcepcionSiDAODevuelveNull(){
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class))).thenReturn(null);
        assertThrows(AlojadosSinCoincidenciasException.class, ()->gestor.buscarHuesped(crit));
    }

    /**
     * Verifica que se lance {@link AlojadosSinCoincidenciasException} cuando el DAO
     * retorna {@code null} al buscar alojados en general.
     * <p>
     * Asegura el manejo correcto de valores nulos provenientes del DAO en el
     * método {@code obtenerCriteriosAlojado}.
     * </p>
     */
    @Test public void buscarAlojadoArrojaExcepcionSiDAODevuelveNull(){
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class))).thenReturn(null);
        assertThrows(AlojadosSinCoincidenciasException.class, ()->gestor.obtenerCriteriosAlojado(crit));
    }

    /**
     * Verifica que se lance {@link AlojadosSinCoincidenciasException} cuando el DAO
     * devuelve una lista vacía al buscar huéspedes.
     * <p>
     * Comprueba que, si no existen registros en la base de datos (lista vacía),
     * el servicio lo interprete correctamente como una falta de coincidencias.
     * </p>
     */
    @Test public void buscarHuespedArrojaExcepcionSiDAODevuelveVacio(){
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class))).thenReturn(new ArrayList<Alojado>());
        assertThrows(AlojadosSinCoincidenciasException.class, ()->gestor.buscarHuesped(crit));
    }

    /**
     * Verifica que se lance {@link AlojadosSinCoincidenciasException} cuando el DAO
     * devuelve una lista vacía al buscar alojados.
     * <p>
     * Valida que el método {@code obtenerCriteriosAlojado} propague la excepción de negocio
     * adecuada cuando la búsqueda no arroja resultados.
     * </p>
     */
    @Test public void buscarAlojadoArrojaExcepcionSiDAODevuelveVacio(){
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class))).thenReturn(new ArrayList<Alojado>());
        assertThrows(AlojadosSinCoincidenciasException.class, ()->gestor.obtenerCriteriosAlojado(crit));
    }

    /**
     * Verifica que se lance la excepción de "sin coincidencias" cuando la búsqueda
     * encuentra alojados, pero ninguno es de tipo {@link Huesped}.
     * <p>
     * Simula un escenario donde el DAO devuelve una lista compuesta exclusivamente
     * por objetos de tipo {@link Invitado}. El gestor debe filtrar estos resultados
     * y, al quedar la lista vacía, lanzar la excepción correspondiente.
     * </p>
     */
    @Test
    public void buscarHuespedNoEncuentraHuespedes(){
        var li = listaDeMuchosInvitados();
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class))).thenReturn(li);
        assertThrows(AlojadosSinCoincidenciasException.class,()->gestor.buscarHuesped(crit));
    }

    /**
     * Verifica que el servicio filtre correctamente un único {@link Invitado} de una
     * lista mixta, retornando solo los {@link Huesped}.
     * <p>
     * Se inyecta una lista que contiene múltiples huéspedes y un solo invitado.
     * La prueba asegura que el tamaño y contenido de la lista resultante coincidan
     * exactamente con los huéspedes originales.
     * </p>
     */
    @Test
    public void buscarHuespedFiltraUnInvitado(){
        var lh = listaDeMuchosHuespedes();
        var li = listaDeUnInvitado();
        List<Alojado> lfinal = new ArrayList<>();
        lfinal.addAll(lh);
        lfinal.addAll(li);
        Collections.shuffle(lfinal);
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class))).thenReturn(lfinal);
        assertDoesNotThrow(()->gestor.buscarHuesped(crit), "No encontro ni un huesped");
        var lconsulta = gestor.buscarHuesped(crit);
        Set<CriteriosBusq> comparadorRetorno = new HashSet<>(lconsulta);
        Set<CriteriosBusq> comparadorOriginal = new HashSet<>(convertirACriterio(lh));
        //El retorno de la consulta tiene que contener los mismos huespedes que lh
        assertEquals(comparadorOriginal, comparadorRetorno);
    }

    /**
     * Verifica que el servicio filtre correctamente múltiples {@link Invitado} de una
     * lista mixta, retornando solo los {@link Huesped}.
     * <p>
     * Similar al caso anterior, pero con una mayor proporción de invitados en la lista
     * simulada, para asegurar la robustez del filtrado en colecciones más grandes.
     * </p>
     */
    @Test
    public void buscarHuespedFiltraMuchosInvitados() {
        var lh = listaDeMuchosHuespedes();
        var li = listaDeMuchosInvitados();
        List<Alojado> lfinal = new ArrayList<>();
        lfinal.addAll(lh);
        lfinal.addAll(li);
        Collections.shuffle(lfinal);
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class))).thenReturn(lfinal);
        assertDoesNotThrow(() -> gestor.buscarHuesped(crit), "No encontro ni un huesped");
        var lconsulta = gestor.buscarHuesped(crit);
        Set<CriteriosBusq> comparadorRetorno = new HashSet<>(lconsulta);
        Set<CriteriosBusq> comparadorOriginal = new HashSet<>(convertirACriterio(lh));
        //El retorno de la consulta tiene que contener los mismos huespedes que lh
        assertEquals(comparadorOriginal, comparadorRetorno);
    }


    /**
     * Verifica la consistencia del filtrado comparando el resultado del gestor contra
     * un filtrado manual de la lista simulada.
     * <p>
     * Este test actúa como una "prueba de doble verificación", filtrando manualmente
     * la lista de mocks y asegurando que el método {@code buscarHuesped} produzca
     * exactamente el mismo subconjunto de objetos.
     * </p>
     */
    @Test
    public void buscarHuespedFiltraInvitadosDeListaAlojados(){
        List<Alojado> lista = listaDeMuchosAlojados();
        List<Alojado> listaSinInvitados = new ArrayList<>();
        lista.forEach(a -> {
            if(a instanceof Huesped) listaSinInvitados.add(a);
        });
        Set<CriteriosBusq> setAlojado = new HashSet<>(convertirACriterio(listaSinInvitados));
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class))).thenReturn(lista);
        List<CriteriosBusq> lconsulta = new ArrayList<>();
        try{
            lconsulta = gestor.buscarHuesped(crit);
        } catch (AlojadosSinCoincidenciasException e) {
            assertEquals(0, setAlojado.size());
        }
        Set<CriteriosBusq> setHuesped= new HashSet<>(lconsulta);
        assertEquals(setHuesped, setAlojado);
    }

    /**
     * Verifica que {@code obtenerCriteriosAlojado} filtre y retorne correctamente los alojados
     * basándose en el nombre.
     * <p>
     * Valida que, dado un criterio con nombre, el servicio devuelva todos los alojados
     * (sean Huéspedes o Invitados) que coincidan con dicho nombre.
     * </p>
     */
    @Test
    public void buscarAlojadoFiltraNombre(){
        var lista = listaDeMuchosAlojados();
        String nombreFiltro = lista.getFirst().getDatos().getDatos_personales().getNombre();
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class)))
                .thenAnswer(invocation -> {
                    CriteriosBusq critArgumento = invocation.getArgument(0);
                    return funcionMocoFiltro(critArgumento, lista);
                });
        CriteriosBusq critNombre = new CriteriosBusq(null,nombreFiltro,null,null);

        assertDoesNotThrow(()->gestor.obtenerCriteriosAlojado(critNombre));
        var listaBusqueda = gestor.obtenerCriteriosAlojado(critNombre);
        for(var a : listaBusqueda){
            assertEquals(a.getNombre(), nombreFiltro);
        }
    }


    /**
     * Verifica que {@code obtenerCriteriosAlojado} filtre y retorne correctamente los alojados
     * basándose en el apellido.
     * <p>
     * Comprueba que la lista resultante contenga únicamente alojados cuyo apellido
     * coincida con el criterio de búsqueda establecido.
     * </p>
     */
    @Test
    public void buscarAlojadoFiltraApellido(){
        var lista = listaDeMuchosAlojados();
        String apellidoFiltro = lista.getFirst().getDatos().getDatos_personales().getApellido();
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class)))
                .thenAnswer(invocation -> {
                    CriteriosBusq critArgumento = invocation.getArgument(0);
                    return funcionMocoFiltro(critArgumento, lista);
                });
        CriteriosBusq critApellido = new CriteriosBusq(apellidoFiltro,null,null,null);
        assertDoesNotThrow(()->gestor.obtenerCriteriosAlojado(critApellido));
        var listaBusqueda = gestor.obtenerCriteriosAlojado(critApellido);
        for(var a : listaBusqueda){
            assertEquals(a.getApellido(), apellidoFiltro);
        }
    }

    /**
     * Verifica que {@code obtenerCriteriosAlojado} filtre correctamente por tipo de documento.
     * <p>
     * Asegura que todos los elementos de la lista devuelta tengan el {@link TipoDoc}
     * especificado en el criterio de búsqueda.
     * </p>
     */
    @Test
    public void buscarAlojadoFiltraTipoDoc(){
        var lista = listaDeMuchosAlojados();
        TipoDoc tipoDocFiltro = lista.getFirst().getDatos().getDatos_personales().getTipoDoc();
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class)))
                .thenAnswer(invocation -> {
                    CriteriosBusq critArgumento = invocation.getArgument(0);
                    return funcionMocoFiltro(critArgumento, lista);
                });
        CriteriosBusq critTipoDoc = new CriteriosBusq(null,null,tipoDocFiltro,null);
        assertDoesNotThrow(()->gestor.obtenerCriteriosAlojado(critTipoDoc));
        var listaBusqueda = gestor.obtenerCriteriosAlojado(critTipoDoc);
        for(var a : listaBusqueda){
            assertEquals(a.getTipoDoc(), tipoDocFiltro);
        }
    }

    /**
     * Verifica que {@code obtenerCriteriosAlojado} filtre correctamente por número de documento.
     * <p>
     * Valida la coincidencia exacta del número de documento en los resultados devueltos
     * por el servicio.
     * </p>
     */
    @Test
    public void buscarAlojadoFiltraNroDoc(){
        var lista = listaDeMuchosAlojados();
        String nroDodFiltro = lista.getFirst().getDatos().getDatos_personales().getNroDoc();
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class)))
                .thenAnswer(invocation -> {
                    CriteriosBusq critArgumento = invocation.getArgument(0);
                    return funcionMocoFiltro(critArgumento, lista);
                });
        CriteriosBusq critNroDoc = new CriteriosBusq(null,null,null,nroDodFiltro);
        assertDoesNotThrow(()->gestor.obtenerCriteriosAlojado(critNroDoc));
        var listaBusqueda = gestor.obtenerCriteriosAlojado(critNroDoc);
        for(var a : listaBusqueda){
            assertEquals(a.getNroDoc(), nroDodFiltro);
        }
    }

    /**
     * Verifica que el método de búsqueda de huéspedes aplique correctamente el filtrado
     * por nombre.
     * <p>
     * Se simula un escenario donde el DAO retorna una lista mixta de alojados (Huéspedes e Invitados)
     * que coinciden con el nombre buscado. La prueba asegura que el gestor:
     * 1. Recibe la lista filtrada por nombre desde el DAO.
     * 2. Filtra internamente para descartar los objetos que no son de tipo Huesped.
     * </p>
     */
    @Test
    public void buscarHuespedFiltraNombre() {
        var listaCompleta = listaDeMuchosAlojados();
        String nombreFiltro = listaCompleta.getFirst().getDatos().getDatos_personales().getNombre();

        List<Alojado> respuestaDAO = listaCompleta.stream()
                .filter(a -> a.getDatos().getDatos_personales().getNombre().equals(nombreFiltro))
                .toList();

        List<Huesped> resultadoEntidades = respuestaDAO.stream()
                .filter(a -> a instanceof Huesped)
                .map(a -> (Huesped) a)
                .toList();

        var resultadoEsperadoCriterio = convertirACriterio(resultadoEntidades);

        CriteriosBusq crit = new CriteriosBusq(null, nombreFiltro, null, null);
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class))).thenReturn(respuestaDAO);


        if (resultadoEsperadoCriterio.isEmpty()) {
            assertThrows(AlojadosSinCoincidenciasException.class, () -> gestor.buscarHuesped(crit));
        } else {
            List<CriteriosBusq> resultadoReal = gestor.buscarHuesped(crit);
            assertEquals(resultadoEsperadoCriterio.size(), resultadoReal.size());
            assertTrue(resultadoReal.containsAll(resultadoEsperadoCriterio));
            // Verificar que todos sean Huesped y tengan el nombre correcto
            resultadoReal.forEach(h -> {
                assertEquals(nombreFiltro, h.getNombre());
            });
        }
    }

    /**
     * Verifica que el método de búsqueda de huéspedes aplique correctamente el filtrado
     * por apellido.
     * <p>
     * Valida que, dado un criterio de búsqueda por apellido, el gestor invoque al DAO
     * y posteriormente retorne únicamente las instancias de tipo Huesped que coincidan
     * con dicho apellido, excluyendo Invitados y otros tipos de Alojado.
     * </p>
     */
    @Test
    public void buscarHuespedFiltraApellido() {
        var listaCompleta = listaDeMuchosAlojados();
        String apellidoFiltro = listaCompleta.getFirst().getDatos().getDatos_personales().getApellido();

        List<Alojado> respuestaDAO = listaCompleta.stream()
                .filter(a -> a.getDatos().getDatos_personales().getApellido().equals(apellidoFiltro))
                .toList();

        List<Huesped> resultadoEntidades = respuestaDAO.stream()
                .filter(a -> a instanceof Huesped)
                .map(a -> (Huesped) a)
                .toList();

        var resultadoEsperadoCriterio = convertirACriterio(resultadoEntidades);

        CriteriosBusq crit = new CriteriosBusq(apellidoFiltro, null, null, null);
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class))).thenReturn(respuestaDAO);



        if (resultadoEsperadoCriterio.isEmpty()) {
            assertThrows(AlojadosSinCoincidenciasException.class, () -> gestor.buscarHuesped(crit));
        } else {
            var resultadoReal = gestor.buscarHuesped(crit);
            assertEquals(resultadoEsperadoCriterio.size(), resultadoReal.size());
            assertTrue(resultadoReal.containsAll(resultadoEsperadoCriterio));
            resultadoReal.forEach(h -> assertEquals(apellidoFiltro, h.getApellido()));
        }
    }

    /**
     * Verifica que el método de búsqueda de huéspedes aplique correctamente el filtrado
     * por tipo de documento.
     * <p>
     * Comprueba que al buscar por un {@link TipoDoc} específico, el resultado
     * contenga exclusivamente objetos Huesped con ese tipo de documento, manejando
     * correctamente la respuesta simulada del DAO.
     * </p>
     */
    @Test
    public void buscarHuespedFiltraTipoDoc() {
        var listaCompleta = listaDeMuchosAlojados();
        TipoDoc tipoDocFiltro = listaCompleta.getFirst().getDatos().getDatos_personales().getTipoDoc();

        List<Alojado> respuestaDAO = listaCompleta.stream()
                .filter(a -> a.getDatos().getDatos_personales().getTipoDoc() == tipoDocFiltro)
                .toList();

        List<Huesped> resultadoEntidades = respuestaDAO.stream()
                .filter(a -> a instanceof Huesped)
                .map(a -> (Huesped) a)
                .toList();

        var resultadoEsperadoCriterio = convertirACriterio(resultadoEntidades);

        CriteriosBusq crit = new CriteriosBusq(null, null, tipoDocFiltro, null);
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class))).thenReturn(respuestaDAO);



        if (resultadoEsperadoCriterio.isEmpty()) {
            assertThrows(AlojadosSinCoincidenciasException.class, () -> gestor.buscarHuesped(crit));
        } else {
            var resultadoReal = gestor.buscarHuesped(crit);
            assertEquals(resultadoEsperadoCriterio.size(), resultadoReal.size());
            resultadoReal.forEach(h -> assertEquals(tipoDocFiltro, h.getTipoDoc()));
        }
    }

    /**
     * Verifica que el método de búsqueda de huéspedes aplique correctamente el filtrado
     * por número de documento.
     * <p>
     * Asegura que la búsqueda por número de documento retorne la lista correcta de
     * Huéspedes, validando que el filtrado de tipo (Huesped vs Invitado) se realice
     * después de obtener las coincidencias de documento desde el componente de persistencia.
     * </p>
     */
    @Test
    public void buscarHuespedFiltraNroDoc() {
        var listaCompleta = listaDeMuchosAlojados();
        String nroDocFiltro = listaCompleta.getFirst().getDatos().getDatos_personales().getNroDoc();

        List<Alojado> respuestaDAO = listaCompleta.stream()
                .filter(a -> a.getDatos().getDatos_personales().getNroDoc().equals(nroDocFiltro))
                .toList();

        List<Huesped> resultadoEntidades = respuestaDAO.stream()
                .filter(a -> a instanceof Huesped)
                .map(a -> (Huesped) a)
                .toList();

        var resultadoEsperadoCriterio = convertirACriterio(resultadoEntidades);

        CriteriosBusq crit = new CriteriosBusq(null, null, null, nroDocFiltro);
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class))).thenReturn(respuestaDAO);


        if (resultadoEsperadoCriterio.isEmpty()) {
            assertThrows(AlojadosSinCoincidenciasException.class, () -> gestor.buscarHuesped(crit));
        } else {
            var resultadoReal = gestor.buscarHuesped(crit);
            assertEquals(resultadoEsperadoCriterio.size(), resultadoReal.size());
            resultadoReal.forEach(h -> assertEquals(nroDocFiltro, h.getNroDoc()));
        }
    }


    private List<Alojado> listaDeUnAlojado(){
        double aleatorioDouble = Math.random();
        int resultado = (int) (aleatorioDouble * 2);
        DatosAlojado da = GeneradorDatosAleatorios.generarDatosAlojadoAleatorio();
        Alojado a = FactoryAlojado.create(resultado, da);
        return listOf(a);
    }

    private List<Alojado> listaDeMuchosAlojados(){
        List<Alojado> la = new ArrayList<>();
        Random random = new Random();
        int n=random.nextInt(100)+1;
        for(int i=0;i<n;i++){
            la.addAll(listaDeUnAlojado());
        }
        Collections.shuffle(la);
        return la;
    }

    private List<Alojado> listaDeUnInvitado(){
        int tipo = 1;
        DatosAlojado da = GeneradorDatosAleatorios.generarDatosAlojadoAleatorio();
        Alojado a = FactoryAlojado.create(tipo, da);
        return listOf(a);
    }

    private List<Alojado> listaDeMuchosInvitados(){
        List<Alojado> li = new ArrayList<>();
        Random random = new Random();
        int n=random.nextInt(100)+1;
        for(int i=0;i<n;i++){
            li.addAll(listaDeUnInvitado());
        }
        Collections.shuffle(li);
        return li;
    }

    private List<Alojado> listaDeUnHuesped(){
        int tipo=0;
        DatosAlojado da = GeneradorDatosAleatorios.generarDatosAlojadoAleatorio();
        Alojado a = FactoryAlojado.create(tipo, da);
        return listOf(a);
    }

    private List<Alojado> listaDeMuchosHuespedes(){
        List<Alojado> lh = new ArrayList<>();
        Random random = new Random();
        int n=random.nextInt(100)+1;
        for(int i=0;i<n;i++){
            lh.addAll(listaDeUnHuesped());
        }
        Collections.shuffle(lh);
        return lh;
    }

    List<CriteriosBusq> convertirACriterio(List<? extends Alojado>lista){
        List<CriteriosBusq> respuestaDAO = new ArrayList<>();
        for(var a: lista){
            respuestaDAO.add(new CriteriosBusq(
                    a.getDatos().getDatos_personales().getApellido(),
                    a.getDatos().getDatos_personales().getNombre(),
                    a.getDatos().getDatos_personales().getTipoDoc(),
                    a.getDatos().getDatos_personales().getNroDoc()));
        }
        return respuestaDAO;
    }

}