package ddb.deso;

import ddb.deso.almacenamiento.DTO.CriteriosBusq;
import ddb.deso.service.GestorAlojamiento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class TestCU07Integracion {

    @Autowired
    private GestorAlojamiento gestorAlojamiento;

    @Test
    @Transactional
    public void pruebaDeQueryPorId(){
        List<CriteriosBusq> x = gestorAlojamiento.buscarCriteriosALojadoDeEstadia(15);

        System.out.println("dou " + x.size() );
        for(var coso : x){
            System.out.println(coso.getNombre());
            System.out.println(coso.getApellido());
            System.out.println(coso.getTipoDoc());
            System.out.println(coso.getNroDoc());
        }
    }

}
