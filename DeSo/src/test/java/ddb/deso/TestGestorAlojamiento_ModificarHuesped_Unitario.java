package ddb.deso;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.negocio.TipoDoc;
import ddb.deso.negocio.alojamiento.Alojado;
import ddb.deso.negocio.alojamiento.FactoryAlojado;
import ddb.deso.service.GestorAlojamiento;
import ddb.deso.service.excepciones.AlojadoInvalidoException;
import ddb.deso.service.excepciones.AlojadoPreExistenteException;

@ExtendWith(MockitoExtension.class)
public class TestGestorAlojamiento_ModificarHuesped_Unitario {

    @Mock
    private AlojadoDAO alojadoDAO;

    @InjectMocks
    private GestorAlojamiento gestor;

    private AlojadoDTO crearDTOValido(String dni) {
        AlojadoDTO ret = new AlojadoDTO();
        ret.setApellido("Perez");
        ret.setNombre("Juan");
        ret.setNacionalidad("argentino");
        ret.setFechanac("2000-01-01");
        ret.setTipoDoc(TipoDoc.DNI);
        ret.setNroDoc(dni);
        ret.setTelefono("111");
        ret.setEmail("a@a.com");
        ret.setCalle("Calle");
        ret.setNroCalle("1");
        ret.setPiso("1");
        ret.setCodPost("3000");
        ret.setPais("AR");
        ret.setProv("SF");
        ret.setLocalidad("SF");
        ret.setOcupacion("Estudiante");
        ret.setCUIT("20123456789");
        ret.setPosicionIva("Consumidor Final");
        return ret;
    }

    @Test
    public void modificarHuesped_lanzaSiOriginalNull() {
        AlojadoDTO mod = crearDTOValido("222");
        assertThrows(AlojadoInvalidoException.class, () -> gestor.modificarHuesped(null, mod, false));
    }

    @Test
    public void modificarHuesped_lanzaSiModificadoNull() {
        AlojadoDTO orig = crearDTOValido("111");
        assertThrows(AlojadoInvalidoException.class, () -> gestor.modificarHuesped(orig, null, false));
    }

    @Test
    public void modificarHuesped_lanzaPreExistente_siNoForzar_yCambiaIdentidad_yDniExisteTrue() throws Exception {
        AlojadoDTO orig = crearDTOValido("111");
        AlojadoDTO mod  = crearDTOValido("222"); // cambia DNI

        // Spy para mockear dniExiste() sin entrar a buscarAlojado sobrecargado
        GestorAlojamiento spy = Mockito.spy(new GestorAlojamiento(alojadoDAO));
        doReturn(true).when(spy).dniExiste(eq("222"), eq(TipoDoc.DNI));

        assertThrows(AlojadoPreExistenteException.class, () -> spy.modificarHuesped(orig, mod, false));
    }

    @Test
    public void modificarHuesped_caminoFeliz_retornaDTO() throws Exception, AlojadoPreExistenteException {
        AlojadoDTO orig = crearDTOValido("111");
        AlojadoDTO mod  = crearDTOValido("111");
        mod.setNombre("NombreNuevo"); // cambia algún dato no clave

        doNothing().when(alojadoDAO).actualizarAlojado(any(Alojado.class), any(Alojado.class));

        // entidad "guardada" que comparteDatos => true
        Alojado guardadoReal = FactoryAlojado.createFromDTO(mod);
        Alojado guardadoSpy = Mockito.spy(guardadoReal);
        doReturn(true).when(guardadoSpy).comparteDatos(any(Alojado.class));

        when(alojadoDAO.buscarPorDNI(eq(mod.getNroDoc()), eq(mod.getTipoDoc()))).thenReturn(guardadoSpy);

        AlojadoDTO respuesta = gestor.modificarHuesped(orig, mod, false);
        assertNotNull(respuesta);
        assertEquals(mod.getNroDoc(), respuesta.getNroDoc());
        assertEquals(mod.getTipoDoc(), respuesta.getTipoDoc());
    }

    @Test
    public void modificarHuesped_lanzaSiBuscarPorDniDevuelveNull() {
        AlojadoDTO orig = crearDTOValido("111");
        AlojadoDTO mod  = crearDTOValido("111");
        mod.setNombre("X");

        doNothing().when(alojadoDAO).actualizarAlojado(any(Alojado.class), any(Alojado.class));
        when(alojadoDAO.buscarPorDNI(eq(mod.getNroDoc()), eq(mod.getTipoDoc()))).thenReturn(null);

        assertThrows(AlojadoInvalidoException.class, () -> gestor.modificarHuesped(orig, mod, false));
    }

    @Test
    public void modificarHuesped_lanzaSiNoComparteDatos() {
        AlojadoDTO orig = crearDTOValido("111");
        AlojadoDTO mod  = crearDTOValido("111");
        mod.setNombre("X");

        doNothing().when(alojadoDAO).actualizarAlojado(any(Alojado.class), any(Alojado.class));

        Alojado guardadoReal = FactoryAlojado.createFromDTO(mod);
        Alojado guardadoSpy = Mockito.spy(guardadoReal);
        doReturn(false).when(guardadoSpy).comparteDatos(any(Alojado.class));

        when(alojadoDAO.buscarPorDNI(eq(mod.getNroDoc()), eq(mod.getTipoDoc()))).thenReturn(guardadoSpy);

        assertThrows(AlojadoInvalidoException.class, () -> gestor.modificarHuesped(orig, mod, false));
    }
}
