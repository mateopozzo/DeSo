package ddb.deso;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ddb.deso.almacenamiento.DTO.AlojadoDTO;
import ddb.deso.alojamiento.Alojado;
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

    private void setForzar(int valorDeVerdad){
        forzar = valorDeVerdad > 0;
    }

    private void setDniExiste(int valorDeVerdad){
        when(gestorMock.dniExiste(any(String.class), any(TipoDoc.class))).thenReturn(valorDeVerdad>0);
    }

    private void setGestorDevuelveExcepcion(int valorDeVerdad){
        if(valorDeVerdad>0){
            doThrow(new AlojadoInvalidoException("Alojado invalido")).when(gestorMock).darDeAltaHuesped(any(Alojado.class));
        } else {
            doNothing().when(gestorMock).darDeAltaHuesped(any(Alojado.class));
        }
    }


    /**
     * Método que prueba todas las combinaciones de la capa controller.
     * Las combinaciones se definen por los valores de los siguientes parametros
     * Los valores de verdad se definen por el valor que toma el primer, segundo o tercer bit de un entero
     *
     * FORZAR       DNI_EXISTE      EXCEPCION_GESTOR
     * 1 & 4           1 & 2             1 & 1
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
