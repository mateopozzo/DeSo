package ddb.deso;

import ddb.deso.almacenamiento.DTO.CrearEstadiaDTO;
import ddb.deso.alojamiento.CriteriosBusq;
import ddb.deso.gestores.GestorHabitacion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback(false)
public class TestCU15 {

    private final GestorHabitacion gestorHabitacion;

    @Autowired
    public TestCU15(GestorHabitacion gestorHabitacion) {
        this.gestorHabitacion = gestorHabitacion;
    }

    private CrearEstadiaDTO funcionarCrearEstadia() {
        CrearEstadiaDTO estadiaDTO = new CrearEstadiaDTO();
        estadiaDTO.setIdHabitacion(101L);
        LocalDate fi = LocalDate.parse("2025-11-26");
        LocalDate ff = LocalDate.parse("2025-11-28");
        estadiaDTO.setFechaInicio(fi);
        estadiaDTO.setFechaFin(ff);
        CriteriosBusq crit = new CriteriosBusq(null,null,TipoDoc.DNI,"40543210");
        estadiaDTO.setEncargado(crit);
        List<CriteriosBusq> listaCriteriosBusq = new ArrayList<CriteriosBusq>();
        listaCriteriosBusq.add(new CriteriosBusq(null,null,TipoDoc.DNI,"41345678"));
        listaCriteriosBusq.add(new CriteriosBusq(null,null,TipoDoc.PASAPORTE,"CHL56789"));
        listaCriteriosBusq.add(new CriteriosBusq(null,null,TipoDoc.LC,"12567890"));
        estadiaDTO.setListaInvitados(listaCriteriosBusq);
        return estadiaDTO;
    }

    @Test
    void guardarEstadia() {
        var estadia = funcionarCrearEstadia();
        gestorHabitacion.ocuparHabitacion(
                estadia.getIdHabitacion(),
                estadia.getEncargado(),
                estadia.getListaInvitados(),
                estadia.getFechaInicio(),
                estadia.getFechaFin()
        );
    }



}
