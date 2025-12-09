package ddb.deso;

import com.fasterxml.jackson.databind.ObjectMapper;
import ddb.deso.almacenamiento.DTO.ReservaDTO;
import ddb.deso.controller.HabitacionController;
import ddb.deso.gestores.GestorHabitacion;
import ddb.deso.service.EstadoHab;
import ddb.deso.service.TipoHab;
import ddb.deso.service.habitaciones.Estadia;
import ddb.deso.service.habitaciones.Habitacion;
import ddb.deso.service.habitaciones.Reserva;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(HabitacionController.class)
public class TestCU04ControllerUnitario {

    @MockitoBean
    private GestorHabitacion gestorHabitacion;

    @Autowired
    private HabitacionController habitacionController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    private List<Long> crearListaIdHabitaciones(){
        return List.of(101L, 202L);
    }

    private List<Estadia> crearListaEstadiasConflictiva(){
        Estadia e = new Estadia();
        e.setFecha_inicio(LocalDate.now());
        e.setFecha_fin(LocalDate.parse("2025-12-02"));
        Habitacion h = new Habitacion();
        h.setNroHab(101L);
        h.setEstado_hab(EstadoHab.DISPONIBLE);
        h.setTipo_hab(TipoHab.DOBLEESTANDAR);
        h.setTarifa(1);
        h.setCapacidad(1);
        e.setHabitacion(h);
        e.setIdEstadia(1);
        return List.of(e);
    }

    private ReservaDTO crearDTO(){
        ReservaDTO r = new ReservaDTO();
        r.setFecha_inicio(LocalDate.parse("2025-12-01"));
        r.setFecha_fin(LocalDate.now());
        r.setNombre("Constantino");
        r.setApellido("Bilindeo");
        r.setTelefono("123456789");
        return r;
    }

