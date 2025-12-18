package ddb.deso;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DTO.CriteriosBusq;
import ddb.deso.almacenamiento.DTO.DatosCheckOutDTO;
import ddb.deso.almacenamiento.DTO.PersonaJuridicaDTO;
import ddb.deso.negocio.TipoDoc;
import ddb.deso.negocio.alojamiento.Alojado;
import ddb.deso.negocio.alojamiento.DatosAlojado;
import ddb.deso.negocio.alojamiento.DatosContacto;
import ddb.deso.negocio.alojamiento.DatosPersonales;
import ddb.deso.negocio.alojamiento.DatosResidencia;
import ddb.deso.negocio.alojamiento.Huesped;
import ddb.deso.negocio.alojamiento.Invitado;
import ddb.deso.service.GestorAlojamiento;
import ddb.deso.service.excepciones.AlojadoInvalidoException;

@ExtendWith(MockitoExtension.class)
public class TestGestorAlojamiento_CheckOut_Estadia_Cuit_Unitario {

    @Mock
    private AlojadoDAO alojadoDAO;

    @InjectMocks
    private GestorAlojamiento gestor;

    // ---------------- generarCheckOut ----------------

    @Test
    public void generarCheckOut_lanzaSiListaNull() {
        assertThrows(AlojadoInvalidoException.class, () -> gestor.generarCheckOut(null));
    }

    @Test
    public void generarCheckOut_retornaNull_siEncuentraCriterioValidoPeroEntidadNull() {
        var crit = new CriteriosBusq(null, null, TipoDoc.DNI, "123");
        when(alojadoDAO.buscarPorDNI("123", TipoDoc.DNI)).thenReturn(null);

        DatosCheckOutDTO dto = gestor.generarCheckOut(List.of(crit));
        assertNull(dto);
    }

    @Test
    public void generarCheckOut_caminoFeliz_retornaDTO() {
        var crit = new CriteriosBusq(null, null, TipoDoc.DNI, "123");

        // una entidad real para que nuevoCheckOut no explote
        Alojado entidad = crearInvitadoConCuit("20333444556", "123", TipoDoc.DNI);
        when(alojadoDAO.buscarPorDNI("123", TipoDoc.DNI)).thenReturn(entidad);

        DatosCheckOutDTO dto = gestor.generarCheckOut(List.of(crit));
        assertNotNull(dto);
    }

    @Test
    public void generarCheckOut_noExplota_yDevuelveDTO_siHayAlMenosUnCriterioValido() {
        var valido = new CriteriosBusq(null, null, TipoDoc.DNI, "123");

        Alojado entidad = crearInvitadoConCuit("20333444556", "123", TipoDoc.DNI);
        when(alojadoDAO.buscarPorDNI("123", TipoDoc.DNI)).thenReturn(entidad);

        var criterios = new java.util.ArrayList<CriteriosBusq>();
        criterios.add(null);
        criterios.add(new CriteriosBusq(null, null, TipoDoc.DNI, "")); // inválido (nroDoc vacío)
        criterios.add(new CriteriosBusq(null, null, null, "999"));     // inválido (tipoDoc null)
        criterios.add(valido);                                         // válido

        DatosCheckOutDTO dto = gestor.generarCheckOut(criterios);

        assertNotNull(dto);
    }



    // ---------------- buscarCriteriosALojadoDeEstadia ----------------

    @Test
    public void buscarCriteriosALojadoDeEstadia_mapeaLista() {
        long idEstadia = 99L;

        Alojado a1 = crearInvitadoConCuit("20111111111", "111", TipoDoc.DNI);
        Alojado a2 = crearInvitadoConCuit("20222222222", "222", TipoDoc.DNI);

        when(alojadoDAO.buscarAlojado(idEstadia)).thenReturn(List.of(a1, a2));

        var lista = gestor.buscarCriteriosALojadoDeEstadia(idEstadia);
        assertEquals(2, lista.size());
        assertEquals("111", lista.get(0).getNroDoc());
        assertEquals("222", lista.get(1).getNroDoc());
    }

