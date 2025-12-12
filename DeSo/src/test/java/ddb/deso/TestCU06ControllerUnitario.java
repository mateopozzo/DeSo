package ddb.deso;

import com.fasterxml.jackson.databind.ObjectMapper;
import ddb.deso.almacenamiento.DTO.BuscarReservaDTO;
import ddb.deso.almacenamiento.DTO.HabitacionReservaDTO;
import ddb.deso.almacenamiento.DTO.ReservaGrillaDTO;
import ddb.deso.controller.HabitacionController;
import ddb.deso.negocio.TipoHab;
import ddb.deso.service.GestorHabitacion;
import ddb.deso.service.excepciones.ReservaInexistenteException;
import ddb.deso.service.excepciones.ApellidoVacioException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HabitacionController.class)
public class TestCU06ControllerUnitario {

    @MockitoBean
    private GestorHabitacion gestorHabitacion;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private BuscarReservaDTO dtoBusquedaValido() {
        BuscarReservaDTO dto = new BuscarReservaDTO();
        dto.setApellido("Perez");
        dto.setNombre("Juan");
        return dto;
    }

    private List<ReservaGrillaDTO> respuestaEjemplo() {
        HabitacionReservaDTO h1 = new HabitacionReservaDTO(101L, TipoHab.DOBLEESTANDAR);
        HabitacionReservaDTO h2 = new HabitacionReservaDTO(102L, TipoHab.SUITE);

        ReservaGrillaDTO r = new ReservaGrillaDTO(
                8L,
                "Perez",
                "Juan",
                LocalDate.parse("2026-01-16"),
                LocalDate.parse("2026-01-19"),
                List.of(h1, h2)
        );
        return List.of(r);
    }

    @Test
    public void postBuscarReservas_ok_devuelve200() throws Exception {
        when(gestorHabitacion.buscarReservasPorApellidoNombre("Perez", "Juan"))
                .thenReturn(respuestaEjemplo());

        String json = mapper.writeValueAsString(dtoBusquedaValido());

        mockMvc.perform(post("/api/reservas/buscar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(gestorHabitacion).buscarReservasPorApellidoNombre("Perez", "Juan");
    }

    @Test
    public void postBuscarReservas_apellidoVacio_devuelve400() throws Exception {
        doThrow(new ApellidoVacioException("El campo apellido no puede estar vacío"))
                .when(gestorHabitacion).buscarReservasPorApellidoNombre(eq(""), any());

        BuscarReservaDTO dto = new BuscarReservaDTO();
        dto.setApellido(""); // inválido
        dto.setNombre("Juan");

        String json = mapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/reservas/buscar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postBuscarReservas_nombreNulo_ok_devuelve200() throws Exception {
        when(gestorHabitacion.buscarReservasPorApellidoNombre("Perez", null))
                .thenReturn(respuestaEjemplo());

        BuscarReservaDTO dto = new BuscarReservaDTO();
        dto.setApellido("Perez");
        dto.setNombre(null);

        String json = mapper.writeValueAsString(dto);

        mockMvc.perform(post("/api/reservas/buscar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        verify(gestorHabitacion).buscarReservasPorApellidoNombre("Perez", null);
    }

    @Test
    public void postBuscarReservas_bodyNulo_devuelve400() throws Exception {
        // si mandás JSON null -> dto == null -> gestor recibe (null,null) -> ApellidoVacioException -> 400
        doThrow(new ApellidoVacioException("El campo apellido no puede estar vacío"))
                .when(gestorHabitacion).buscarReservasPorApellidoNombre(isNull(), isNull());

        mockMvc.perform(post("/api/reservas/buscar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("null"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteCancelarReserva_ok_devuelve204() throws Exception {
        doNothing().when(gestorHabitacion).cancelarReserva(8L);

        mockMvc.perform(delete("/api/reservas/8"))
                .andExpect(status().isNoContent());

        verify(gestorHabitacion).cancelarReserva(8L);
    }

    @Test
    public void deleteCancelarReserva_noExiste_devuelve404() throws Exception {
        doThrow(new ReservaInexistenteException("No existe")).when(gestorHabitacion).cancelarReserva(999L);

        mockMvc.perform(delete("/api/reservas/999"))
                .andExpect(status().isNotFound());

        verify(gestorHabitacion).cancelarReserva(999L);
    }
}
