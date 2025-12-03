package ddb.deso;

import ddb.deso.almacenamiento.DAO.AlojadoDAO;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.alojamiento.Alojado;
import ddb.deso.alojamiento.FactoryAlojado;
import ddb.deso.gestores.GestorAlojamiento;
import ddb.deso.gestores.excepciones.AlojadoInvalidoException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class TestCU09ServiceUnitario {

    @Autowired
    GestorAlojamiento gestor;

    @MockitoBean
    AlojadoDAO dao;

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

    @Test
    public void gestorRecibeNulo(){
        assertThrows(AlojadoInvalidoException.class, ()->gestor.darDeAltaHuesped(null));
    }

    @Test
    public void campoObligatorioNulo(){
        var a = crearAlojadoDTO();
        a.setNombre(null);
        var alojado = FactoryAlojado.createFromDTO(a);
        assertThrows(AlojadoInvalidoException.class, ()->gestor.darDeAltaHuesped(alojado));
    }

    @Test
    public void campoObligatorioQuedaVacio(){
        var a = crearAlojadoDTO();
        a.setNombre("");
        var alojado = FactoryAlojado.createFromDTO(a);
        assertThrows(AlojadoInvalidoException.class, ()->gestor.darDeAltaHuesped(alojado));
    }

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
