package ddb.deso;

import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.almacenamiento.DTO.CriteriosBusq;
import ddb.deso.negocio.TipoDoc;
import ddb.deso.negocio.alojamiento.Alojado;
import ddb.deso.negocio.alojamiento.FactoryAlojado;
import ddb.deso.service.GestorAlojamiento;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestGestorAlojamiento_DniYObtenerPorDni_Unitario {

    @Mock
    private AlojadoDAO alojadoDAO;

    @InjectMocks
    private GestorAlojamiento gestor;

    private AlojadoDTO crearAlojadoDTOValido() {
        AlojadoDTO ret = new AlojadoDTO();
        ret.setApellido("Perez");
        ret.setNombre("Juan");
        ret.setNacionalidad("argentino");
        ret.setFechanac("2000-01-01");
        ret.setTipoDoc(TipoDoc.DNI);
        ret.setNroDoc("12345678");
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

    // ---------------- dniExiste ----------------

    @Test
    public void dniExiste_devuelveFalse_siTipoNull() {
        assertFalse(gestor.dniExiste("123", null));
    }

    @Test
    public void dniExiste_devuelveFalse_siDniNull() {
        assertFalse(gestor.dniExiste(null, TipoDoc.DNI));
    }

    @Test
    public void dniExiste_devuelveFalse_siDniBlank() {
        assertFalse(gestor.dniExiste("   ", TipoDoc.DNI));
    }

    @Test
    public void dniExiste_devuelveFalse_siDaoDevuelveNull() {
        when(alojadoDAO.buscarAlojado(Mockito.<CriteriosBusq>any())).thenReturn(null);
        assertFalse(gestor.dniExiste("123", TipoDoc.DNI));
    }

    @Test
    public void dniExiste_devuelveFalse_siDaoDevuelveListaVacia() {
        when(alojadoDAO.buscarAlojado(Mockito.<CriteriosBusq>any())).thenReturn(List.of());
        assertFalse(gestor.dniExiste("123", TipoDoc.DNI));
    }

    @Test
    public void dniExiste_devuelveTrue_siDaoDevuelveAlMenosUno() {
        AlojadoDTO dto = crearAlojadoDTOValido();
        Alojado entidad = FactoryAlojado.createFromDTO(dto);

        when(alojadoDAO.buscarAlojado(Mockito.<CriteriosBusq>any())).thenReturn(List.of(entidad));
        assertTrue(gestor.dniExiste(dto.getNroDoc(), dto.getTipoDoc()));
    }

    // ---------------- obtenerAlojadoPorDNI ----------------

    @Test
    public void obtenerAlojadoPorDNI_retornaNull_siParametrosInvalidos() {
        assertNull(gestor.obtenerAlojadoPorDNI(null, TipoDoc.DNI));
        assertNull(gestor.obtenerAlojadoPorDNI(" ", TipoDoc.DNI));
        assertNull(gestor.obtenerAlojadoPorDNI("123", null));
    }

    @Test
    public void obtenerAlojadoPorDNI_retornaDTO_siDaoDevuelveListaConEntidad() {
        AlojadoDTO dto = crearAlojadoDTOValido();
        Alojado entidad = FactoryAlojado.createFromDTO(dto);

        when(alojadoDAO.buscarAlojado(Mockito.<CriteriosBusq>any())).thenReturn(List.of(entidad));

        AlojadoDTO respuesta = gestor.obtenerAlojadoPorDNI(dto.getNroDoc(), dto.getTipoDoc());
        assertNotNull(respuesta);
        assertEquals(dto.getNroDoc(), respuesta.getNroDoc());
        assertEquals(dto.getTipoDoc(), respuesta.getTipoDoc());
    }
}
