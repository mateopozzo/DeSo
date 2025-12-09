package ddb.deso;

import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DTO.CriteriosBusq;
import ddb.deso.gestores.GestorAlojamiento;
import ddb.deso.gestores.excepciones.AlojadosSinCoincidenciasException;
import ddb.deso.service.TipoDoc;
import ddb.deso.service.alojamiento.*;
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
     * método {@code buscarAlojado}.
     * </p>
     */
    @Test public void buscarAlojadoArrojaExcepcionSiDAODevuelveNull(){
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class))).thenReturn(null);
        assertThrows(AlojadosSinCoincidenciasException.class, ()->gestor.buscarAlojado(crit));
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
     * Valida que el método {@code buscarAlojado} propague la excepción de negocio
     * adecuada cuando la búsqueda no arroja resultados.
     * </p>
     */
    @Test public void buscarAlojadoArrojaExcepcionSiDAODevuelveVacio(){
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class))).thenReturn(new ArrayList<Alojado>());
        assertThrows(AlojadosSinCoincidenciasException.class, ()->gestor.buscarAlojado(crit));
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
     * Verifica que {@code buscarAlojado} filtre y retorne correctamente los alojados
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
        assertDoesNotThrow(()->gestor.buscarAlojado(critApellido));
        var listaBusqueda = gestor.buscarAlojado(critApellido);
        for(var a : listaBusqueda){
            assert(lista.contains(a));
            assertEquals(a.getApellido(), apellidoFiltro);
        }
    }

    /**
     * Verifica que {@code buscarAlojado} filtre correctamente por tipo de documento.
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
        assertDoesNotThrow(()->gestor.buscarAlojado(critTipoDoc));
        var listaBusqueda = gestor.buscarAlojado(critTipoDoc);
        for(var a : listaBusqueda){
            assert(lista.contains(a));
            assertEquals(a.getTipoDoc(), tipoDocFiltro);
        }
    }

    /**
     * Verifica que {@code buscarAlojado} filtre correctamente por número de documento.
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
        assertDoesNotThrow(()->gestor.buscarAlojado(critNroDoc));
        var listaBusqueda = gestor.buscarAlojado(critNroDoc);
        for(var a : listaBusqueda){
            assert(lista.contains(a));
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

        List<Alojado> alojadosFiltrados = listaCompleta.stream()
                .filter(a -> a.getDatos().getDatos_personales().getNombre().equals(nombreFiltro))
                .toList();

        CriteriosBusq crit = new CriteriosBusq(null, nombreFiltro, null, null);
        when(mockitoDAO.buscarAlojado(any(CriteriosBusq.class))).thenReturn(alojadosFiltrados);

        if (alojadosFiltrados.isEmpty()) {
            assertThrows(AlojadosSinCoincidenciasException.class, () -> gestor.buscarHuesped(crit));
        } else {
            List<CriteriosBusq> resultadoReal = gestor.buscarHuesped(crit);
            assertEquals(alojadosFiltrados.size(), resultadoReal.size());
            assertTrue(resultadoReal.containsAll(alojadosFiltrados));
            // Verificar que todos sean Huesped y tengan el nombre correcto
            resultadoReal.forEach(h -> {
                assertEquals(nombreFiltro, h.getNombre());
            });
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

}
