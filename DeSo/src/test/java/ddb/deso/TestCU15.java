package ddb.deso;

import ddb.deso.controller.HabitacionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(true)
public class TestCU15 {

    @Autowired
    private HabitacionController habitacionController;


    @Test
    public void buscarHabs(){
        var listaHabs = habitacionController.listarTodaHabitacion().getBody();
        for(var x : listaHabs){
            System.out.println(x.getNroHab());
        }
    }



}
