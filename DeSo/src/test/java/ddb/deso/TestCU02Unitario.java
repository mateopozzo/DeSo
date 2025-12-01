package ddb.deso;

import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.alojamiento.*;
import ddb.deso.gestores.GestorAlojamiento;
import ddb.deso.gestores.excepciones.AlojadosSinCoincidenciasException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.*;

import static org.hibernate.internal.util.collections.CollectionHelper.listOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TestCU02Unitario {
    private FactoryAlojado FA;

    @InjectMocks
    private GestorAlojamiento gestor;

    @Mock
    AlojadoDAO mockitoDAO;

    @Test
    public void buscarHuespedArrojaExcepcionSiDAODevuelveNull(){
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.listarAlojados()).thenReturn(null);
        assertThrows(AlojadosSinCoincidenciasException.class, ()->gestor.buscarHuesped(crit));
    }

    @Test public void buscarAlojadoArrojaExcepcionSiDAODevuelveNull(){
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.listarAlojados()).thenReturn(null);
        assertThrows(AlojadosSinCoincidenciasException.class, ()->gestor.buscarAlojado(crit));
    }

    @Test public void buscarHuespedArrojaExcepcionSiDAODevuelveVacio(){
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.listarAlojados()).thenReturn(new ArrayList<Alojado>());
        assertThrows(AlojadosSinCoincidenciasException.class, ()->gestor.buscarHuesped(crit));
    }

    @Test public void buscarAlojadoArrojaExcepcionSiDAODevuelveVacio(){
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.listarAlojados()).thenReturn(new ArrayList<Alojado>());
        assertThrows(AlojadosSinCoincidenciasException.class, ()->gestor.buscarAlojado(crit));
    }

    @Test
    public void buscarHuespedNoEncuentraHuespedes(){
        var li = listaDeMuchosInvitados();
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.listarAlojados()).thenReturn(li);
        assertThrows(AlojadosSinCoincidenciasException.class,()->gestor.buscarHuesped(crit));
    }

    @Test
    public void buscarHuespedFiltraUnInvitado(){
        var lh = listaDeMuchosHuespedes();
        var li = listaDeUnInvitado();
        var lfinal = lh;
        lfinal.addAll(li);
        Collections.shuffle(lfinal);
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.listarAlojados()).thenReturn(lfinal);
        assertDoesNotThrow(()->gestor.buscarHuesped(crit), "No encontro ni un huesped");
        var lconsulta = gestor.buscarHuesped(crit);
        Set<Alojado> comparadorRetorno = new HashSet<Alojado>(lconsulta);
        Set<Alojado> comparadorOriginal = new HashSet<Alojado>(lh);
        //El retorno de la consulta tiene que contener los mismos huespedes que lh
        assertEquals(comparadorOriginal, comparadorRetorno);
    }

    @Test
    public void buscarHuespedFiltraMuchosInvitados() {
        var lh = listaDeMuchosHuespedes();
        var li = listaDeMuchosInvitados();
        var lfinal = lh;
        lfinal.addAll(li);
        Collections.shuffle(lfinal);
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.listarAlojados()).thenReturn(lfinal);
        assertDoesNotThrow(() -> gestor.buscarHuesped(crit), "No encontro ni un huesped");
        var lconsulta = gestor.buscarHuesped(crit);
        Set<Alojado> comparadorRetorno = new HashSet<Alojado>(lconsulta);
        Set<Alojado> comparadorOriginal = new HashSet<Alojado>(lh);
        //El retorno de la consulta tiene que contener los mismos huespedes que lh
        assertEquals(comparadorOriginal, comparadorRetorno);
    }



    @Test
    public void buscarHuespedFiltraInvitadosDeListaAlojados(){
        List<Alojado> lista = listaDeMuchosInvitados();
        Set<Alojado> setAlojado = new HashSet<>();
        for(var a : lista) if(a instanceof Huesped) {
            setAlojado.add(a);
        }
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.listarAlojados()).thenReturn(lista);
        List<? extends Alojado> lconsulta = new ArrayList<>();
        try{
            lconsulta = gestor.buscarHuesped(crit);
        } catch (AlojadosSinCoincidenciasException e) {
            assertEquals(0, setAlojado.size());
        }
        Set<Alojado> setHuesped= new HashSet<Alojado>(lconsulta);
        assertEquals(setHuesped, setAlojado);
    }

    @Test
    public void buscarAlojadoFiltraNombre(){
        var lista = listaDeMuchosAlojados();
        String nombreFiltro = lista.getFirst().getDatos().getDatos_personales().getNombre();
        when(mockitoDAO.listarAlojados()).thenReturn(lista);
        CriteriosBusq critNombre = new CriteriosBusq(null,nombreFiltro,null,null);
        assertDoesNotThrow(()->gestor.buscarAlojado(critNombre));
        var listaBusqueda = gestor.buscarAlojado(critNombre);
        for(var a : listaBusqueda){
            assert(lista.contains(a));
            assertEquals(a.getDatos().getDatos_personales().getNombre(), nombreFiltro);
        }
    }

    @Test
    public void buscarAlojadoFiltraApellido(){
        var lista = listaDeMuchosAlojados();
        String apellidoFiltro = lista.getFirst().getDatos().getDatos_personales().getApellido();
        when(mockitoDAO.listarAlojados()).thenReturn(lista);
        CriteriosBusq critApellido = new CriteriosBusq(apellidoFiltro,null,null,null);
        assertDoesNotThrow(()->gestor.buscarAlojado(critApellido));
        var listaBusqueda = gestor.buscarAlojado(critApellido);
        for(var a : listaBusqueda){
            assert(lista.contains(a));
            assertEquals(a.getDatos().getDatos_personales().getApellido(), apellidoFiltro);
        }
    }

    @Test
    public void buscarAlojadoFiltraTipoDoc(){
        var lista = listaDeMuchosAlojados();
        TipoDoc tipoDocFiltro = lista.getFirst().getDatos().getDatos_personales().getTipoDoc();
        when(mockitoDAO.listarAlojados()).thenReturn(lista);
        CriteriosBusq critTipoDoc = new CriteriosBusq(null,null,tipoDocFiltro,null);
        assertDoesNotThrow(()->gestor.buscarAlojado(critTipoDoc));
        var listaBusqueda = gestor.buscarAlojado(critTipoDoc);
        for(var a : listaBusqueda){
            assert(lista.contains(a));
            assertEquals(a.getDatos().getDatos_personales().getTipoDoc(), tipoDocFiltro);
        }
    }

    @Test
    public void buscarAlojadoFiltraNroDoc(){
        var lista = listaDeMuchosAlojados();
        String nroDodFiltro = lista.getFirst().getDatos().getDatos_personales().getNroDoc();
        when(mockitoDAO.listarAlojados()).thenReturn(lista);
        CriteriosBusq critNroDoc = new CriteriosBusq(null,null,null,nroDodFiltro);
        assertDoesNotThrow(()->gestor.buscarAlojado(critNroDoc));
        var listaBusqueda = gestor.buscarAlojado(critNroDoc);
        for(var a : listaBusqueda){
            assert(lista.contains(a));
            assertEquals(a.getDatos().getDatos_personales().getNroDoc(), nroDodFiltro);
        }
    }

    @Test
    public void buscarHuespedFiltraNombre() {
        var listaCompleta = listaDeMuchosAlojados();
        String nombreFiltro = listaCompleta.getFirst().getDatos().getDatos_personales().getNombre();

        List<Alojado> respuestaDAO = listaCompleta.stream()
                .filter(a -> a.getDatos().getDatos_personales().getNombre().equals(nombreFiltro))
                .toList();


        List<Huesped> resultadoEsperado = respuestaDAO.stream()
                .filter(a -> a instanceof Huesped)
                .map(a -> (Huesped) a)
                .toList();

        CriteriosBusq crit = new CriteriosBusq(null, nombreFiltro, null, null);
        when(mockitoDAO.buscarAlojadoDAO(crit)).thenReturn(respuestaDAO);


        if (resultadoEsperado.isEmpty()) {
            assertThrows(AlojadosSinCoincidenciasException.class, () -> gestor.buscarHuesped(crit));
        } else {
            List<Huesped> resultadoReal = gestor.buscarHuesped(crit);
            assertEquals(resultadoEsperado.size(), resultadoReal.size());
            assertTrue(resultadoReal.containsAll(resultadoEsperado));
            // Verificar que todos sean Huesped y tengan el nombre correcto
            resultadoReal.forEach(h -> {
                assertEquals(nombreFiltro, h.getDatos().getDatos_personales().getNombre());
                assertInstanceOf(Huesped.class, h);
            });
        }
    }

    @Test
    public void buscarHuespedFiltraApellido() {
        var listaCompleta = listaDeMuchosAlojados();
        String apellidoFiltro = listaCompleta.getFirst().getDatos().getDatos_personales().getApellido();

        List<Alojado> respuestaDAO = listaCompleta.stream()
                .filter(a -> a.getDatos().getDatos_personales().getApellido().equals(apellidoFiltro))
                .toList();

        List<Huesped> resultadoEsperado = respuestaDAO.stream()
                .filter(a -> a instanceof Huesped)
                .map(a -> (Huesped) a)
                .toList();

        CriteriosBusq crit = new CriteriosBusq(apellidoFiltro, null, null, null);
        when(mockitoDAO.buscarAlojadoDAO(crit)).thenReturn(respuestaDAO);


        if (resultadoEsperado.isEmpty()) {
            assertThrows(AlojadosSinCoincidenciasException.class, () -> gestor.buscarHuesped(crit));
        } else {
            List<Huesped> resultadoReal = gestor.buscarHuesped(crit);
            assertEquals(resultadoEsperado.size(), resultadoReal.size());
            assertTrue(resultadoReal.containsAll(resultadoEsperado));
            resultadoReal.forEach(h -> assertEquals(apellidoFiltro, h.getDatos().getDatos_personales().getApellido()));
        }
    }

    @Test
    public void buscarHuespedFiltraTipoDoc() {
        var listaCompleta = listaDeMuchosAlojados();
        TipoDoc tipoDocFiltro = listaCompleta.getFirst().getDatos().getDatos_personales().getTipoDoc();

        List<Alojado> respuestaDAO = listaCompleta.stream()
                .filter(a -> a.getDatos().getDatos_personales().getTipoDoc() == tipoDocFiltro)
                .toList();

        List<Huesped> resultadoEsperado = respuestaDAO.stream()
                .filter(a -> a instanceof Huesped)
                .map(a -> (Huesped) a)
                .toList();

        CriteriosBusq crit = new CriteriosBusq(null, null, tipoDocFiltro, null);
        when(mockitoDAO.buscarAlojadoDAO(crit)).thenReturn(respuestaDAO);


        if (resultadoEsperado.isEmpty()) {
            assertThrows(AlojadosSinCoincidenciasException.class, () -> gestor.buscarHuesped(crit));
        } else {
            List<Huesped> resultadoReal = gestor.buscarHuesped(crit);
            assertEquals(resultadoEsperado.size(), resultadoReal.size());
            resultadoReal.forEach(h -> assertEquals(tipoDocFiltro, h.getDatos().getDatos_personales().getTipoDoc()));
        }
    }

    @Test
    public void buscarHuespedFiltraNroDoc() {
        var listaCompleta = listaDeMuchosAlojados();
        String nroDocFiltro = listaCompleta.getFirst().getDatos().getDatos_personales().getNroDoc();

        List<Alojado> respuestaDAO = listaCompleta.stream()
                .filter(a -> a.getDatos().getDatos_personales().getNroDoc().equals(nroDocFiltro))
                .toList();

        List<Huesped> resultadoEsperado = respuestaDAO.stream()
                .filter(a -> a instanceof Huesped)
                .map(a -> (Huesped) a)
                .toList();

        CriteriosBusq crit = new CriteriosBusq(null, null, null, nroDocFiltro);
        when(mockitoDAO.buscarAlojadoDAO(crit)).thenReturn(respuestaDAO);


        if (resultadoEsperado.isEmpty()) {
            assertThrows(AlojadosSinCoincidenciasException.class, () -> gestor.buscarHuesped(crit));
        } else {
            List<Huesped> resultadoReal = gestor.buscarHuesped(crit);
            assertEquals(resultadoEsperado.size(), resultadoReal.size());
            resultadoReal.forEach(h -> assertEquals(nroDocFiltro, h.getDatos().getDatos_personales().getNroDoc()));
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
