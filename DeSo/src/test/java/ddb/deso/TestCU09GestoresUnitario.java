package ddb.deso;

import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.service.alojamiento.FactoryAlojado;
import ddb.deso.gestores.GestorAlojamiento;
import ddb.deso.gestores.excepciones.AlojadoInvalidoException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Clase de pruebas unitarias para el Caso de Uso 09 (CU09): Alta de Huésped/Alojado.
 * <p>
 * Esta clase verifica la lógica de negocio contenida en {@link GestorAlojamiento},
 * asegurando que las validaciones de datos (campos obligatorios, nulidad, vacuidad)
 * funcionen correctamente antes de intentar la persistencia.
 * </p>
 * <p>
 * Se utiliza {@link SpringBootTest} para levantar el contexto necesario y
 * {@link MockitoBean} para aislar la capa de persistencia ({@link AlojadoDAO}),
 * permitiendo probar el gestor sin impactar la base de datos real.
 * </p>
 *
 * @see GestorAlojamiento
 * @see AlojadoDAO
 */
@SpringBootTest
public class TestCU09GestoresUnitario {

    @Autowired
    GestorAlojamiento gestor;

    @MockitoBean
    AlojadoDAO dao;

    /**
     * Método auxiliar para construir un {@link AlojadoDTO} válido y completo.
     * <p>
     * Sirve como base para las pruebas:
     * <ul>
     * <li>Se usa tal cual para pruebas de camino feliz (Happy Path).</li>
     * <li>Se modifica puntualmente para pruebas de casos negativos (validación de errores).</li>
     * </ul>
     * </p>
     *
     * @return Una instancia de AlojadoDTO con todos sus campos obligatorios poblados correctamente.
     */
    private AlojadoDTO crearAlojadoDTO(){
        AlojadoDTO ret = new AlojadoDTO();
        ret.setApellido("Daverio");
        ret.setNombre("Juan");
        ret.setNacionalidad("argentino");
        ret.setFechanac("1984-08-15");
        ret.setTipoDoc(TipoDoc.DNI);
        ret.setNroDoc("34987783");
        ret.setTelefono("543424894085");
        ret.setEmail("david@gmail.com");
        ret.setCalle("Lavaisse");
        ret.setNroCalle("2341");
        ret.setPiso("3");
        ret.setCodPost("4540");
        ret.setPais("Italia");
        ret.setProv("Milan");
        ret.setLocalidad("Milan");
        ret.setOcupacion("Electricista");
        ret.setCUIT("20349877831");
        ret.setPosicionIva("Excentos");
        return ret;
    }

    /**
     * Verifica que el gestor rechace un objeto {@code null} como argumento.
     * <p>
     * Se espera que se lance una {@link AlojadoInvalidoException} inmediatamente.
     * </p>
     */
    @Test
    public void gestorRecibeNulo(){
        assertThrows(AlojadoInvalidoException.class, ()->gestor.darDeAltaHuesped(null));
    }

    /**
     * Verifica la validación de campos obligatorios cuando estos son {@code null}.
     * <p>
     * Escenario: Se crea un alojado válido y se fuerza el campo 'Nombre' a null.
     * Resultado esperado: {@link AlojadoInvalidoException}.
     * </p>
     */
    @Test
    public void campoObligatorioNulo(){
        var a = crearAlojadoDTO();
        a.setNombre(null);
        var alojado = FactoryAlojado.createFromDTO(a);
        assertThrows(AlojadoInvalidoException.class, ()->gestor.darDeAltaHuesped(alojado));
    }

    /**
     * Verifica la validación de campos obligatorios cuando estos son cadenas vacías.
     * <p>
     * Escenario: Se crea un alojado válido y se fuerza el campo 'Nombre' a "" (string vacío).
     * Resultado esperado: {@link AlojadoInvalidoException}.
     * </p>
     */
    @Test
    public void campoObligatorioQuedaVacio(){
        var a = crearAlojadoDTO();
        a.setNombre("");
        var alojado = FactoryAlojado.createFromDTO(a);
        assertThrows(AlojadoInvalidoException.class, ()->gestor.darDeAltaHuesped(alojado));
    }

    /**
     * Verifica el "caminito feliz" del gestor.
     * <p>
     * Escenario: Se crea un alojado válido.
     * Resultado esperado: void.
     * </p>
     */
    @Test
    public void gestorRecibeAljadoValido(){
        var a = crearAlojadoDTO();
        var alojado = FactoryAlojado.createFromDTO(a);
        assertDoesNotThrow(()->gestor.darDeAltaHuesped(alojado), "Recibir un alojado valido no deberia arrojar excepcion");
    }
// TODO -> Probar nulidad campo por campo
// TODO -> Probar vacuidad campo por campo

//    apellido: formData.apellido,
//    nombre: formData.nombre,
//    nacionalidad: formData.nacionalidad,
//    fechanac: formData.fechaNacimiento,
//    tipoDoc: formData.tipo_documento,
//    nroDoc: formData.numeroDocumento,
//    telefono: formData.telefono,
//    email: formData.email,
//    calle: formData.calle,
//    nroCalle: formData.numeroCalle,
//    piso: formData.piso,
//    codPost: formData.codPostal,
//    pais: formData.paisResidencia,
//    prov: formData.provincia,
//    localidad: formData.localidad,
//    ocupacion: formData.ocupacion,
//    cuit: formData.cuit,
//    posicionIva: formData.iva,

//    { key: "apellido", etiq: "apellido" },
//    { key: "nombre", etiq: "nombre" },
//    { key: "nacionalidad", etiq: "nacionalidad" },
//    { key: "fechaNacimiento", etiq: "fecha de nacimiento" },
//    { key: "tipo_documento", etiq: "tipo de doc." },
//    { key: "numeroDocumento", etiq: "nro. de doc." },
//    { key: "calle", etiq: "calle" },
//    { key: "numeroCalle", etiq: "nro. de calle" },
//    { key: "codPostal", etiq: "cód. postal" },
//    { key: "paisResidencia", etiq: "país" },
//    { key: "provincia", etiq: "provincia" },
//    { key: "localidad", etiq: "localidad" },
//    { key: "ocupacion", etiq: "ocupación" },
//    { key: "iva", etiq: "posición frente al IVA" },


}
