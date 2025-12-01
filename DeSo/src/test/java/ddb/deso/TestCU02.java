package ddb.deso;

//import static ddb.deso.gestores.GestorAlojamiento.buscarAlojado;

import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.alojamiento.*;
import ddb.deso.gestores.GestorAlojamiento;
import ddb.deso.gestores.excepciones.AlojadosSinCoincidenciasException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.hibernate.internal.util.collections.CollectionHelper.listOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class TestCU02 {

    private FactoryAlojado FA;

    @Autowired
    private GestorAlojamiento gestorAlojamiento;

    @Autowired
    public TestCU02(GestorAlojamiento gestorAlojamiento){
        this.gestorAlojamiento = gestorAlojamiento;
    }

    @MockitoBean
    AlojadoDAO mockitoDAO;

    @Test
    public void busquedaRegular() {
        CriteriosBusq crit2 = new CriteriosBusq("Gomez", "Juan", TipoDoc.LE, "47183532");
        assertDoesNotThrow(()->gestorAlojamiento.buscarHuesped(crit2), "Encuentra al menos un resultado");
        var lista = gestorAlojamiento.buscarHuesped(crit2);
        assertEquals(1, lista.size());
        assertEquals("GOMEZ", lista.getFirst().getDatos().getDatos_personales().getApellido());
        assertEquals("JUAN", lista.getFirst().getDatos().getDatos_personales().getNombre());
        assertEquals(TipoDoc.LE, lista.getFirst().getDatos().getDatos_personales().getTipoDoc());
        assertEquals("47183532", lista.getFirst().getDatos().getDatos_personales().getNroDoc());
    }

    @Test
    public void busquedaConTildes  (){
        CriteriosBusq crit5 = new CriteriosBusq("Fernández", "Domingo", TipoDoc.DNI, "87800466");
        assertDoesNotThrow(()->gestorAlojamiento.buscarHuesped(crit5), "Encuentra al menos un resultado");
        var lista = gestorAlojamiento.buscarHuesped(crit5);
        assertEquals(1, lista.size());
        assertEquals("FERNANDEZ", lista.getFirst().getDatos().getDatos_personales().getApellido());
        assertEquals("DOMINGO", lista.getFirst().getDatos().getDatos_personales().getNombre());
        assertEquals(TipoDoc.DNI, lista.getFirst().getDatos().getDatos_personales().getTipoDoc());
        assertEquals("87800466", lista.getFirst().getDatos().getDatos_personales().getNroDoc());
    }

    @Test
    public void busquedaCriterioUnicoNombre() {
        CriteriosBusq crit3 = new CriteriosBusq(null,"Juan", null, null);
        assertDoesNotThrow(()->gestorAlojamiento.buscarHuesped(crit3), "Encuentra al menos un resultado");
        var lista = gestorAlojamiento.buscarHuesped(crit3);
        assertNotEquals(1, lista.size());
        for(var h : lista){
            assert(h instanceof Huesped);
            assertEquals("JUAN", h.getDatos().getDatos_personales().getNombre());
        }
    }

    @Test
    public void busquedaCriterioUnicoDocumento() {
        CriteriosBusq crit4 = new CriteriosBusq(null,null, null, "45510538");
        assertDoesNotThrow(()->gestorAlojamiento.buscarHuesped(crit4), "Encuentra al menos un resultado");
        var lista = gestorAlojamiento.buscarHuesped(crit4);
        assertNotEquals(1, lista.size());
        for(var h : lista){
            assert(h instanceof Huesped);
            assertEquals("45510538", h.getDatos().getDatos_personales().getNroDoc());
        }
    }

    @Test
    public void buscarSinExito(){
        /**SIN COINCIDENCIAS ---------------------------------------------------")*/
        //System.out.println("BÚSQUEDA: Perez Mateo LC 3 ---------\n");
        CriteriosBusq crit1 = new CriteriosBusq("Perez","Carlos", TipoDoc.LC, "3");
        when(mockitoDAO.buscarAlojadoDAO(crit1)).thenReturn(null);
        GestorAlojamiento gestorVacio = new GestorAlojamiento(mockitoDAO);
        assertThrows(
                AlojadosSinCoincidenciasException.class,
                () -> gestorVacio.buscarHuesped(crit1)
        );
    }

    @Test
    public void buscarTodos() {
        /**"ENCUENTRA TODOS ---------------------------------------------------");*/
        //System.out.println("BÚSQUEDA: Todos los campos null ---------\n");
        CriteriosBusq crit7 = new CriteriosBusq(null,null, null, null);
        assertDoesNotThrow(()->gestorAlojamiento.buscarHuesped(crit7), "Encuentra al menos un resultado");
        var lista = gestorAlojamiento.buscarHuesped(crit7);
        assertEquals(32, lista.size());
    }

    @Test
    public void buscarHuespedFiltraUnInvitado(){
        var lh = listaDeMuchosHuespedes();
        var li = listaDeUnInvitado();
        var lfinal = lh;
        lfinal.addAll(li);
        Collections.shuffle(lfinal);
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.buscarAlojadoDAO(crit)).thenReturn(lfinal);
        GestorAlojamiento gestor = new GestorAlojamiento(mockitoDAO);
        var lconsulta = gestor.buscarHuesped(crit);
        Set<Alojado> comparadorRetorno = new HashSet<Alojado>(lconsulta);
        Set<Alojado> comparadorOriginal = new HashSet<Alojado>(lh);
        //El retorno de la consulta tiene que contener los mismos huespedes que lh
        assertEquals(comparadorOriginal, comparadorRetorno);
    }

    @Test
    public void buscarHuespedFiltraMuchosInvitados(){
        var lh = listaDeMuchosHuespedes();
        var li = listaDeMuchosInvitados();
        var lfinal = lh;
        lfinal.addAll(li);
        Collections.shuffle(lfinal);
        CriteriosBusq crit = new CriteriosBusq();
        when(mockitoDAO.buscarAlojadoDAO(crit)).thenReturn(lfinal);
        GestorAlojamiento gestor = new GestorAlojamiento(mockitoDAO);
        var lconsulta = gestor.buscarHuesped(crit);
        Set<Alojado> comparadorRetorno = new HashSet<Alojado>(lconsulta);
        Set<Alojado> comparadorOriginal = new HashSet<Alojado>(lh);
        //El retorno de la consulta tiene que contener los mismos huespedes que lh
        assertEquals(comparadorOriginal, comparadorRetorno);
    }

    @Test
    public void buscarHuespedFiltraInvitadosDeListaAlojados(){
        List<Alojado> lista = listaDeMuchosInvitados();
        Set<Alojado> set = new HashSet<>();
        for(var a : lista) if(a instanceof Huesped) set.add(a);
        when(mockitoDAO.buscarAlojadoDAO())
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
        int n=random.nextInt(10)+1;
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
        int n=random.nextInt(10)+1;
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
        int n=random.nextInt(10)+1;
        for(int i=0;i<n;i++){
            lh.addAll(listaDeUnHuesped());
        }
        Collections.shuffle(lh);
        return lh;
    }

}
