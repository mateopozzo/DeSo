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


/**
 * Clase de prueba para el caso de uso de alta de un alojado (CU09).
 *
 * Objetivo: Verificar el comportamiento del sistema al intentar persistir dos
 * registros de Alojado con la misma clave primaria (TipoDoc y NroDoc), simulando
 * una operación de "upsert" (update/insert).
 */

@SpringBootTest
@Transactional
@Rollback
public class TestCU09Integracion {

    @Autowired
    GestorAlojamiento gestor;
    /**
     * Inyección de dependencia del GestorAlojamiento.
     * @param gestor Instancia inyectada de GestorAlojamiento.
     */
    @Autowired
    public TestCU09Integracion(GestorAlojamiento gestor) {this.gestor = gestor;}

    /**
     * Crea y retorna un objeto Alojado (Invitado) con datos específicos.
     * DNI: 45673829, Nombre: Juan Perez
     * @return Una instancia de Alojado (Invitado).
     */
    public Alojado crearAlojadoUno(){
        DatosContacto dc = new DatosContacto("5342334", "prueba@gmail.com");
        DatosPersonales dp = new DatosPersonales("Perez", "Juan", "argentino", "Excento", "45673829", TipoDoc.DNI, "Desocupado", "", LocalDate.parse("2005-03-14"));
        DatosResidencia dr = new DatosResidencia("Lavaisse", "Capital", "Santa Fe", "Santa Fe", "Argentina", "", "","3000");
        DatosAlojado da = new DatosAlojado(dc, dr, dp);
        return new Invitado(da);
    }

    /**
     * Crea y retorna un segundo objeto Alojado (Invitado) que comparte
     * el mismo NroDoc y TipoDoc que el Alojado Uno, pero con datos personales diferentes.
     * DNI: 45673829, Nombre: Sean Ramiez, Fecha Nac.: 2003-02-01.
     * @return Una instancia de Alojado (Invitado) con ID repetida.
     */
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
     * Verifica que al intentar dar de alta un segundo alojado con la misma
     * clave primaria (DNI y TipoDoc), el sistema realice una modificacion de los datos ya presentes
     *
     * Comportamiento esperado: La búsqueda por el ID debe retornar
     * exclusivamente el objeto con los datos del segundo alojado ('Sean Ramiez').
     */
    @Test
    public void altaAlojadoConIDrepetido(){

        // creacion de alojados
        var alojado1 = crearAlojadoUno();
        var alojado2 = crearAlojadoDos();

        // criterio de busqueda final
        CriteriosBusq crit = new CriteriosBusq(null,null, TipoDoc.DNI, "45673829");
        Alojado resultado = null;

        // prints para debugs
        System.out.println("en invitado datos dp" + alojado1.getDatos().getDatos_personales().getNroDoc());
        System.out.println("en invitado datos " + alojado1.getDatos().getNroDoc());
        System.out.println("en invitado alojid "+alojado1.getId().getNroDoc());

        gestor.darDeAltaHuesped(alojado1);// guardado primer huesped
        System.out.println("Primer alojado guardado"); //aviso

        gestor.darDeAltaHuesped(alojado2);// guardado de segundo huesped

        // resultado de la busqueda
        var lista = gestor.buscarAlojado(crit);

        // la lista tiene que tener un solo invitado
        assertEquals("Cantidad encontrados distinta a 1",1,lista.size());
        resultado = lista.getFirst();

        // el unico resultado debe ser el alojado2
        assertEquals("Resultado no es el segundo guardado", alojado2, resultado);
    }

}