    @Test
    public void reservaConFechaInicioNula() throws Exception {
        var r = crearDTO();
        r.setFecha_inicio(null);
        var l = crearListaIdHabitaciones();
        Map<String, Object> body = Map.of(
                "reservaDTO", r,
                "listaIDHabitaciones", l
        );
        String jsonParaEnviar = mapper.writeValueAsString(body);
        mockMvc.perform(post("/api/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParaEnviar))
                        .andExpect(status().isBadRequest());
    }

//    @Test
//    public void reservaConFechaInicioVacia() throws Exception {
//        var r = crearDTO();
//        r.setFecha_inicio(LocalDate.parse(""));
//        var l = crearListaIdHabitaciones();
//        Map<String, Object> body = Map.of(
//                "reservaDTO", r,
//                "listaIDHabitaciones", l
//        );
//        String jsonParaEnviar = mapper.writeValueAsString(body);
//        mockMvc.perform(post("/api/reserva")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonParaEnviar))
//                .andExpect(status().isBadRequest());
//    }

    @Test
    public void reservaConFechaInicioIFutura() throws Exception {
        var r = crearDTO();
        r.setFecha_inicio(LocalDate.parse("2027-11-02"));
        var l = crearListaIdHabitaciones();
        Map<String, Object> body = Map.of(
                "reservaDTO", r,
                "listaIDHabitaciones", l
        );
        String jsonParaEnviar = mapper.writeValueAsString(body);
        mockMvc.perform(post("/api/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParaEnviar))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void reservaConFechaFinNula() throws Exception {
        var r = crearDTO();
        r.setFecha_fin(null);
        var l = crearListaIdHabitaciones();
        Map<String, Object> body = Map.of(
                "reservaDTO", r,
                "listaIDHabitaciones", l
        );
        String jsonParaEnviar = mapper.writeValueAsString(body);
        mockMvc.perform(post("/api/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParaEnviar))
                .andExpect(status().isBadRequest());
    }

//    @Test  // TODO -> Hacer que este caso envie una fecha "" en json
//    public void reservaConFechaFinVacia() throws Exception {
//        var r = crearDTO();
//        r.setFecha_fin(LocalDate.parse(""));
//        var l = crearListaIdHabitaciones();
//        Map<String, Object> body = Map.of(
//                "reservaDTO", r,
//                "listaIDHabitaciones", l
//        );
//        String jsonParaEnviar = mapper.writeValueAsString(body);
//        mockMvc.perform(post("/api/reserva")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonParaEnviar))
//                .andExpect(status().isBadRequest());
//    }

    @Test
    public void reservaConNombreNulo() throws Exception {
        var r = crearDTO();
        r.setNombre(null);
        var l = crearListaIdHabitaciones();
        Map<String, Object> body = Map.of(
                "reservaDTO", r,
                "listaIDHabitaciones", l
        );
        String jsonParaEnviar = mapper.writeValueAsString(body);
        mockMvc.perform(post("/api/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParaEnviar))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void reservaConNombreVacio() throws Exception {
        var r = crearDTO();
        r.setNombre("");
        var l = crearListaIdHabitaciones();
        Map<String, Object> body = Map.of(
                "reservaDTO", r,
                "listaIDHabitaciones", l
        );
        String jsonParaEnviar = mapper.writeValueAsString(body);
        mockMvc.perform(post("/api/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParaEnviar))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void reservaConApellidoNulo() throws Exception {
        var r = crearDTO();
        r.setApellido(null);
        var l = crearListaIdHabitaciones();
        Map<String, Object> body = Map.of(
                "reservaDTO", r,
                "listaIDHabitaciones", l
        );
        String jsonParaEnviar = mapper.writeValueAsString(body);
        mockMvc.perform(post("/api/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParaEnviar))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void reservaConApellidoVacio() throws Exception {
        var r = crearDTO();
        r.setApellido("");
        var l = crearListaIdHabitaciones();
        Map<String, Object> body = Map.of(
                "reservaDTO", r,
                "listaIDHabitaciones", l
        );
        String jsonParaEnviar = mapper.writeValueAsString(body);
        mockMvc.perform(post("/api/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParaEnviar))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void reservaConTelefonoNulo() throws Exception {
        var r = crearDTO();
        r.setTelefono(null);
        var l = crearListaIdHabitaciones();
        Map<String, Object> body = Map.of(
                "reservaDTO", r,
                "listaIDHabitaciones", l
        );
        String jsonParaEnviar = mapper.writeValueAsString(body);
        mockMvc.perform(post("/api/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParaEnviar))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void reservaConTelefonoVacio() throws Exception {
        var r = crearDTO();
        r.setTelefono("");
        var l = crearListaIdHabitaciones();
        Map<String, Object> body = Map.of(
                "reservaDTO", r,
                "listaIDHabitaciones", l
        );
        String jsonParaEnviar = mapper.writeValueAsString(body);
        mockMvc.perform(post("/api/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParaEnviar))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void dtoReservaValido() throws Exception {
        when(gestorHabitacion.listarEstadias(any(LocalDate.class), any(LocalDate.class))).thenReturn(null);
        when(gestorHabitacion.listarReservas(any(LocalDate.class), any(LocalDate.class))).thenReturn(null);
        var r = crearDTO();
        var l = crearListaIdHabitaciones();
        Map<String, Object> body = Map.of(
                "reservaDTO", r,
                "listaIDHabitaciones", l
        );
        String jsonParaEnviar = mapper.writeValueAsString(body);
        mockMvc.perform(post("/api/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParaEnviar))
                .andExpect(status().isCreated());
    }

    @Test
    public void reservaConflictivaConOtraEstadia() throws Exception {
        when(gestorHabitacion.listarEstadias(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(crearListaEstadiasConflictiva());
        var r = crearDTO();
        var l = crearListaIdHabitaciones();
        Map<String, Object> body = Map.of(
                "reservaDTO", r,
                "listaIDHabitaciones", l
        );
        String jsonParaEnviar = mapper.writeValueAsString(body);
        mockMvc.perform(post("/api/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParaEnviar))
                .andExpect(status().isConflict());
    }

    @Test
    public void reservaConflictivaConOtrasReservas() throws Exception {
        when(gestorHabitacion.listarReservas(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(crearListaReservasConflictivas());
        var r = crearDTO();
        var l = crearListaIdHabitaciones();
        Map<String, Object> body = Map.of(
                "reservaDTO", r,
                "listaIDHabitaciones", l
        );
        String jsonParaEnviar = mapper.writeValueAsString(body);
        mockMvc.perform(post("/api/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonParaEnviar))
                .andExpect(status().isConflict());
    }

    private List<Reserva> crearListaReservasConflictivas() {
        Reserva e = new Reserva();
        e.setFecha_inicio(LocalDate.now());
        e.setFecha_fin(LocalDate.parse("2025-12-02"));
        e.setNombre("me");
        e.setApellido("mo");
        e.setEstado("RESERVADO");
        e.setIdReserva(1L);

        e.setListaHabitaciones(crearListaHabitaciones());
        return List.of(e);
    }

    @Test
    public void listaHabitacionesDevuelveNull() throws Exception {
        when(gestorHabitacion.listarHabitaciones())
                .thenReturn(null);
        mockMvc.perform(get("/api/habitacion")).andExpect(status().isBadRequest());
    }


    @Test
    public void listaHabitacionesDevuelveVacio() throws Exception {
        when(gestorHabitacion.listarHabitaciones())
                .thenReturn(List.of());
        mockMvc.perform(get("/api/habitacion")).andExpect(status().isBadRequest());
    }

    @Test
    public void listaHabitacionesDevuelveAlgo() throws Exception {
        when(gestorHabitacion.listarHabitaciones())
                .thenReturn(crearListaHabitaciones());
        mockMvc.perform(get("/api/habitacion")).andExpect(status().isOk());
    }

    private List<Habitacion> crearListaHabitaciones() {
        Habitacion h1 = new Habitacion(TipoHab.DOBLEESTANDAR,EstadoHab.DISPONIBLE,1,2), h2 = new Habitacion(TipoHab.DOBLEESTANDAR,EstadoHab.DISPONIBLE,1,2);
        h1.setNroHab(101L);
        h2.setNroHab(202L);
        return List.of(h1,h2);
    }

}
