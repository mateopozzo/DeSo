package ddb.deso;

import ddb.deso.alojamiento.*;
import ddb.deso.gestores.GestorAlojamiento;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
@Transactional
@Rollback
public class TestCU09 {

    @Autowired
    GestorAlojamiento gestor;

    @Autowired
    public TestCU09(GestorAlojamiento gestor) {this.gestor = gestor;}


    public Alojado crearAlojadoUno(){
        DatosContacto dc = new DatosContacto("5342334", "prueba@gmail.com");
        DatosPersonales dp = new DatosPersonales("Perez", "Juan", "argentino", "Excento", "45673829", TipoDoc.DNI, "Desocupado", "", LocalDate.parse("2005-03-14"));
        DatosResidencia dr = new DatosResidencia("Lavaisse", "Capital", "Santa Fe", "Santa Fe", "Argentina", "", "","3000");
        DatosAlojado da = new DatosAlojado(dc, dr, dp);
        return new Invitado(da);
    }

    public Alojado crearAlojadoDos(){
        DatosContacto dc = new DatosContacto("35532345", "prueba@gmail.com");
        DatosPersonales dp = new DatosPersonales("Ramiez", "Sean", "mexicano", "Excento", "45673829", TipoDoc.DNI, "Desocupado", "", LocalDate.parse("2003-02-01"));
        System.out.println("en dp" + dp.getNroDoc());
        DatosResidencia dr = new DatosResidencia("Ricardo Aldao", "Capital", "Santa Fe", "Santa Fe", "Argentina", "", "","3000");
        DatosAlojado da = new DatosAlojado(dc, dr, dp);
        System.out.println("en datos" + da.getNroDoc());
        return new Invitado(da);
    }
    /**
     *  Test -> Que oocurre cuando el Conserje apreta aceptar igualmente frente a tipo y nro doc repetido
     *  Comportamiento esperado -> El ultimo en guardarse persiste
     */
    @Test
    public void altaAlojadoConIDrepetido(){
        var alojado1 = crearAlojadoUno();
        var alojado2 = crearAlojadoDos();
        CriteriosBusq crit = new CriteriosBusq("Ramiez", "Sean", TipoDoc.DNI, "45673829");
        Alojado resultado = null;

        System.out.println("en invitado datos dp" + alojado1.getDatos().getDatos_personales().getNroDoc());
        System.out.println("en invitado datos " + alojado1.getDatos().getNroDoc());
        System.out.println("en invitado alojid "+alojado1.getId().getNroDoc());

        gestor.darDeAltaHuesped(alojado1);

        System.out.println("Primer alojado guardado");

        gestor.darDeAltaHuesped(alojado2);

        var lista = gestor.buscarAlojado(crit);

        assertEquals("Cantidad encontrados distinta a 1",1,lista.size());
        resultado = lista.getFirst();


        assertEquals("Resultado no es el segundo guardado", alojado2, resultado);


    }

}