    // ---------------- buscarCriteriosAlojadoPorCuit ----------------
//
//    @Test
//    public void buscarCriteriosAlojadoPorCuit_retornaNull_siCuitNullOVacio() {
//        assertNull(gestor.buscarCriteriosAlojadoPorCuit(null));
//        assertNull(gestor.buscarCriteriosAlojadoPorCuit(""));
//    }
//
//    @Test
//    public void buscarCriteriosAlojadoPorCuit_retornaNull_siDaoDevuelveNull() {
//        when(alojadoDAO.buscarAlojado("20")).thenReturn(null);
//        assertNull(gestor.buscarCriteriosAlojadoPorCuit("20"));
//    }
//
//    @Test
//    public void buscarCriteriosAlojadoPorCuit_promueveSiEsInvitado_yRetornaDTO() {
//        String cuit = "20333444556";
//        String dni = "123";
//        TipoDoc tipo = TipoDoc.DNI;
//
//        Alojado invitado = crearInvitadoConCuit(cuit, dni, tipo);
//        when(alojadoDAO.buscarAlojado(cuit)).thenReturn(invitado);
//
//        // al promover, el gestor vuelve a buscarPorDNI y castea a Huesped
//        Huesped huesped = crearHuespedConCuit(cuit, dni, tipo);
//        when(alojadoDAO.buscarPorDNI(dni, tipo)).thenReturn(huesped);
//
//        PersonaJuridicaDTO dto = gestor.buscarCriteriosAlojadoPorCuit(cuit);
//        assertNotNull(dto);
//
//        verify(alojadoDAO, times(1)).promoverAHuesped(eq(dni), eq(tipo.toString()));
//        verify(alojadoDAO, times(1)).buscarPorDNI(eq(dni), eq(tipo));
//    }
//
//    @Test
//    public void buscarCriteriosAlojadoPorCuit_siYaEsHuesped_noPromueve() {
//        String cuit = "20333444556";
//        String dni = "123";
//        TipoDoc tipo = TipoDoc.DNI;
//
//        Huesped huesped = crearHuespedConCuit(cuit, dni, tipo);
//        when(alojadoDAO.buscarAlojado(cuit)).thenReturn(huesped);
//
//        PersonaJuridicaDTO dto = gestor.buscarCriteriosAlojadoPorCuit(cuit);
//        assertNotNull(dto);
//
//        verify(alojadoDAO, never()).promoverAHuesped(anyString(), anyString());
//        verify(alojadoDAO, never()).buscarPorDNI(anyString(), any());
//    }

    // ---------- Helpers de dominio (mínimos, con CUIT seteado) ----------

    private Invitado crearInvitadoConCuit(String cuit, String nroDoc, TipoDoc tipoDoc) {
        DatosContacto dc = new DatosContacto("111", "a@a.com");
        DatosResidencia dr = new DatosResidencia("Calle", "Loc", "Prov", "Dpto", "Pais", "1", "1", "3000");
        DatosPersonales dp = new DatosPersonales(
                "Perez", "Juan", "argentino", "CF", nroDoc, tipoDoc, "Estudiante", cuit, LocalDate.parse("2000-01-01")
        );
        DatosAlojado da = new DatosAlojado(dc, dr, dp);
        return new Invitado(da);
    }

    private Huesped crearHuespedConCuit(String cuit, String nroDoc, TipoDoc tipoDoc) {
        DatosContacto dc = new DatosContacto("111", "a@a.com");
        DatosResidencia dr = new DatosResidencia("Calle", "Loc", "Prov", "Dpto", "Pais", "1", "1", "3000");
        DatosPersonales dp = new DatosPersonales(
                "Perez", "Juan", "argentino", "CF", nroDoc, tipoDoc, "Estudiante", cuit, LocalDate.parse("2000-01-01")
        );
        DatosAlojado da = new DatosAlojado(dc, dr, dp);
        return new Huesped(da);
    }
}
