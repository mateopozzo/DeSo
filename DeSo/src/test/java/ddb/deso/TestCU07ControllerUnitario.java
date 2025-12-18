package ddb.deso;

import com.fasterxml.jackson.databind.ObjectMapper;
import ddb.deso.almacenamiento.DTO.DetalleFacturaDTO;
import ddb.deso.almacenamiento.DTO.EstadiaDTO;
import ddb.deso.almacenamiento.DTO.FacturaDTO;
import ddb.deso.almacenamiento.DTO.GenerarFacturaRequestDTO;
import ddb.deso.controller.FacturaController;
import ddb.deso.negocio.TipoFactura;
import ddb.deso.negocio.habitaciones.Estadia;
import ddb.deso.negocio.habitaciones.Habitacion;
import ddb.deso.service.GestorContabilidad;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(FacturaController.class)
public class TestCU07ControllerUnitario {

    @MockitoBean
    private GestorContabilidad gestorContabilidad;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void verificarEstadia_RetornaEstadiaDTO_CuandoExiste() throws Exception {
        Estadia estadiaMock = new Estadia();
        estadiaMock.setIdEstadia(10L);
        estadiaMock.setFecha_inicio(LocalDate.now());
        estadiaMock.setHabitacion(new Habitacion());
        
        when(gestorContabilidad.existeEstadia(anyLong())).thenReturn(estadiaMock);

        mockMvc.perform(get("/api/facturacion/habitacion/101/verificar-estadia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEstadia").value(10));
    }

    @Test
    public void verificarEstadia_RetornaNotFound_CuandoNoExiste() throws Exception {
        when(gestorContabilidad.existeEstadia(anyLong())).thenThrow(new Exception("No encontrada"));

        mockMvc.perform(get("/api/facturacion/habitacion/101/verificar-estadia"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void obtenerDetalle_RetornaDTO_Correctamente() throws Exception {
        DetalleFacturaDTO detalleMock = new DetalleFacturaDTO(
                1L, "Habitacion 101", 5000.0, new ArrayList<>(), 5000.0, TipoFactura.B
        );

        when(gestorContabilidad.calcularDetalleFacturacion(anyLong(), any(LocalTime.class)))
                .thenReturn(detalleMock);

        mockMvc.perform(get("/api/facturacion/habitacion/101/detalle")
                        .param("horaSalida", "10:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.montoTotal").value(5000.0));
    }

    @Test
    public void generarFactura_Exito_200() throws Exception {
        // Uso de Builder para evitar error de constructor
        GenerarFacturaRequestDTO request = GenerarFacturaRequestDTO.builder()
                .idEstadia(1L)
                .responsableId("100")
                .build();

        FacturaDTO facturaMock = new FacturaDTO();
        facturaMock.setImporte_total(10000.0f);
        facturaMock.setTipo_factura(TipoFactura.B);

        when(gestorContabilidad.generarFactura(any(GenerarFacturaRequestDTO.class)))
                .thenReturn(facturaMock);

        String jsonRequest = mapper.writeValueAsString(request);

        mockMvc.perform(post("/api/facturacion/generar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.importe_total").value(10000.0));
    }

    @Test
    public void generarFactura_Falla_BadRequest() throws Exception {
        GenerarFacturaRequestDTO request = GenerarFacturaRequestDTO.builder()
                .idEstadia(1L)
                .responsableId("100")
                .build();
        
        doThrow(new Exception("Error validacion"))
                .when(gestorContabilidad).generarFactura(any(GenerarFacturaRequestDTO.class));

        String jsonRequest = mapper.writeValueAsString(request);

        mockMvc.perform(post("/api/facturacion/generar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }
}