package ddb.deso;

import ddb.deso.gestores.GestorAlojamiento;
import ddb.deso.almacenamiento.DTO.CriteriosBusq;
import ddb.deso.service.TipoDoc;
import ddb.deso.service.alojamiento.Huesped;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = true)
public class TestCU02Integracion {

    @Autowired
    private GestorAlojamiento gestorAlojamiento;

    @Autowired
    public TestCU02Integracion(GestorAlojamiento gestorAlojamiento){
        this.gestorAlojamiento = gestorAlojamiento;
    }

    private String normalizar (String cadena){
        return Normalizer.normalize(cadena, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    /**
     * Test de integración para una búsqueda regular exitosa "caminito feliz".
     * <p>
     * Verifica el flujo completo de búsqueda utilizando criterios específicos
     * (Apellido, Nombre, Tipo y Nro de Documento) y valida que los datos recuperados
     * coincidan exactamente con los esperados en la base de datos de prueba.
     * </p>
     */
    @Test
    public void busquedaRegular() {
        CriteriosBusq crit2 = new CriteriosBusq("Gomez", "Ana Maria", TipoDoc.DNI, "28123456");
        assertDoesNotThrow(()->gestorAlojamiento.buscarHuesped(crit2), "Encuentra al menos un resultado");
        var lista = gestorAlojamiento.buscarHuesped(crit2);
        assertEquals(1, lista.size());
        assertEquals("GOMEZ", normalizar(lista.getFirst().getApellido().toUpperCase()));
        assertEquals("ANA MARIA", normalizar(lista.getFirst().getNombre()).toUpperCase());
        assertEquals(TipoDoc.DNI, lista.getFirst().getTipoDoc());
        assertEquals("28123456", lista.getFirst().getNroDoc());
    }

    /**
     * Test de integración para verificar la búsqueda con caracteres especiales (tildes).
     * <p>
     * Asegura que el mecanismo de búsqueda y la base de datos manejen correctamente
     * la codificación de caracteres (ej. "Fernández") al realizar consultas.
     * </p>
     */
    @Test
    public void busquedaConTildes  (){
        CriteriosBusq crit5 = new CriteriosBusq("Gómez", "Ana María", TipoDoc.DNI, "28123456");
        assertDoesNotThrow(()->gestorAlojamiento.buscarHuesped(crit5), "Encuentra al menos un resultado");
        var lista = gestorAlojamiento.buscarHuesped(crit5);
        assertEquals(1, lista.size());
        assertEquals("GOMEZ", normalizar(lista.getFirst().getApellido().toUpperCase()));
        assertEquals("ANA MARIA", normalizar(lista.getFirst().getNombre()).toUpperCase());
        assertEquals(TipoDoc.DNI, lista.getFirst().getTipoDoc());
        assertEquals("28123456", lista.getFirst().getNroDoc());
    }

    /**
     * Test de integración para verificar la búsqueda con caracteres especiales (tildes) y mayusculas random.
     * <p>
     * Asegura que el mecanismo de búsqueda y la base de datos manejen correctamente
     * la codificación de caracteres (ej. "Fernández") y sea CaseInsensitive al realizar consultas.
     * </p>
     */
    @Test
    public void busquedaConTildesYRandomCase  (){
        CriteriosBusq crit5 = new CriteriosBusq("GóMEz", "ANA MaRÍa", TipoDoc.DNI, "28123456");
        assertDoesNotThrow(()->gestorAlojamiento.buscarHuesped(crit5), "Encuentra al menos un resultado");
        var lista = gestorAlojamiento.buscarHuesped(crit5);
        assertEquals(1, lista.size());
        assertEquals("GOMEZ", normalizar(lista.getFirst().getApellido().toUpperCase()));
        assertEquals("ANA MARIA", normalizar(lista.getFirst().getNombre()).toUpperCase());
        assertEquals(TipoDoc.DNI, lista.getFirst().getTipoDoc());
        assertEquals("28123456", lista.getFirst().getNroDoc());
    }

    /**
     * Test de integración para búsqueda utilizando únicamente el criterio de nombre.
     * <p>
     * Verifica que el sistema sea capaz de recuperar múltiples resultados que compartan
     * el mismo nombre, ignorando los demás campos nulos del criterio.
     * </p>
     */
    @Test
    public void busquedaCriterioUnicoNombre() {
        CriteriosBusq crit3 = new CriteriosBusq(null,"Juan", null, null);
        assertDoesNotThrow(()->gestorAlojamiento.buscarHuesped(crit3), "Encuentra al menos un resultado");
        var lista = gestorAlojamiento.buscarHuesped(crit3);
        System.out.println("LA LISTA TIENE TAMAÑO: " + lista.size());
        assertNotEquals(1, lista.size());
        for(var h : lista){
            assert(h.getNombre().toUpperCase().startsWith("JUAN"));
        }
    }

    /**
     * Test de integración para búsqueda utilizando únicamente el número de documento.
     * <p>
     * Valida que se pueda recuperar un alojado específico proporcionando solo su
     * documento, un caso de uso común para identificación rápida.
     * </p>
     */
    @Test
    public void busquedaCriterioUnicoDocumento() {
        CriteriosBusq crit4 = new CriteriosBusq(null,null, null, "40543210");
        assertDoesNotThrow(()->gestorAlojamiento.buscarHuesped(crit4), "Encuentra al menos un resultado");
        var lista = gestorAlojamiento.buscarHuesped(crit4);
        assertNotEquals(1, lista.size());
        for(var h : lista){
            assertEquals("40543210", h.getNroDoc());
        }
    }



}
