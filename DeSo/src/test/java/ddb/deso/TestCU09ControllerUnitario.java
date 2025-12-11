package ddb.deso;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.service.TipoDoc;
import ddb.deso.service.alojamiento.Alojado;
import ddb.deso.controller.AlojadoController;
import ddb.deso.gestores.GestorAlojamiento;
import ddb.deso.gestores.excepciones.AlojadoInvalidoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas unitarias para el controlador del Caso de Uso 09 (Alta de Huésped).
 * <p>
 * Utiliza {@link WebMvcTest} para validar exclusivamente la capa web (endpoints, serialización JSON,
 * códigos HTTP) aislando la lógica de negocio mediante un mock de {@link GestorAlojamiento}.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(AlojadoController.class)
public class TestCU09ControllerUnitario {


    @Autowired
    AlojadoController controller;

    @MockitoBean
    GestorAlojamiento gestorMock;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    private boolean forzar = false;
    private ObjectMapper objectMapper;

    /**
     * Genera un DTO válido para usar como base en las peticiones HTTP.
     * @return Instancia de {@link AlojadoDTO} con datos completos.
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
     * Configura el parámetro 'force' de la petición.
     * @param valorDeVerdad Entero positivo para true, 0 para false.
     */
    private void setForzar(int valorDeVerdad){
        forzar = valorDeVerdad > 0;
    }

    /**
     * Configura el comportamiento del mock {@code gestor.dniExiste()}.
     * @param valorDeVerdad Entero positivo hace que el mock retorne true.
     */
    private void setDniExiste(int valorDeVerdad){
        when(gestorMock.dniExiste(any(String.class), any(TipoDoc.class))).thenReturn(valorDeVerdad>0);
    }

    /**
     * Configura si el método {@code gestor.darDeAltaHuesped()} debe lanzar excepción.
     * @param valorDeVerdad Entero positivo provoca una {@link AlojadoInvalidoException}.
     */
    private void setGestorDevuelveExcepcion(int valorDeVerdad){
        if(valorDeVerdad>0){
            doThrow(new AlojadoInvalidoException("Alojado invalido")).when(gestorMock).darDeAltaHuesped(any(AlojadoDTO.class));
        } else {
            doNothing().when(gestorMock).darDeAltaHuesped(any(AlojadoDTO.class));
        }
    }


    /**
     * Ejecuta una prueba combinatoria exhaustiva de los escenarios del controlador.
     * <p>
     * Utiliza un bucle de 0 a 7 (representación binaria de 3 bits) para alternar las condiciones:
     * <ul>
     * <li><b>Bit 2 (4):</b> Parámetro {@code force} (true/false).</li>
     * <li><b>Bit 1 (2):</b> Mock {@code dniExiste} (true/false).</li>
     * <li><b>Bit 0 (1):</b> Mock lanza {@code Exception} (true/false).</li>
     * </ul>
     * Se validan los códigos de estado HTTP resultantes:
     * <ul>
     * <li><b>201 Created:</b> Escenarios válidos (0, 4, 6).</li>
     * <li><b>400 Bad Request:</b> Cuando el gestor lanza excepción (1, 5, 7).</li>
     * <li><b>409 Conflict:</b> Cuando el DNI existe y no se fuerza la creación (2, 3).</li>
     * </ul>
     */
    @Test
    public void combinacionesCapaController() throws Exception {
        var a = crearAlojadoDTO();
        String mandar;
        try{
            mandar = mapper.writeValueAsString(a);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        for(int i=0;i<8;i++){
            setForzar(i&(1<<2));
            setDniExiste(i&(1<<1));
            setGestorDevuelveExcepcion(i&1);
            var resultado = mockMvc.perform(post("/api/huesped").param("force", String.valueOf(forzar)).contentType(MediaType.APPLICATION_JSON).content(mandar));
            if(i == 0 || i == 4 || i == 6){
                //  Se espera que sea creado
                System.out.println(i);
                resultado.andExpect(status().isCreated());
            } else if(i == 1 || i == 5 || i == 7){
                //  Se espera que sea badRequest
                System.out.println(i);
                resultado.andExpect(status().isBadRequest());
            } else {
                //  Se espera conflicto
                System.out.println(i);
                resultado.andExpect(status().isConflict());
            }
        }
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
