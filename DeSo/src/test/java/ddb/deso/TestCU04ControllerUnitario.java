package ddb.deso;

import ddb.deso.controller.HabitacionController;
import ddb.deso.controller.ReservaController;
import ddb.deso.gestores.GestorHabitacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(value = true)
public class TestCU04ControllerUnitario {

    @MockitoBean
    private GestorHabitacion gestorHabitacion;

    @Autowired
    private HabitacionController habitacionController;

    @Autowired
    private ReservaController reservaController;



}
